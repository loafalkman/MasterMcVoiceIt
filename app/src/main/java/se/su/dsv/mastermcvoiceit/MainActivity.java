package se.su.dsv.mastermcvoiceit;
import android.content.Context;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void voiceInput(View v) {

        String input = ""; // let the voice recognizer dance!


        Toast.makeText(this, "You said: " + input, Toast.LENGTH_SHORT).show();
    }
}