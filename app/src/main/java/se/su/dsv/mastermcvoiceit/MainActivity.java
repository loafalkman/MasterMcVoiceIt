package se.su.dsv.mastermcvoiceit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
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
import se.su.dsv.mastermcvoiceit.command.Command;
import se.su.dsv.mastermcvoiceit.command.TempCommand;
import se.su.dsv.mastermcvoiceit.gps.LocationService;
import se.su.dsv.mastermcvoiceit.cardModels.CardModel;
import se.su.dsv.mastermcvoiceit.cardModels.CardModelType;
import se.su.dsv.mastermcvoiceit.cardModels.LocationCardModel;
import se.su.dsv.mastermcvoiceit.cardModels.TempsCardModel;
import se.su.dsv.mastermcvoiceit.remote.sensor.Sensor;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorType;
import se.su.dsv.mastermcvoiceit.remote.sensor.TelldusSensor;

public class MainActivity extends AppCompatActivity implements RecognitionListener, CardFragment.GPSController {

    private static final String TAG = "main";
    public static final String LOCATION_UPDATE = "location update";
    public static final String LOCATION = "location";
    static final int RESULT_SPEECH = 7474;

    SpeechRecognizer speechRecognizer;
    Intent recognizerIntent;
    Intent locationService;
    BroadcastReceiver broadcastReceiver;

    ArrayList<String> resultArray;
    String voiceResultStr;
    Location homeLocation; // TEMP

    FragmentManager fragmentManager;
    CardFragment cardFragment;

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

        cardFragment = new CardFragment();
        launchFragment(cardFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        locationService = new Intent(this, LocationService.class);

        if (broadcastReceiver == null) {
            broadcastReceiver = new LocationBroadcastReceiver();
        }

        registerReceiver(broadcastReceiver, new IntentFilter(LOCATION_UPDATE));
        startService(locationService);
    }

    private void launchFragment(CardFragment cardFragment) {
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_main_cardsfragment, cardFragment);
        fragmentTransaction.commit();
    }


    /**
     * temporary listener for a temporary button :P TODO: call from a Thread instead.
     */
    public void updateCardModelListener(View view) {
        cardFragment.updateCardModels();
        cardFragment.renderAllCards();
    }

    /**
     * Temporarily bound to a button for testing, should be activated after voice result.
     */
    public void voiceResult(View v) {
        voiceResultStr = "sensor 2";

        if (voiceResultStr != null) {
            // TODO: implement method in fragment to execute and render command?
            // idea: use the place name as prefix in command if you want to execute a command for another place that's not the visible one.
            // for example, say the command: "home, turn off all lights" when in the fragment for country house.
            // commands with such prefix launches the corresponding place fragment and runs the command within it.

//            Command foundCommand = Command.findCommand(voiceResultStr);
//
//            if (foundCommand != null) {
//                Toast.makeText(this, "Command: " + voiceResultStr, Toast.LENGTH_SHORT).show();
//
//            } else {
//                Toast.makeText(this, "Couldn't find command: " + voiceResultStr, Toast.LENGTH_LONG).show();
//            }
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
    public void onReadyForSpeech(Bundle bundle) {}

    @Override
    public void onBeginningOfSpeech() {}

    @Override
    public void onRmsChanged(float v) {}

    @Override
    public void onBufferReceived(byte[] bytes) {}

    @Override
    public void onEndOfSpeech() {}

    @Override
    public void onError(int i) {}

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if (matches != null && matches.size() > 0) {
            voiceResultStr = matches.get(0).toLowerCase();
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {}

    @Override
    public void onEvent(int i, Bundle bundle) {}

    private void createHomeLocation() {
        homeLocation = new Location("");
        homeLocation.setLatitude(59.345613);
        homeLocation.setLongitude(18.111798);
    }

    /** TODO: remove this, activity won't need the location
     * Custom BroadCastReceiver for receiving messages from CurrentLocationService.
     */
    public class LocationBroadcastReceiver extends BroadcastReceiver {

        /**
         * Called when the receiver receives the intent.
         * Determines if the current location is near the "home" location.
         * If true, some action will be fired.
         * @param context the context in which the receiver is running.
         * @param intent intent containing the current location.
         */
        @Override // when the receiver receives the intent
        public void onReceive(Context context, Intent intent) {

            Location location = intent.getParcelableExtra(LOCATION);
            float distanceToHome = location.distanceTo(homeLocation);
            if (distanceToHome < 2000) {
                Toast.makeText(MainActivity.this, "Near!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Not Near!", Toast.LENGTH_SHORT).show();
            }

//            locationCardModel.setDistanceFromHome(distanceToHome);
//            renderCard(locationCardModel);
        }
    }

    /**
     * The final method in the Activity lifecycle. If the app is stopped
     * the Reminders has to be saved and the BroadCastReceiver need to disconnect.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // save data?

        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    public void startService() {
        startService(locationService);
    }

    public void stopService() {
        stopService(locationService);
    }
}