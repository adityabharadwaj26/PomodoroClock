package in.blogspot.understandingthecode.pomodoroclock;

//Imports
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    //Variable Declarations
    boolean pause = false;
    boolean maxTimeset = false;
    private ProgressBar progress;
    private Ringtone ring;
    long timeWhenStopped = 0;
    long timeWhenStarted = 0;
    long maxMinutes = 20;
    long maxSeconds = 1200;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    //onCreate Method
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedpreferences = getPreferences(MODE_PRIVATE);
        boolean userFirstLogin = sharedpreferences.getBoolean("NoShow", true);

        if (userFirstLogin) {
            Intent intent = new Intent(this, Intro.class);
            startActivity(intent);
            sharedpreferences.edit().putBoolean("NoShow", false).apply();
        }
        PowerManager mgr = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if (mAdView != null) {
            mAdView.loadAd(adRequest);
        }
        progress = (ProgressBar) findViewById(R.id.progressBar);
        assert progress != null;
        progress.setMax((int) maxSeconds);
        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);

        final Button startButton = (Button) findViewById(R.id.startButton);
        assert startButton != null;
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!pause) {
                    assert chronometer != null;
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    timeWhenStarted = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.start();
                    startButton.setText(R.string.Pause);
                    pause = true;
                    Toast.makeText(MainActivity.this, "Pomodoro Clock has started, Alarm will play after 20 Minutes. Get to Work Now!!",
                          Toast.LENGTH_SHORT).show();
                } else {
                    assert chronometer != null;
                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                    startButton.setText(R.string.Start);
                    pause = false;
                    Toast.makeText(MainActivity.this, "Pomodoro Clock has paused",
                          Toast.LENGTH_SHORT).show();
                }
            }
        });
        assert chronometer != null;
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick (Chronometer chronometer){
                long minutes =((SystemClock.elapsedRealtime()- chronometer.getBase())/1000)/60;
                //long seconds =((SystemClock.elapsedRealtime()- chronometer.getBase())/1000)%60;
                long totalMilliseconds =((SystemClock.elapsedRealtime()- chronometer.getBase())/1000);
                progress.setProgress((int) totalMilliseconds);
                if (minutes == maxMinutes){
                    //Toast.makeText(MainActivity.this, "Yes it works", Toast.LENGTH_SHORT).show();
                    maxTimeset = true;
                    progress.setProgress((int) totalMilliseconds);
                    playTimer();
                    chronometer.setFormat(null);
                    chronometer.stop();
                }
            }
        });
        Button resetButton = (Button) findViewById(R.id.resetButton);
        assert resetButton != null;
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                timeWhenStopped = 0;
                chronometer.setFormat(null);
                chronometer.stop();
                progress.setProgress(0);
                startButton.setText(R.string.Start);
                pause = false;
                Toast.makeText(MainActivity.this, "Pomodoro Clock has stopped",
                      Toast.LENGTH_SHORT).show();
                if (maxTimeset) {
                    stopTimer();
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //Play Timer method
    public void playTimer() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ring = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ring.play();
        Toast.makeText(MainActivity.this, "20 Minutes are over, Press reset to stop the alarm.",
              Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);//Menu Resource, Menu
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(MainActivity.this, About_Me.class);
                MainActivity.this.startActivity(intent);
                //Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void stopTimer() {
        ring.stop();
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://in.blogspot.understandingthecode.pomodoroclock/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://in.blogspot.understandingthecode.pomodoroclock/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Press Back again to Exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}