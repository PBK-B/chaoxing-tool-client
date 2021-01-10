package com.zmide.xuexiton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    String TAG = "PBK-B";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CharSequence text = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        // Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
        searchTT(text.toString());
        finish();
        // setContentView(R.layout.activity_search);
    }

    void searchTT(String tt) {
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
                        String fy = apiFY(tt);
                        dn = dn + fy + hh;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // 专属题库
                try {
                    String da0 = apiSeekTT0(tt);
                    dn = dn + da0 + hh;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 题库一
                try {
                    String da1 = apiSeekTT1(tt);
                    dn = dn + da1 + hh;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 题库二
//                try {
//                    String da2 = apiSeekTT2(tt);
//                    dn = dn + da2 + hh;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                // 题库三
//                try {
//                    String da3 = apiSeekTT3(tt);
//                    dn = dn + da3 + hh;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                // 处理接口返回脏数据
                dn = dn.replace("李恒雅", "好傻好天真");

                // showTTWindow(tt,dn);
                toast(dn);
            }
        }).start();
    }

    void toast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showTTWindow(String wt, String da) {
        String tag = "toast";
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_toast, null);
        TextView mTT = view.findViewById(R.id.xfc_toast_tt);
        TextView mDN = view.findViewById(R.id.xfc_toast_dn);

        mTT.setText(wt);
        mDN.setText(da);

        if (FloatWindow.get(tag) != null && FloatWindow.get().isShowing()) {
            Log.d(TAG, "onFloat: 销毁");
            FloatWindow.destroy(tag);
        } else {
            FloatWindow.with(getApplicationContext())
                    .setView(view)
                    .setWidth(600)
                    .setTag(tag)
                    // .setHeight(450)     //设置控件宽高
                    .setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
                    .setHeight(Screen.width, 0.3f)
                    .setX(0)                                   //设置控件初始位置
                    .setY(Screen.height, 0.85f)
                    .setDesktopShow(true)    //桌面显示
                    .setMoveType(MoveType.slide)
                    .setMoveStyle(500, new AccelerateInterpolator())   //贴边动画时长为500ms，加速插值器
                    .build();
            FloatWindow.get().show();

        }
    }

    String apiSeekTT0(String tt) throws IOException, JSONException {
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