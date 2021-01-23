package com.zmide;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.zmide.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    String TAG = "zmide";
    Context mContext;
    boolean isVip = false;
    String version = "v1.5";


    private CardView card1;
    private CardView card2;
    private CardView card3;
    private CardView card4;
    private CardView card5;
    private CardView card6;
    private CardView card7;
    private CardView card8;
    private CardView card9;
    private CardView card10;

    private LinearLayout about;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;


        getAnnouncement();
        showAnnouncement();
        updata();


        card1 = (CardView) findViewById(R.id.card1);
        card2 = (CardView) findViewById(R.id.card2);
        card3 = (CardView) findViewById(R.id.card3);
        card4 = (CardView) findViewById(R.id.card4);
        card5 = (CardView) findViewById(R.id.card5);
        card6 = (CardView) findViewById(R.id.card6);
        card7 = (CardView) findViewById(R.id.card7);
        card8 = (CardView) findViewById(R.id.card8);
        card9 = (CardView) findViewById(R.id.card9);
        card10 = (CardView) findViewById(R.id.card10);

        about = (LinearLayout) findViewById(R.id.about);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
        card6.setOnClickListener(this);
        card7.setOnClickListener(this);
        card8.setOnClickListener(this);
        card9.setOnClickListener(this);
        card10.setOnClickListener(this);

        about.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card1:
                Intent intent = new Intent(MainActivity.this, com.zmide.search.MainActivity.class);
                startActivity(intent);
                break;
            case R.id.card2:
                Intent intent2 = new Intent(MainActivity.this, com.zmide.myClass.activity.MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.card3:
                /*Intent intent = new Intent(MainActivity.this, Demo.class);
                startActivity(intent);*/
                toast("正在开发中....");
                break;
            case R.id.card4:
                /*Intent intent = new Intent(MainActivity.this, Demo.class);
                startActivity(intent);*/
                toast("待添加4");
                break;
            case R.id.card5:
                /*Intent intent = new Intent(MainActivity.this, Demo.class);
                startActivity(intent);*/
                toast("待添加5");
                break;
            case R.id.card6:
                /*Intent intent = new Intent(MainActivity.this, Demo.class);
                startActivity(intent);*/
                toast("待添加6");
                break;
            case R.id.card7:
                /*Intent intent = new Intent(MainActivity.this, Demo.class);
                startActivity(intent);*/
                toast("待添加7");
                break;
            case R.id.card8:
                /*Intent intent = new Intent(MainActivity.this, Demo.class);
                startActivity(intent);*/
                toast("待添加8");
                break;
            case R.id.card9:
                /*Intent intent = new Intent(MainActivity.this, Demo.class);
                startActivity(intent);*/
                toast("待添加9");
                break;
            case R.id.card10:
                /*Intent intent = new Intent(MainActivity.this, Demo.class);
                startActivity(intent);*/
                toast("待添加10");
                break;
            case R.id.about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
        }
    }

    void toast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    // 获取公告
    void getAnnouncement() {
        String Url = "http://hnvist1.zmorg.cn/data/hnvist_xnm_announcement.json";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(Url)
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String data = response.body().string();
                                if (response.code() == 200) {
                                    Log.d(TAG, "获取公告成功！" + data);
                                    try {
                                        JSONObject json = new JSONObject(data);
                                        String msg = json.getString("msg");
                                        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                                        String announcement = pref.getString("announcement", "");
                                        if (!msg.equals("") && !msg.equals(announcement)) {
                                            // 公告相同就不更新了
                                            setAnnouncement(msg);
                                            readingAnnouncement(false);
                                            showAnnouncement();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                } catch (Error error) {
                }
            }
        }).start();
    }

    // 设置公告
    void setAnnouncement(String msg) {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("announcement", msg);
        editor.putBoolean("isReading", false);
        editor.commit();
    }

    // 阅读公告
    void readingAnnouncement(boolean isReading) {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putBoolean("isReading", isReading);
        editor.commit();
    }

    // 显示公告
    void showAnnouncement() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String announcement = pref.getString("announcement", "该软件仅用于技术学习，任何人不得用于盈利，开发者不承担任何法律责任");
        boolean isReading = pref.getBoolean("isReading", false);

        if (!isReading && announcement != null) {
            // 未阅读公告，弹出最新公告

            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    // 创建dialog构造器
                    AlertDialog.Builder normalDialog = new AlertDialog.Builder(mContext);
                    // 设置title
                    normalDialog.setTitle("友情提示！");
                    // 设置内容
                    normalDialog.setMessage(announcement);
                    // 设置按钮
                    normalDialog.setPositiveButton("我知道了"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    readingAnnouncement(true);
                                }
                            });
                    // 创建并显示
                    AlertDialog dialog = normalDialog.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            });

        }

    }

    //检查更新
    private void updata() {
        String Url = "http://hnvist1.zmorg.cn/data/hnvist_xnm_announcement.json";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(Url)
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String data = response.body().string();
                                if (response.code() == 200) {
                                    try {
                                        JSONObject json = new JSONObject(data);
                                        String msg = json.getString("msg");
                                        String newversion = json.getString("version");
                                        String downUrl = json.getString("down");
                                        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                                        String announcement = pref.getString("announcement", "");

                                        if (!version.equals(newversion)){
                                            toast(newversion);
                                            toast("检查到更新");
                                            runOnUiThread(new Runnable() {
                                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                @Override
                                                public void run() {
                                                    // 创建dialog构造器
                                                    AlertDialog.Builder normalDialog = new AlertDialog.Builder(mContext);
                                                    // 设置title
                                                    normalDialog.setTitle("发现更新");
                                                    // 设置内容
                                                    normalDialog.setMessage(msg);
                                                    //禁止用户返回
                                                    normalDialog.setCancelable(false);
                                                    // 设置按钮
                                                    normalDialog.setPositiveButton("去更新"
                                                            , new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    try {
                                                                        Uri uri = Uri.parse(downUrl);
                                                                        Intent intent = new Intent();
                                                                        intent.setAction("android.intent.action.VIEW");
                                                                        intent.setData(uri);
                                                                        startActivity(intent);
                                                                        toast("正在前往更新....");
                                                                        System.exit(0);
                                                                    } catch (Exception e) {
                                                                        toast("调用浏览器更新失败。");
                                                                        System.exit(0);
                                                                    }
                                                                }
                                                            });
                                                    normalDialog.setNegativeButton("加入QQ群"
                                                            , new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent intent = new Intent();
                                                                    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + "TI8c6LLADvf811TMU3SfD3Pcf50lpvLP"));
                                                                    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                                    try {
                                                                        toast("正在加入QQ群...");
                                                                        startActivity(intent);
                                                                        System.exit(0);
                                                                    } catch (Exception e) {
                                                                        toast("加入QQ群失败，请检查是否安装QQ客户端.");
                                                                        System.exit(0);
                                                                    }
                                                                }
                                                            });
                                                    // 创建并显示
                                                    AlertDialog dialog = normalDialog.create();
                                                    dialog.setCanceledOnTouchOutside(false);
                                                    dialog.show();
                                                }
                                            });
                                        }else {
                                            toast("已是最新版");
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                } catch (Error error) {
                }
            }
        }).start();
    }


}