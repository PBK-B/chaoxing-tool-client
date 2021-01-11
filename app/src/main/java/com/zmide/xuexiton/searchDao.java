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

/*
    public String searchDao1(final String question) throws IOException, JSONException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String a = searchDao(question);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        return question;
    }
*/

    public String searchDao(String question) throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("question", question)
                .build();
        Request request = new Request.Builder()
                .url("http://cx.icodef.com/wyn-nb?v=2")
                .method("POST", body)
                .addHeader("Content-type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        String data = response.body().string();
        JSONObject jsonObject = new JSONObject(data);
        String answer = "【题库1】 " + jsonObject.getString("data");
        System.out.println(answer);
        return answer;
    }


}
