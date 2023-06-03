package com.example.logintest;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 * Class to handle post/get requests
 * */
public class HTTPHandler {
    //create a client
    final OkHttpClient client = new OkHttpClient();

    public HTTPHandler(){}

    /**
     * Perform a GET request
     *
     * @param url the url to perform the request on
     * @param params the key-value query parameters
     * @param responseType The type the response output should be cast to.Valid ones are
     * String.class,JSONArray.class,JSONObject.class
     * @return The response of the query
     * */
    public <T> T getRequest(String url, @NonNull JSONObject params, Class<T> responseType) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        params.keys().forEachRemaining(key -> {
            try {
                String value = params.get(key).toString();
                urlBuilder.addQueryParameter(key,value);
            }catch (JSONException e){
                System.out.println("error");
            }
        });

        String requestUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        CompletableFuture<T> futureResult = new CompletableFuture<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                futureResult.completeExceptionally(e);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    throw new IOException("Unexpected code " + response);
                }
                String res = response.body().string();
                T output = formatResString(res,responseType);
                futureResult.complete(responseType.cast(output));
            }
        });

        return futureResult.join();
    }

    /**
     * Perform a POST request
     *
     * @param url the url to perform the request on
     * @param params the key-value query parameters
     * @param responseType The type the response output should be cast to.Valid ones are
     * String.class,JSONArray.class,JSONObject.class
     * @return The response of the query
     * */
    public <T> T postRequest(String url, @NonNull JSONObject params, Class <T> responseType) {
        FormBody.Builder builder = new FormBody.Builder();

        params.keys().forEachRemaining(key -> {
            try {
                String value = params.get(key).toString();
                builder.add(key,value);
            }catch (JSONException e){
                System.out.println("Error");
            }
        });

        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        CompletableFuture<T> futureResult = new CompletableFuture<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body()==null) {
                    throw new IOException("Unexpected code " + response);
                }
                String res = response.body().string();
                T output = formatResString(res,responseType);
                futureResult.complete(responseType.cast(output));
            }
        });
        return futureResult.join();
    }

    /**
     *
     * @param responseString the response string
     * @param responseType the type to cast the response string to. Valid ones are String.class,
     * String.class,JSONArray.class
     * @return responseString casted to the new type
     * @param <T> The type the response string should be cast to
     */
    public <T> T formatResString(String responseString,Class<T> responseType) {
        if (responseType.equals(JSONArray.class)) {
            JSONArray obj;
            try {
                obj = new JSONArray(responseString);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return responseType.cast(obj);
        }else if (responseType.equals(JSONObject.class)){
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                return responseType.cast(jsonObject);
            }catch (JSONException e){
                System.out.println(e.getMessage());
            }
        }
        return responseType.cast(responseString);
    }
}

