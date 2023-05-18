package com.example.logintest;

import android.app.Activity;
import android.content.ContentValues;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PHPRequest
{
    private final String url;
    public PHPRequest(String prefix){
        url = prefix;
    }

    public void doRequest(Activity a, String method, ContentValues params, RequestHandler rh)
    {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        for(String key : params.keySet()) {
            builder.add(key, params.getAsString(key));
        }

        Request request = new Request.Builder()
                .url(url + method + ".php")
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
            {
                if (response.isSuccessful())
                {
                    final String responseStr = response.body() != null ? response.body().string() : null;
                    a.runOnUiThread(() -> rh.processResponse(responseStr));
                }
                else {
                    throw new IOException("Unexpected response: " + response);
                }
            }
        });
    }
}
