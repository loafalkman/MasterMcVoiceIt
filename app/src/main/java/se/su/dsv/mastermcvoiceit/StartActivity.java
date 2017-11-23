package se.su.dsv.mastermcvoiceit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1;

    /**
     * If API level is 23 or higher, prompt the user for permission before
     * launching the app.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    /**
     * To avoid the user to deny permission, restart the app and access AppActivity
     * anyway, permission is checked inside onStart.
     */
    @Override
    public void onStart() {
        super.onStart();
        checkPermissionStatus();
    }

    /**
     * If API level >= 23 location permission is necessary.
     */
    private void checkPermissionStatus() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            locationPermission();
        } else {
            launchAppActivity();
            finish();
        }
    }

    /**
     * Launches the app.
     */
    private void launchAppActivity() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Asks for permission
     */
    private void locationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_PERMISSION_ACCESS_FINE_LOCATION);

        } else {
            launchAppActivity();
        }
    }

    /**
     * Handes the permission result.
     * @param requestCode who sent this.
     * @param permissions permissions being asked.
     * @param grantResults the results.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case REQUEST_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    launchAppActivity();
                    finish();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showAlertDialog();
                    break;
                }
            }
        }
    }

    /**
     * Pops an AlertDialog where the user is encouraged to grant permission for location.
     * If user press try again the permission dialog will pop up again. If the user press
     * close app, the app will finish.
     */
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle(R.string.reasons_for_permission);
        builder.setMessage(R.string.reasons_for_permission);
        builder.setCancelable(false);

        builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                locationPermission();
            }
        });

        builder.setNegativeButton("CLOSE APP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });

        builder.show();
    }
}
