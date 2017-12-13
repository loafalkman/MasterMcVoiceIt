package se.su.dsv.mastermcvoiceit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.cardViews.CardFragment;
import se.su.dsv.mastermcvoiceit.place.HomePlace;
import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.service.BackgroundService;

public class MainActivity extends AppCompatActivity implements RecognitionListener, CardFragment.GPSController, ConnDetailsDialog.ConnDetailDialogListener {

    private static final String TAG = "main";
    public static final String LOCATION_UPDATE = "location update";
    public static final String LOCATION = "location";
    static final int RESULT_SPEECH = 7474;
    private static final String PREF_SSH_IP = "SSHIpAddress";
    private static final String PREF_SSH_USER = "SSHUsername";
    private static final String PREF_SSH_PASS = "SSHPassword";

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private Intent backgroundService;

    private ArrayList<String> resultArray;
    private Location homeLocation; // TEMP

    private FragmentManager fragmentManager;
    private CardFragment cardFragment;

    private Handler readingsHandler = new Handler();
    private Runnable updateUIReadings = new Runnable() {
        @Override
        public void run() {
            updateCardModelListener(null);
            readingsHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createHomeLocation();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        backgroundService = new Intent(this, BackgroundService.class);
        backgroundService.putExtra(BackgroundService.INTENT_KEY_GPS_ON, true);
        startService(backgroundService);

        initFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        readingsHandler.removeCallbacks(updateUIReadings);
        readingsHandler.postDelayed(updateUIReadings, 1000);
    }

    private void initFragment() {
        if (initPlaces()) {
            cardFragment = new CardFragment();
            Bundle cardFragArgs = new Bundle();
            cardFragArgs.putInt(CardFragment.KEY_PLACE_NUMBER, 0);
            cardFragment.setArguments(cardFragArgs);
            launchFragment(cardFragment);
        }
    }

    private void launchFragment(CardFragment cardFragment) {
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_main_cardsfragment, cardFragment);
        fragmentTransaction.commit();
    }

    /** returns false if waiting for user input of connection details*/
    private boolean initPlaces() {
        if (BackgroundService.places.isEmpty()) {
            SSHConnDetails homeSSH = getConnDetails();

            if (homeSSH != null) {
                try {
                    Location homeLoc = new Location("");
                    homeLoc.setLatitude(59.345613);
                    homeLoc.setLongitude(18.111798);
                    BackgroundService.places.add(new HomePlace(this, homeLoc, homeSSH));
                    return true;
                } catch (IllegalStateException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    stopService(backgroundService);
                    finish();
                    e.printStackTrace();
                }
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return null if ssh details had to be asked via dialog, dialog answer captured in dialogResult()
     */
    private SSHConnDetails getConnDetails() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String ip = prefs.getString(PREF_SSH_IP, null);
        String user = prefs.getString(PREF_SSH_USER, null);
        String pass = prefs.getString(PREF_SSH_PASS, null);

        if (ip == null || user == null || pass == null) {
            ConnDetailsDialog dialog = new ConnDetailsDialog();
            dialog.show(getFragmentManager(), "ConnDetails");
            return null;
        }

        return new SSHConnDetails(ip, user, pass);
    }

    @Override
    public void dialogResult(String ip, String usr, String pass) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_SSH_IP, ip);
        editor.putString(PREF_SSH_USER, usr);
        editor.putString(PREF_SSH_PASS, pass);
        editor.commit();

        initFragment();
    }


    /**
     * temporary listener for a temporary button :P
     */
    public void updateCardModelListener(View view) {
        if(cardFragment != null) {
            cardFragment.updateCardModels();
            cardFragment.renderAllCards();
        }
    }

    /**
     * Temporarily bound to a button for testing, should be activated after voice result.
     */
    public void voiceResult(View v) {
        doCommand("colder than last sunday");
    }

    private void doCommand(String command) {
        if(cardFragment != null) {
            cardFragment.doCommand(command);
            cardFragment.updateCardModels();
            cardFragment.renderAllCards();
        }
    }

    public void voiceInputButtonListener(View v) {
        if (SpeechRecognizer.isRecognitionAvailable(MainActivity.this)) {
            speechRecognizer.startListening(recognizerIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    resultArray = text;
                }

                break;
            }
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onRmsChanged(float v) {
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onError(int i) {
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if (matches != null && matches.size() > 0) {
            String voiceResultStr = matches.get(0).toLowerCase();
            doCommand(voiceResultStr);
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
    }

    private void createHomeLocation() {
        homeLocation = new Location("");
        homeLocation.setLatitude(59.345613);
        homeLocation.setLongitude(18.111798);
    }

    @Override
    public void onStop() {
        super.onStop();
        readingsHandler.removeCallbacks(updateUIReadings);
    }

    public void startGPS() {
        Intent startGPSintent = new Intent(backgroundService);
        startGPSintent.putExtra(BackgroundService.INTENT_KEY_GPS_ON, true);
        startService(startGPSintent);
    }

    public void stopGPS() {
        Intent stopGPSintent = new Intent(backgroundService);
        stopGPSintent.putExtra(BackgroundService.INTENT_KEY_GPS_ON, false);
        startService(stopGPSintent);
    }
}