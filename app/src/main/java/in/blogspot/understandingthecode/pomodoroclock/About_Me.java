package in.blogspot.understandingthecode.pomodoroclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About_Me extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__me);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        //String value = intent.getStringExtra("key"); //if it's a string you stored.
        TextView link = (TextView) findViewById(R.id.textView);
        link.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
