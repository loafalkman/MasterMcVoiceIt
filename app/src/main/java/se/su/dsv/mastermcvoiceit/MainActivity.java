package se.su.dsv.mastermcvoiceit;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.command.Command;
import se.su.dsv.mastermcvoiceit.command.TempCommand;
import se.su.dsv.mastermcvoiceit.sensor.TelldusSensor;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    private static final String TAG = "main";
    SpeechRecognizer speechRecognizer;
    Intent recognizerIntent;
    static final int RESULT_SPEECH = 7474;

    ArrayList<String> resultArray;
    String resultString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        initCommands();
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
        if (resultString != null) {
            Command foundCommand = Command.findCommand(resultString);

            if (foundCommand != null) {
                Toast.makeText(this, "Command: " + resultString, Toast.LENGTH_SHORT).show();

                View commandView = foundCommand.doCommand(this, resultString);
                FrameLayout tmpContainer = (FrameLayout) findViewById(R.id.framelayout_main_tmpcommandcontainer);
                tmpContainer.addView(commandView);
            } else {
                Toast.makeText(this, "Couldn't find command: " + resultString, Toast.LENGTH_LONG).show();
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
//        Called after the user stops speaking.

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {
        // Called when recognition results are ready.

        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d(TAG, "onResults: ----> " + matches.get(0));
        if (matches != null && matches.size() > 0) {
            resultString = matches.get(0);
//            Toast.makeText(MainActivity.this, (matches.get(0)), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

}