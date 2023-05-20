package com.example.login;

import android.app.Activity;
import android.content.ContentValues;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PHPRequest
{
    private final String url;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    public PHPRequest(String prefix){
        url = prefix;
    }

    void post(String url, Callback callback) {
        RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void doRequest(Activity a, String file, ContentValues params, RequestHandler rh)
    {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url + file))
                .newBuilder();

        for(String key : params.keySet()){
            urlBuilder.addQueryParameter(key, params.getAsString(key));
        }

        post(urlBuilder.build().toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
            {
                if(response.isSuccessful())
                {
                    final String responseStr = response.body() != null ? response.body().string() : null;

                    a.runOnUiThread(() -> rh.processResponse(responseStr));
                }
                else {
                    throw new IOException("Unexpected code: " + response);
                }
            }
        });
    }
}
