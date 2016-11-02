package com.victor.customtogglebutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.victor.togglebuttonlib.CustomToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomToggleButton toggleButton = (CustomToggleButton) findViewById(R.id.ctb_button);

        //toggleButton.setSwitchBackgroundResource(R.mipmap.switch_background);
        //toggleButton.setSlideButtonResource(R.mipmap.ic_launcher);
        //toggleButton.setSwitchState(true);
        toggleButton.setOnStateChangeListener(new CustomToggleButton.OnStateChangedListener() {

            @Override
            public void onStateChanged(boolean state) {
                Toast.makeText(MainActivity.this, state ? "toggle is opened" : "toggle is closed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
