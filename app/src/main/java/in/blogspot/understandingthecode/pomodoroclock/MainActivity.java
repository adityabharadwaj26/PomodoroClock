package in.blogspot.understandingthecode.pomodoroclock;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int maxTime = 1200000;
    boolean pause = false;
    boolean maxTimeset = false;
    Toolbar mActionBarToolbar;
    private ProgressBar progress;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private Ringtone ring;
    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = ((int) (updatedTime / 1000));
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            progress.setMax(maxTime);
            progress.setProgress((int) updatedTime);
            customHandler.postDelayed(this, 0);
            if (updatedTime == maxTime) {
                maxTimeset = true;
                playTimer();
                //stopTimer();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //final CountDown timer = new CountDown();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle("Pomodoro");
        setSupportActionBar(mActionBarToolbar);
*/
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        timerValue = (TextView) findViewById(R.id.timerVal);
        final Button startButton = (Button) findViewById(R.id.startButton);
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
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;
                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                int milliseconds = (int) (updatedTime % 1000);
                timerValue.setText(mins + ":"
                        + String.format("%02d", secs) + ":"
                        + String.format("%03d", milliseconds));
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

    public void playTimer() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ring = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ring.play();
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        Toast.makeText(MainActivity.this, "20 Minutes are over, Press reset to stop the alarm.",
                Toast.LENGTH_LONG).show();
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
}