package com.amg.iotsmarthouse;

import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GateActivity extends AppCompatActivity {

    private static String gateIP = "192.168.1.13";
    private boolean openGate = false;
    private boolean gateStatus = false;
    private String data[];
    private TextView onDoor;
    private Switch gateDoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ((AppBarLayout) findViewById(R.id.appbar)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Gate");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        onDoor = (TextView) findViewById(R.id.on_door);
        gateDoor = (Switch) findViewById(R.id.gate_door);
        gateDoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gateStatus = true;
                openGate = b;
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (gateStatus) {
                            if (openGate) {
                                new OkHttpClient().newCall(new Request.Builder().url("http://" + gateIP + "/openGate").build()).execute().body().string();
                            } else {
                                new OkHttpClient().newCall(new Request.Builder().url("http://" + gateIP + "/closeGate").build()).execute().body().string();
                            }
                            gateStatus = false;
                        }
                        data = new OkHttpClient().newCall(new Request.Builder().url("http://" + gateIP).build()).execute().body().string().split(",");
                        GateActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data[0].equals("ON")) {
                                    gateDoor.setChecked(true);
                                } else if (data[0].equals("OFF")) {
                                    gateDoor.setChecked(false);
                                }
                                onDoor.setText(data[1]);
                            }
                        });
                        Thread.sleep(500);
                    } catch (IOException | InterruptedException e) {
                        Log.e("error", e.toString());
                    }
                }
            }
        });
    }
}
