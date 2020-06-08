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

public class BlueHouseActivity extends AppCompatActivity {

    private static String gateIP = "192.168.1.13";
    private boolean openDoor = false;
    private boolean gateStatus = false;
    private boolean openAir = false;
    private boolean airStatus = false;

    private String data[];

    private Switch door;
    private Switch airCond;
    private Switch isInside;
    private Switch isComing;
    private Switch thief;
    private TextView sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_house);
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
                    collapsingToolbarLayout.setTitle("Blue House");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        sensor = (TextView) findViewById(R.id.sensor);
        door = (Switch) findViewById(R.id.blue_door);
        airCond = (Switch) findViewById(R.id.air_cond);
        isInside = (Switch) findViewById(R.id.is_inside);
        isComing = (Switch) findViewById(R.id.is_coming);
        thief = (Switch) findViewById(R.id.thief);

        door.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                gateStatus = true;
                openDoor = b;
            }
        });


        airCond.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                airStatus = true;
                openAir = b;
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (gateStatus) {
                            if (openDoor) {
                                new OkHttpClient().newCall(new Request.Builder().url("http://" + gateIP + "/openDoor").build()).execute().body().string();
                            } else {
                                new OkHttpClient().newCall(new Request.Builder().url("http://" + gateIP + "/closeDoor").build()).execute().body().string();
                            }
                            gateStatus = false;
                        }
                        if (airStatus) {
                            if (openAir) {
                                new OkHttpClient().newCall(new Request.Builder().url("http://" + gateIP + "/openAir").build()).execute().body().string();
                            } else {
                                new OkHttpClient().newCall(new Request.Builder().url("http://" + gateIP + "/closeAir").build()).execute().body().string();
                            }
                            airStatus = false;
                        }
                        data = new OkHttpClient().newCall(new Request.Builder().url("http://" + gateIP).build()).execute().body().string().split(",");
                        BlueHouseActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data[0].equals("ON")) {
                                    door.setChecked(true);
                                } else if (data[0].equals("OFF")) {
                                    door.setChecked(false);
                                }

                                if (data[1].equals("ON")) {
                                    airCond.setChecked(true);
                                } else if (data[1].equals("OFF")) {
                                    airCond.setChecked(false);
                                }

                                if (data[2].equals("ON")) {
                                    isInside.setChecked(true);
                                } else if (data[2].equals("OFF")) {
                                    isInside.setChecked(false);
                                }

                                if (data[3].equals("ON")) {
                                    isComing.setChecked(true);
                                } else if (data[3].equals("OFF")) {
                                    isComing.setChecked(false);
                                }

                                if (data[4].equals("ON")) {
                                    thief.setChecked(true);
                                } else if (data[4].equals("OFF")) {
                                    thief.setChecked(false);
                                }

                                sensor.setText(data[5]);
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
