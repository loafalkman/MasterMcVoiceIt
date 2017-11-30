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
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import se.su.dsv.mastermcvoiceit.cardViews.CardFragment;
import se.su.dsv.mastermcvoiceit.command.Command;
import se.su.dsv.mastermcvoiceit.command.TempCommand;
import se.su.dsv.mastermcvoiceit.gps.LocationService;
import se.su.dsv.mastermcvoiceit.mainCards.CardInfo;
import se.su.dsv.mastermcvoiceit.mainCards.CardInfoType;
import se.su.dsv.mastermcvoiceit.mainCards.LocationCardInfo;
import se.su.dsv.mastermcvoiceit.mainCards.TempCardInfo;
import se.su.dsv.mastermcvoiceit.sensor.TelldusSensor;

public class MainActivity extends AppCompatActivity implements RecognitionListener, CardFragment.GPSController {

    private static final String TAG = "main";
    public static final String LOCATION_UPDATE = "location update";
    public static final String LOCATION = "location";
    static final int RESULT_SPEECH = 7474;

    SpeechRecognizer speechRecognizer;
    Intent recognizerIntent;
    Intent locationService;
    BroadcastReceiver broadcastReceiver;

    TempCommand tempCommand;

    ArrayList<String> resultArray;
    String voiceResultStr;
    Location homeLocation; // TEMP

    ArrayList<CardInfo> cardModels = new ArrayList<>();
    HashMap<Command, CardInfo> cardOnCommand = new HashMap<>();

    LocationCardInfo locationCardInfo;

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

        initCommands();
        populateCards();
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

        renderAllCards();
    }

    private void launchFragment(CardFragment cardFragment) {
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_main_cardsfragment, cardFragment);
        fragmentTransaction.commit();
    }

    private void renderAllCards() {
        for (CardInfo card : cardModels) {
            renderOneCard(card);
        }
    }

    private void renderOneCard(CardInfo cardInfo) {
        CardInfoType type = cardInfo.getItemViewType();
        switch (type) {
            case TEMPERATURE:
                cardFragment.renderTemperature(((TempCardInfo) cardInfo));
                break;

            case LOCATION:
                cardFragment.renderLocation(((LocationCardInfo) cardInfo));
                break;
        }
    }

    private void createHomeLocation() {
        homeLocation = new Location("");
        homeLocation.setLatitude(59.345613);
        homeLocation.setLongitude(18.111798);
    }

    private void initCommands() {
        tempCommand = new TempCommand(new TelldusSensor(2));
    }

    private void populateCards() {
        TempCardInfo tempInfo = new TempCardInfo(23.4f);
        cardModels.add(tempInfo);
        cardOnCommand.put(tempCommand, tempInfo);

        locationCardInfo = new LocationCardInfo();
        cardModels.add(locationCardInfo);

    }

    public void voiceInput(View v) {
        if (SpeechRecognizer.isRecognitionAvailable(MainActivity.this)) {
            speechRecognizer.startListening(recognizerIntent);
        }
    }

    public void updateInfo(View view) {
        voiceResultStr = "sensor 2";

        if (voiceResultStr != null) {
            Command foundCommand = Command.findCommand(voiceResultStr);
            TempCardInfo tempCard = (TempCardInfo) cardOnCommand.get(foundCommand);

            // Voila
            tempCard.setTemperature(666.666f);
            cardFragment.renderTemperature(tempCard);
        }
    }

    public void voiceResult(View v) {
        voiceResultStr = "sensor 2";

        if (voiceResultStr != null) {
            Command foundCommand = Command.findCommand(voiceResultStr);

            if (foundCommand != null) {
                Toast.makeText(this, "Command: " + voiceResultStr, Toast.LENGTH_SHORT).show();

                TempCardInfo tempCard = (TempCardInfo) cardOnCommand.get(foundCommand);
                foundCommand.doCommand(voiceResultStr, tempCard);
                renderOneCard(tempCard);

            } else {
                Toast.makeText(this, "Couldn't find command: " + voiceResultStr, Toast.LENGTH_LONG).show();
            }
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

    /**
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

            locationCardInfo.setDistanceFromHome(distanceToHome);
            renderOneCard(locationCardInfo);
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