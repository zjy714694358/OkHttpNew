package com.zjy.okhttpnew;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView responseText;
    Button sendRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendRequest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {
            sendRequestWithOkHttp();
            int[] newShuzu = {1,5,6,2,3,7};
            Mp(newShuzu);
            Log.e("===", Arrays.toString(newShuzu));
            //int[] m2 = {2,1,5,3,4,6,8};
            M2(newShuzu);
            Log.e("m2====",Arrays.toString(newShuzu));
            Mp(newShuzu);
            Log.e("mp===",Arrays.toString(newShuzu));
        }
    }
    private void sendRequestWithOkHttp() {
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    OkHttpClient client = new OkHttpClient();
                    // post请求
                    RequestBody requestBody = new FormBody.Builder()
                            .add("type","1")
                            .add("name","test")
                            .build();

                    Request request = new Request.Builder()
                            //.url("http://devmg.yunlutong.com/api/test/testApi")
                            .url("https://www.httpbin.org/post")
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
    public void Mp(int[] sz){
        for(int i=0;i<sz.length-1;i++){
            for(int j=0;j<sz.length-i-1;j++){
                if(sz[j]>sz[j+1]){
                    int temp = sz[j];
                    sz[j] = sz[j+1];
                    sz[j+1] = temp;
                }
            }
        }
    }
    public void M2(int[] m2){
        for(int i=0;i<m2.length-1;i++){
            for(int j=0;j<m2.length-i-1;j++){
                if(m2[j]<m2[j+1]){
                    int temp = m2[j];
                    m2[j] = m2[j+1];
                    m2[j+1] = temp;
                }
            }
        }
    }
}