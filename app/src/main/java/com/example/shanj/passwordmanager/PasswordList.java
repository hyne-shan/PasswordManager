package com.example.shanj.passwordmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by shanj on 2015/12/27.
 */
public class PasswordList extends AppCompatActivity {

    private SQLiteDatabase db;
    private FloatingActionButton fat;
    private ListView infoList;
    private EditText edtype;
    private EditText edpwd;

    private TextView accounttv;
    private TextView passwordtv;
    SimpleCursorAdapter adapter;

    HomeWatcher homeWatcher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PasswordList.this.getSharedPreferences("password", PasswordList.this.MODE_PRIVATE);
        String pwd = sp.getString("password", null);
        if (!Constans.isFlag) {
            if (pwd != null && !pwd.equals("")) {
                startActivity(new Intent(PasswordList.this, SettingActivity2.class));
                finish();
            }


        }
        setTitle("");
        setContentView(R.layout.list);
        infoList = (ListView) findViewById(R.id.list);
        fat = (FloatingActionButton) findViewById(R.id.fab);
        fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordList.this, AddPassword.class);
                startActivity(intent);
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

        homeWatcher.startWatch();

        //        infoList = (ListView) findViewById(R.id.list);
        //        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()+"/mypassword.db",null);
        //        Cursor cursor = db.rawQuery("select * from Mypassword",null);
        //        SimpleCursorAdapter adapter = new SimpleCursorAdapter(PasswordList.this,R.layout.listview_item,cursor,new String[]{"info"},new int[]{R.id.name});
        //        infoList.setAdapter(adapter);
        //


    }

    @Override
    protected void onResume() {
        super.onResume();
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/mypassword.db", null);
        try {
            Cursor cursor = db.rawQuery("select * from Mypassword", null);
            adapter = new SimpleCursorAdapter(PasswordList.this, R.layout.listview_item, cursor, new String[]{"info", "_id"}, new int[]{R.id.name});
            infoList.setAdapter(adapter);
        } catch (SQLiteException se) {
            db.execSQL("CREATE TABLE Mypassword(" +
                    "_id integer primary key autoincrement, " +
                    "info TEXT, " +
                    "account TEXT, " +
                    "password TEXT)");
        }


        infoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Toast.makeText(PasswordList.this, "您记录的账号密码", Toast.LENGTH_SHORT).show();
                                                //查询数据
                                                Cursor cursor = db.rawQuery("select * from Mypassword", null);
                                                cursor.moveToPosition(position);
                                                String account = cursor.getString(cursor.getColumnIndex("account"));
                                                String pwd = cursor.getString(cursor.getColumnIndex("password"));
                                                Log.e("Tag", account + "-->" + pwd);
                                                cursor.close();

                                                String[] items = {account, pwd};

                                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordList.this);
                                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                                builder.create();
                                                builder.show();


                                                //                seachData(itemId);
                                                //                if (cursor.moveToFirst())
                                                //                {
                                                //                    String account = cursor.getString(cursor.getColumnIndex("account"));
                                                //                    String pwd = cursor.getString(cursor.getColumnIndex("password"));
                                                //                    Log.e("Tag", account + "-->" + pwd);
                                                //                }
                                            }
                                        }

        );


        infoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()

                                            {
                                                @Override
                                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,
                                                                               final long id) {
                                                    final String[] del_update = {"修改", "删除"};
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordList.this);
                                                    builder.setItems(del_update, new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            switch (which) {
                                                                case 0:
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordList.this);
                                                                    LayoutInflater inflater = getLayoutInflater();
                                                                    inflater.inflate(R.layout.dialog_edit, null);
                                                                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_edit, null);
                                                                    builder.setView(layout);
                                                                    edtype = (EditText) layout.findViewById(R.id.username);
                                                                    edpwd = (EditText) layout.findViewById(R.id.password);
                                                                    //查询数据并把数据提取到对话框中
                                                                    Cursor c = db.rawQuery("select * from Mypassword", null);
                                                                    c.moveToPosition(position);
                                                                    String account = c.getString(c.getColumnIndex("account"));
                                                                    String pwd = c.getString(c.getColumnIndex("password"));
                                                                    Log.e("Tag", account + "-->" + pwd);
                                                                    c.close();
                                                                    edtype.setText(account);
                                                                    edpwd.setText(pwd);

                                                                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int id) {

                                                                            //修改数据
                                                                            //把cursor移到指定点击位置
                                                                            Cursor cursor = db.rawQuery("select * from Mypassword", null);
                                                                            cursor.moveToPosition(position);
                                                                            //得到对应位置的id
                                                                            int itemId = cursor.getInt(cursor.getColumnIndex("_id"));
                                                                            Update(edtype.getText().toString(), edpwd.getText().toString(), String.valueOf(itemId));
                                                                            Toast.makeText(PasswordList.this, "修改成功", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                        }
                                                                    });
                                                                    builder.create().show();
                                                                    //传入修改之后的值
                                                                    break;
                                                                case 1:
                                                                    //先查询数据 找出Id
                                                                    //回去数据表全局cursor
                                                                    Cursor cursor = db.rawQuery("select * from Mypassword", null);
                                                                    //adapter = new SimpleCursorAdapter(PasswordList.this, R.layout.listview_item, cursor, new String[]{"info",}, new int[]{R.id.name});
                                                                    //cursor = adapter.getCursor();
                                                                    //把cursor移到指定点击位置
                                                                    cursor.moveToPosition(position);
                                                                    //得到对应位置的id
                                                                    int itemId = cursor.getInt(cursor.getColumnIndex("_id"));
                                                                    Delete(String.valueOf(itemId));
                                                                    //删除数据的操作
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                        }
                                                    });
                                                    builder.create();
                                                    builder.show();
                                                    return true;
                                                }
                                            }

        );

        homeWatcher.stopWatch();
    }


    public void Update(String newName, String newPwd, String id) {
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/mypassword.db", null);
        db.execSQL("update Mypassword set account=?,password=? where _id=?", new Object[]{newName, newPwd, id});
    }

    public void Delete(String id) {
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/mypassword.db", null);
        db.execSQL("delete from Mypassword where _id=?", new Object[]{id});
        Cursor cursor = db.rawQuery("select * from Mypassword", null);
        adapter = new SimpleCursorAdapter(PasswordList.this, R.layout.listview_item, cursor, new String[]{"info"}, new int[]{R.id.name});
        infoList.setAdapter(adapter);
    }

    private Cursor seachData(int id) {
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/mypassword.db", null);
        Cursor cursor = db.rawQuery("select * from Mypassword where _id=?", new String[]{String.valueOf(id)});
        return cursor;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            finish();
            System.exit(0);
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Tag", "1onpause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Tag", "1onrestart");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Tag", "1onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Tag", "1onDestroy");
    }


}
