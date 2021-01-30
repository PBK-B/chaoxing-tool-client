package com.zmide.search;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
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

import com.zmide.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;

import org.json.JSONException;
import org.xml.sax.XMLReader;

import java.io.IOException;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private Button mStartBtn;
    private OkHttpClient client = new OkHttpClient();
    String TAG = "zmide";
    Context mContext;
    boolean isVip = true;


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
        setContentView(R.layout.search_activity_main);
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
//                            String answer = null;
                            try {
                                String tt = question.getText().toString();
                                String answer = "";
                                // String hh = "<br>";
                                String hh = "\n";
                                String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";


                                switch(tiku) {
                                    case "通用接口":
                                        // 题库一
                                        try {
                                            String da1 =searchDao.apiSeekTT1(tt);
                                            answer = answer + da1 + hh;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        // 题库二
                                        try {
                                            String da2 = searchDao.apiSeekTT2(tt);
                                            answer = answer + da2 + hh;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        // 题库三
                                        try {
                                            String da3 = searchDao.apiSeekTT3(tt);
                                            answer = answer + da3 + hh;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        // 处理接口返回脏数据
                                        answer = answer.replace("李恒雅", "查题君");
                                        answer = answer.replace("并发限制,请使用token(公众号:叛逆青年旅舍 申请)", "");
                                        toast(answer);
                                        answer01.setText(answer);

                                        break;
                                    case "专属接口":
                                        if (isVip) {
                                            // 专属题库
                                            try {
                                                String da0 = searchDao.apiSeekTT0(tt);
                                                answer = answer + da0 + hh;
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (!isVip) {
                                            toast("未授权");
                                            answer01.setText("未授权");
                                            break;
                                        }
                                        toast(answer);
                                        answer01.setText(answer);
                                        break;
                                    case "英译汉":
                                        // 判断不是中文的话就翻译
                                        if (!isChinese(tt.replaceAll(regEx, ""))) {
                                            try {
                                                String fy = searchDao.apiFY(tt);
                                                answer = answer + fy + hh;
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        toast(answer);
                                        answer01.setText(answer);
                                }


                                /*
                                String answer = "";
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
                                */

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });



//        悬浮窗开始
  /*      if (Build.VERSION.SDK_INT >= 23) {
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
*/
        initView();
    }

    private void initView() {


        //绑定打开悬浮窗按钮
        mStartBtn = findViewById(R.id.main_start_btn);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SeekService.class);
//                getApplicationContext().startService(intent);

                onFloat();

            }
        });



    }

    boolean isBall = true;

    private void showBall() {
        isBall = true;
        View view = new ImageView(getApplicationContext());
        Glide.with(getApplicationContext()).load(getResources().getDrawable(R.drawable.search_ic_360)).into((ImageView) view);
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
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.search_layout_xfc, null);
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





}
