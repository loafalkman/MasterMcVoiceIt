package se.su.dsv.mastermcvoiceit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.command.Command;
import se.su.dsv.mastermcvoiceit.command.TempCommand;
import se.su.dsv.mastermcvoiceit.gps.LocationService;
import se.su.dsv.mastermcvoiceit.sensor.TelldusSensor;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    private static final String TAG = "main";
    public static final String LOCATION_UPDATE = "location update";
    public static final String LOCATION = "location";
    static final int RESULT_SPEECH = 7474;

    SpeechRecognizer speechRecognizer;
    Intent recognizerIntent;
    Intent locationService;
    private BroadcastReceiver broadcastReceiver;

    ArrayList<String> resultArray;
    String resultString;
    Location homeLocation; // TEMP

    FrameLayout tmpContainer;
    FrameLayout locContainer;
    View tempSkeleton;
    View locSkeleton;
    Switch simpleSwitch;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createHomeLocation();
        locationService = new Intent(this, LocationService.class);

        initializeTempContainer();
        initializeLocationsContainer();

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        initCommands();
    }

    public void initializeTempContainer() {
        tmpContainer = (FrameLayout) findViewById(R.id.framelayout_main_tmpcommandcontainer);
        tempSkeleton = getLayoutInflater().inflate(R.layout.item_commandhistory_temp, null);
        tmpContainer.addView(tempSkeleton);
    }

    public void initializeLocationsContainer() {
        locContainer = (FrameLayout) findViewById(R.id.framelayout_main_locationservice);
        locSkeleton = getLayoutInflater().inflate(R.layout.container_location_services, null);
        locContainer.addView(locSkeleton);

        simpleSwitch = (Switch) findViewById(R.id.location_switch);
        simpleSwitch.setChecked(true);
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(locationService);
                } else {
                    stopService(locationService);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (broadcastReceiver == null) {
            broadcastReceiver = new LocationBroadcastReceiver();
        }

        registerReceiver(broadcastReceiver, new IntentFilter(LOCATION_UPDATE));
        startService(locationService);
    }

    private void createHomeLocation() {
        homeLocation = new Location("");
        homeLocation.setLatitude(59.345613);
        homeLocation.setLongitude(18.111798);
    }

    private void initCommands() {
        new TempCommand(new TelldusSensor(2));
    }

    public void voiceInput(View v) {
        if (SpeechRecognizer.isRecognitionAvailable(MainActivity.this)) {
            speechRecognizer.startListening(recognizerIntent);
        }
    }

    public void voiceResult(View v) {
//        resultString = "Sensor 2";

        if (resultString != null) {
            Command foundCommand = Command.findCommand(resultString);

            if (foundCommand != null) {
                Toast.makeText(this, "Command: " + resultString, Toast.LENGTH_SHORT).show();
                Bundle bundle = foundCommand.doCommand(resultString);

                renderCard(bundle);

            } else {
                Toast.makeText(this, "Couldn't find command: " + resultString, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void renderCard(Bundle bundle) {
        int flag = bundle.getInt("flag");

        switch (flag) {
            case Command.FLAG_TEMP:
                float temp = bundle.getFloat("Current temperature");

                TextView tempDesc = tempSkeleton.findViewById(R.id.textview_tempitem_description);
                tempDesc.setText("Temperaturen är " + temp + " C*");
                
                break;
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
            resultString = matches.get(0).toLowerCase();
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
            if (location.distanceTo(homeLocation) < 2000) {
                Toast.makeText(MainActivity.this, "Near!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Not Near!", Toast.LENGTH_SHORT).show();

            }
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
}