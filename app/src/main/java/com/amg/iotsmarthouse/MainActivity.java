package com.amg.iotsmarthouse;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton redHouse;
    private ImageButton blueHouse;
    private ImageButton gate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redHouse = (ImageButton) findViewById(R.id.redhouse);
        blueHouse = (ImageButton) findViewById(R.id.bluehouse);
        gate = (ImageButton) findViewById(R.id.gate);

        redHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RedHouseActivity.class),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, redHouse,
                                ViewCompat.getTransitionName(redHouse)).toBundle());
            }
        });
        blueHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BlueHouseActivity.class),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, blueHouse,
                                ViewCompat.getTransitionName(blueHouse)).toBundle());
            }
        });
        gate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GateActivity.class),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, gate,
                                ViewCompat.getTransitionName(gate)).toBundle());
            }
        });
    }
}
