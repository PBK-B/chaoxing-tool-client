package com.zmide.xuexiton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyImageGetter implements Html.ImageGetter {

    WeakReference<TextView> mTextViewReference;
    Context mContext;

    public MyImageGetter(Context context, TextView textView) {
        mContext = context.getApplicationContext();
        mTextViewReference = new WeakReference<TextView>(textView);
    }

    @Override
    public Drawable getDrawable(String url) {

        URLDrawable urlDrawable = new URLDrawable(mContext);

        // 异步获取图片，并刷新显示内容
        new ImageGetterAsyncTask(url, urlDrawable).execute();

        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {

        WeakReference<URLDrawable> mURLDrawableReference;
        String mUrl;

        public ImageGetterAsyncTask(String url, URLDrawable drawable) {
            mURLDrawableReference = new WeakReference<URLDrawable>(drawable);
            mUrl = url;
        }

        public Bitmap getBitmap(String url) {
            URL imageURL = null;
            Bitmap bitmap = null;
            Log.e("inuni","URL = "+url);
            try {
                imageURL = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) imageURL
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Drawable doInBackground(String... params) {

            // 下载图片，并且使用缓存
            Bitmap bitmap = getBitmap(mUrl);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);

            // 设定最小宽高
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            w = w < 30 ? 35 : w ;
            h = h < 30 ? 35 : h ;

            Rect bounds = new Rect(0, 0, w, h);

            if (mURLDrawableReference.get() != null) {
                mURLDrawableReference.get().setBounds(bounds);
            }
            bitmapDrawable.setBounds(bounds);
            return bitmapDrawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (null != result) {
                if (mURLDrawableReference.get() != null) {
                    mURLDrawableReference.get().drawable = result;
                }
                if (mTextViewReference.get() != null) {
                    // 加载完一张图片之后刷新显示内容
                    mTextViewReference.get().setText(mTextViewReference.get().getText());
                }
            }
        }
    }


    public class URLDrawable extends BitmapDrawable {
        protected Drawable drawable;

        public URLDrawable(Context context) {
            // 设置默认大小和默认图片
            Rect bounds = new Rect(0, 0, 100, 100);
            setBounds(bounds);
            drawable = context.getResources().getDrawable(R.drawable.ic_launcher_background);
            drawable.setBounds(bounds);
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}