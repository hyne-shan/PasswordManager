package com.example.shanj.passwordmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by shanj on 2015/12/27.
 */
public class AddPassword extends AppCompatActivity {

    private MyAutoCompleteTextView information;
    private MailAutoCompleteTextView addaccount;
    private EditText addpassword;
    private Button addbtn;
    private Button setting;
    private SQLiteDatabase db;
    String informations[] = new String[]{"Email", "Outlook", "AppleID", "gmail","QQ", "微信", "微博", "AppleID", "gmail", "邮箱", "支付宝", "淘宝", "中国移动", "中国联通", "贴吧"};
    private HomeWatcher homeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.addpassword);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/mypassword.db", null);
        information = (MyAutoCompleteTextView) findViewById(R.id.information);
        addaccount = (MailAutoCompleteTextView) findViewById(R.id.accountinformation);
        addpassword = (EditText) findViewById(R.id.passwordinformation);
        addbtn = (Button) findViewById(R.id.addbtn);

        //信息补充列表
        ArrayAdapter<String> infoAdapter = new ArrayAdapter<String>(this, R.layout.online, informations);
        information.setAdapter(infoAdapter);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = information.getText().toString().trim();
                String account = addaccount.getText().toString().trim();
                String password = addpassword.getText().toString().trim();
                if (info.equals("")) {
                    Toast.makeText(AddPassword.this, "添加失败 请填写完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (account.equals("")) {
                    Toast.makeText(AddPassword.this, "添加失败 请填写完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(AddPassword.this, "添加失败 请填写完整", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    insertData(db, info, account, password);
                    Toast.makeText(AddPassword.this, "添加成功", Toast.LENGTH_SHORT).show();

                } catch (SQLiteException se) {
                    //如果第一次运行，没有创建表，执行异常，创建数据表
                    db.execSQL("CREATE TABLE Mypassword(" +
                            "_id integer primary key autoincrement, " +
                            "info TEXT, " +
                            "account TEXT, " +
                            "password TEXT)");
                    // 执行insert语句插入数据
                    insertData(db, info, account, password);
                    Toast.makeText(AddPassword.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
            }
        });



        setting = (Button) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPassword.this, SettingActivity.class);
                startActivity(intent);

            }
        });



        //home监听
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

    private void insertData(SQLiteDatabase db, String info, String account, String password) {
        db.execSQL("insert into Mypassword values(null,?,?,?)", new String[]{info, account, password});
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Tag", "4onpause");
        // 在onPause中停止监听，不然会报错的。
        homeWatcher.stopWatch();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Tag", "4onrestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Tag", "4onresume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Tag", "4onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Tag", "4onDestroy");
    }
}
