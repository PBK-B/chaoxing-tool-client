package com.zmide.xuexiton;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button mStartBtn;
    private OkHttpClient client = new OkHttpClient();
    String TAG = "PBK-B";
    Context mContext;
    boolean isVip = false;


    private Button paste;
    private Button search;
    private Button clear;
    private EditText question;
    private TextView answer01;
    private Spinner spinner;

    searchDao searchDao = new searchDao();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;


        //主页面控件绑定
        paste = (Button) findViewById(R.id.paste);
        clear = (Button) findViewById(R.id.clear);
        question = (EditText) findViewById(R.id.question);
        search = (Button) findViewById(R.id.search);
        answer01 = (TextView) findViewById(R.id.answer);
        spinner = (Spinner) findViewById(R.id.spinner);

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取剪贴板数据
                String content = null;
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                try {
                    ClipData data = cm.getPrimaryClip();
                    ClipData.Item item = data.getItemAt(0);
                    content = item.getText().toString();
                    question.setText(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.setText("");
                toast("已清空.....");
            }
        });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String tiku = (String) spinner.getSelectedItem();
                        if (question.getText().toString().isEmpty()) {
                            answer01.setText("请输入题目后搜索");
                            toast("请输入题目后搜索");
                        } else {
                            answer01.setText("正在搜索中.....");
                            toast("正在搜索中.....");
                            String answer = null;
                            try {
                                switch(tiku){
                                    case "题库1":
                                        answer = searchDao.apiSeekTT1(question.getText().toString());
                                        break;
                                    case "题库2":
                                        answer = searchDao.apiSeekTT2(question.getText().toString());
                                        break;
                                    case "题库3":
                                        answer = searchDao.apiSeekTT3(question.getText().toString());
                                        break;
                                    case "翻译":
                                        answer = searchDao.apiFY(question.getText().toString());
                                        break;

                                }
                                answer = answer.replace("李恒雅", "查题君");
                                answer = answer.replace("并发限制,请使用token(公众号:叛逆青年旅舍 申请)", "");
                                answer01.setText(answer);
                                toast(answer);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });



//        悬浮窗开始
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(MainActivity.this)) {

                Toast.makeText(MainActivity.this, "已开启悬浮窗权限！", Toast.LENGTH_SHORT).show();
                onFloat();
            } else {
                // 若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                Toast.makeText(MainActivity.this, "需要取得权限以使用悬浮窗", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                // setViewPoint("", true);
            }
        } else {

        }

        initView();
        getAnnouncement();
        showAnnouncement();
    }


    private void initView() {

        TextView mVipText = findViewById(R.id.main_vip_text);
        if(isVip) {
            mVipText.setVisibility(View.VISIBLE);
        }

        mStartBtn = findViewById(R.id.main_start_btn);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SeekService.class);
//                getApplicationContext().startService(intent);

                onFloat();

            }
        });

        TextView mJrqqText = findViewById(R.id.main_jrqq_text);
        mJrqqText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /****************
                 * 发起添加群流程
                 ******************/
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + "TI8c6LLADvf811TMU3SfD3Pcf50lpvLP"));
                    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        toast("正在加入QQ群...");
                        startActivity(intent);
                    } catch (Exception e) {
                        toast("加入QQ群失败，请检查是否安装QQ客户端.");
                    }

            }
        });

    }

    boolean isBall = true;

    private void showBall() {
        isBall = true;
        View view = new ImageView(getApplicationContext());
        Glide.with(getApplicationContext()).load(getResources().getDrawable(R.drawable.ic_360)).into((ImageView) view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatWindow.destroy();
                onFloat();
            }
        });

        if (FloatWindow.get() != null && FloatWindow.get().isShowing()) {
            // Log.d(TAG, "onFloat: 销毁");
            FloatWindow.destroy();
        }

        FloatWindow.with(getApplicationContext())
                .setView(view)
                .setWidth(110)
                .setHeight(110)     //设置控件宽高
                // .setHeight(Screen.width, 0.42f)
                .setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
                .setX(Screen.width, 1f)                                   //设置控件初始位置
                .setY(Screen.height, 0.2f)
                .setDesktopShow(true)    //桌面显示
                .setMoveType(MoveType.slide)
                .setMoveStyle(500, new AccelerateInterpolator())   //贴边动画时长为500ms，加速插值器
                .build();
        FloatWindow.get().show();


    }

    private void onFloat() {

        // 显示球状态
        if (!isBall) {
            showBall();
            return;
        }

        isBall = false;
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_xfc, null);
        EditText edt = view.findViewById(R.id.xfc_seek_edt);
        Button btn = view.findViewById(R.id.xfc_seek_btn);
        TextView mTT = view.findViewById(R.id.xfc_seek_tt);
        TextView mDN = view.findViewById(R.id.xfc_seek_dn);

        Button mOffBtn = view.findViewById(R.id.xfc_off_btn);
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFloat();
            }
        });

        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Bin", "onClick: 点击了火火火火火");
                // 使悬浮窗获取焦点
                FloatWindow.get().updateFlags(WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tt = edt.getText().toString();
                edt.setText("");
                mDN.setText("");

                // 使悬浮窗失去焦点
                FloatWindow.get().updateFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                if (tt.equals("")) {
                    toast("题目不可为空！");
                    return;
                }

                mTT.setText(tt);

                // 开始搜题
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String dn = "";

                        // String hh = "<br>";
                        String hh = "\n";

                        String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
                        // 判断不是中文的话就翻译
                        if (!isChinese(tt.replaceAll(regEx, ""))) {
                            try {
                                String fy = searchDao.apiFY(tt);
                                dn = dn + fy + hh;
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (isVip) {
                            // 专属题库
                            try {
                                String da0 = searchDao.apiSeekTT0(tt);
                                dn = dn + da0 + hh;
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // 题库一
                        try {
                            String da1 =searchDao.apiSeekTT1(tt);
                            dn = dn + da1 + hh;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 题库二
                        try {
                            String da2 = searchDao.apiSeekTT2(tt);
                            dn = dn + da2 + hh;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 题库三
                        try {
                            String da3 = searchDao.apiSeekTT3(tt);
                            dn = dn + da3 + hh;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 处理接口返回脏数据
                        dn = dn.replace("李恒雅", "查题君");
                        dn = dn.replace("并发限制,请使用token(公众号:叛逆青年旅舍 申请)", "");

                        toast(dn);
                        setDN(dn);
                    }
                }).start();

            }

            void setDN(String dn) {

                // 处理换行
                String dns = dn.replace("\n", "<br>");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyImageGetter imageGetter = new MyImageGetter(getApplicationContext(), mDN);
                        mDN.setText(Html.fromHtml(dns, imageGetter, new Html.TagHandler() {
                            @Override
                            public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {
                            }
                        }));
                    }
                });
            }

        });

        if (FloatWindow.get() != null && FloatWindow.get().isShowing()) {
            Log.d(TAG, "onFloat: 销毁");
            FloatWindow.destroy();
        } else {
            FloatWindow.with(getApplicationContext())
                    .setView(view)
                    .setWidth(650)
                    // .setHeight(450)     //设置控件宽高
                    .setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
                    .setHeight(Screen.width, 0.42f)
                    .setX(0)                                   //设置控件初始位置
                    .setY(Screen.height, 0.2f)
                    .setDesktopShow(true)    //桌面显示
                    .setMoveType(MoveType.slide)
                    .setMoveStyle(500, new AccelerateInterpolator())   //贴边动画时长为500ms，加速插值器
                    .build();
            FloatWindow.get().show();

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

    /*String apiSeekTT0(String tt) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url("http://tool.chaoxing.zmorg.cn/api/search.php?q=" + tt)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String data = response.body().string();
        JSONObject jsonObject = new JSONObject(data).getJSONObject("msg");
        String resStr = "【专属题库】 " + jsonObject.getString("answer");
        return resStr;
    }

    String apiSeekTT1(String tt) throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("question", tt)
                .build();
        Request request = new Request.Builder()
                .url("http://cx.icodef.com/wyn-nb?v=2")
                .method("POST", body)
                .addHeader("Content-type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        String data = response.body().string();
        JSONObject jsonObject = new JSONObject(data);
        String resStr = "【题库1】 " + jsonObject.getString("data");
        return resStr;
    }

    String apiSeekTT2(String tt) throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("question", tt)
                .build();
        Request request = new Request.Builder()
                .url("http://exam.tk/search")
                .method("POST", body)
                .addHeader("Content-type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        String data = response.body().string();
        JSONObject jsonObject = new JSONObject(data);
        String resStr = "【题库2】 " + jsonObject.getString("data");
        return resStr;

    }

    String apiSeekTT3(String tt) throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .url("http://imnu.52king.cn/api/wk/index.php?c=" + tt)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String data = response.body().string();
        JSONObject jsonObject = new JSONObject(data);
        String resStr = "【题库3】" + jsonObject.getString("answer");
        return resStr;
    }


    String apiFY(String text) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url("http://tool.chaoxing.zmorg.cn/api/tx_fy.php?q=" + text)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String data = response.body().string();
        JSONObject jsonObject = new JSONObject(data).getJSONObject("msg");
        String resStr = "【翻译】" + jsonObject.getString("TargetText");
        return resStr;
    }*/

    /**
     * 判断该字符串是否为中文
     *
     * @param string
     * @return
     */
    public static boolean isChinese(String string) {
        int n = 0;
        for (int i = 0; i < string.length(); i++) {
            n = (int) string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
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


}
