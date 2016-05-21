package in.blogspot.understandingthecode.pomodoroclock;

//Imports
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    //Variable Declarations
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int maxTime = 1200000;
    boolean pause = false;
    boolean maxTimeset = false;
    private ProgressBar progress;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private Ringtone ring;

    //Runnable Thread
    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = ((int) (updatedTime / 1000));
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            String timer = ("" + String.format("%02d",mins) + ":" + String.format("%02d", secs));
            timerValue.setText(timer) ;
            progress.setMax(maxTime);
            progress.setProgress((int) updatedTime);
            customHandler.postDelayed(this, 0);
            if (updatedTime == maxTime) {
                maxTimeset = true;
                playTimer();
            }
        }
    };


    //onCreate Method
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedpreferences = getPreferences(MODE_PRIVATE);
        //boolean NoShow = sharedpreferences.getBoolean("NoShow",false);

        boolean userFirstLogin = sharedpreferences.getBoolean("NoShow", true);

        if (userFirstLogin) {
            Intent intent = new Intent(this, Intro.class);
            startActivity(intent);
            sharedpreferences.edit().putBoolean("NoShow", true).commit();
            //editor.putBoolean("NoShow", true);
            //editor.apply();
        }
        PowerManager mgr = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if (mAdView != null) {
            mAdView.loadAd(adRequest);
        }

        timerValue = (TextView) findViewById(R.id.timerVal);
        final Button startButton = (Button) findViewById(R.id.startButton);
        assert startButton != null;
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!pause) {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    startButton.setText(R.string.Pause);
                    pause = true;
                    Toast.makeText(MainActivity.this, "Pomodoro Clock has started, Alarm will play after 20 Minutes. Get to Work Now!!",
                            Toast.LENGTH_LONG).show();
                } else {
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                    startButton.setText(R.string.Start);
                    pause = false;
                    Toast.makeText(MainActivity.this, "Pomodoro Clock has paused",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        Button resetButton = (Button) findViewById(R.id.resetButton);
        assert resetButton != null;
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;
                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                int milliseconds = (int) (updatedTime % 1000);
                String timer1 = ("" + String.format("%02d",mins) + ":" + String.format("%02d", secs));
                timerValue.setText(timer1);
                progress.setProgress((int) updatedTime);
                startButton.setText(R.string.Start);
                pause = false;
                customHandler.removeCallbacks(updateTimerThread);
                Toast.makeText(MainActivity.this, "Pomodoro Clock has stopped",
                        Toast.LENGTH_LONG).show();
                if (maxTimeset) {
                    stopTimer();
                }
            }
        });
        progress = (ProgressBar) findViewById(R.id.progressBar);
    }

    //Play Timer method
    public void playTimer() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ring = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ring.play();
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        Toast.makeText(MainActivity.this, "20 Minutes are over, Press reset to stop the alarm.",
                Toast.LENGTH_LONG).show();
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
            /*case R.id.item2:
                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.item3:
                Toast.makeText(getApplicationContext(),"Item 3 Selected",Toast.LENGTH_LONG).show();
                return true;*/
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    public void onDestroy() {
        super.onDestroy();
    }
}