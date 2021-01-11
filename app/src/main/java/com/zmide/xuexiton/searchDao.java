package com.zmide.xuexiton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class searchDao {
    private OkHttpClient client = new OkHttpClient();


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


}
