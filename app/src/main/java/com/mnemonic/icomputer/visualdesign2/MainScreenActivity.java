package com.mnemonic.icomputer.visualdesign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainScreenActivity extends AppCompatActivity {

    public final static int RC_EVENT_CHOOSED = 1;
    public final static int RC_GUEST_CHOOSED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Intent intent = getIntent();
        String personName = intent.getStringExtra(HomeActivity.EXTRA_PERSON_NAME);

        TextView textView = (TextView) findViewById(R.id.person_name);
        textView.setText(personName);
    }

    public void chooseEvent(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        startActivityForResult(intent, RC_EVENT_CHOOSED);
    }

    public void chooseGuest(View view) {
        Intent intent = new Intent(this, GuestActivity.class);
        startActivityForResult(intent, RC_GUEST_CHOOSED);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_EVENT_CHOOSED) {
            if(resultCode == RESULT_OK){
                String eventName = data.getStringExtra(EventActivity.EXTRA_CHOOSED_EVENT_NAME);
                Button btn = (Button) findViewById(R.id.button_choose_event);
                btn.setText(eventName);
            }
        }
        else if (requestCode == RC_GUEST_CHOOSED) {
            if(resultCode == RESULT_OK){
                String guestName = data.getStringExtra(GuestActivity.EXTRA_CHOOSED_GUEST_NAME);
                Button btn = (Button) findViewById(R.id.button_choose_guest);
                btn.setText(guestName);
            }
        }
    }
}
