package com.android.mo.androidthread;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/5/24.
 */
public class MeNetRunnable implements Runnable {

    public final static int POST = 1;
    public final static int GET = 0;
    private final static int timeoutMillis = 5 * 1000;

    private int method = POST;
    private MeNetCallback meCallback;
    private String mUrl;
    private int mTag;


    public MeNetRunnable(int method, int tag, String url, MeNetCallback callback) {
        this.method = method;
        this.meCallback = callback;
        mUrl = url;
        mTag = tag;
    }

    public MeNetRunnable(int method, MeNetCallback callback) {
        this.method = method;
        this.meCallback = callback;
    }

    @Override
    public void run() {
        if (method == POST) {
            doPost(meCallback);
        } else if (method == GET) {
            doGet(mTag, mUrl, meCallback);
        } else {
            doGet(mTag, mUrl, meCallback);
        }
    }

    public void doGet(int tag, String url, MeNetCallback meCallback) {
        HttpURLConnection httpUrlConnection = null;
        try {
            URL mUrl = new URL(url);
            URLConnection urlConnection = mUrl.openConnection();
            httpUrlConnection = (HttpURLConnection) urlConnection;
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();
            httpUrlConnection.setReadTimeout(timeoutMillis);
            httpUrlConnection.setConnectTimeout(timeoutMillis);
            if (httpUrlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    os.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                os.close();
                // 返回字符串
                String result = new String(os.toByteArray());
                meCallback.onSuccess(tag, result);
            } else {
                meCallback.onFailure(tag, "无法获取信息", -1);
            }
        } catch (MalformedURLException e) {
            meCallback.onFailure(tag, e.getMessage(), -1);
            e.printStackTrace();
        } catch (IOException e) {
            meCallback.onFailure(tag, e.getMessage(), -1);
            e.printStackTrace();
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
    }

    public void doPost(MeNetCallback meCallback) {

    }
}
