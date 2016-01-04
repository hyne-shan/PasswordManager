package com.example.shanj.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class SettingActivity2 extends Activity {

    List<Integer> passList;

    HomeWatcher homeWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        final GestureLock lock = (GestureLock) findViewById(R.id.LockView);
        Button btn_reset = (Button) findViewById(R.id.btn_reset);
        Button btn_save = (Button) findViewById(R.id.btn_save);

        lock.setOnDrawFinishedListener(new GestureLock.OnDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                if (passList.size() < 3) {
                    Toast.makeText(SettingActivity2.this, "密码不能少于3个点", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    SettingActivity2.this.passList = passList;
                    if (passList != null) {
                        StringBuilder sb = new StringBuilder();
                        for (Integer i : passList) {
                            sb.append(i);
                        }
                        SharedPreferences sp = SettingActivity2.this.getSharedPreferences("password", SettingActivity2.this.MODE_PRIVATE);
                        if (sb.toString().equals(sp.getString("password", null))) {
                            Constans.isFlag = true;
                            startActivity(new Intent(SettingActivity2.this, PasswordList.class));
                            finish();
                        } else {
                            Toast.makeText(SettingActivity2.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }
            }
        });


        homeWatcher = new HomeWatcher(this);
        homeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                System.exit(0);
            }

            @Override
            public void onHomeLongPressed() {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Tag","2onpause");
        // 在onPause中停止监听，不然会报错的。
        homeWatcher.stopWatch();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Tag", "2onrestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Tag", "2onresume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Tag", "2onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Tag", "2onDestroy");
    }
}
