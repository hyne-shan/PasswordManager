package com.example.shanj.passwordmanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class SettingActivity extends Activity {

    List<Integer> passList;
    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    HomeWatcher homeWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final GestureLock lock = (GestureLock) findViewById(R.id.LockView);
        Button btn_reset = (Button) findViewById(R.id.btn_reset);
        Button btn_save = (Button) findViewById(R.id.btn_save);

       sp = SettingActivity.this.getSharedPreferences("password", SettingActivity.this.MODE_PRIVATE);
       editor = sp.edit();

        lock.setOnDrawFinishedListener(new GestureLock.OnDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                if (passList.size() < 3) {
                    Toast.makeText(SettingActivity.this, "密码不能少于3个点", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    SettingActivity.this.passList = passList;
                    return true;
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock.resetPoints();
                editor.putString("password", "");
                editor.commit();
                Toast.makeText(SettingActivity.this, "密码清除成功", Toast.LENGTH_SHORT).show();

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passList != null) {
                    StringBuilder sb = new StringBuilder();
                    for (Integer i : passList) {
                        sb.append(i);
                    }
                    editor.putString("password", sb.toString());
                    editor.commit();

                    Toast.makeText(SettingActivity.this, "保存完成", Toast.LENGTH_SHORT).show();
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
        homeWatcher.stopWatch();
        Log.i("Tag", "3onpause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Tag", "3onrestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Tag", "3onresume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Tag", "3onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Tag", "3onDestroy");
    }
}
