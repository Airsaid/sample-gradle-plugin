package com.airsaid.sample.extension.utils;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UrlConnections{
    private static final int MAX_HTTP_CACHE_SIZE=10*1024*1024;
    private static final String TAG="UrlConnections";

    public static String getSourceCode(Context context, String url) {
        String result=null;
        HttpURLConnection urlConnection=null;
        try {
            File httpCacheDir = new File(context.getCacheDir(),"http_cache");
            try {
                HttpResponseCache.install(httpCacheDir,MAX_HTTP_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            URL requestUrl = new URL(url);
            urlConnection = (HttpURLConnection)requestUrl.openConnection();
            urlConnection.setConnectTimeout(5 * 1000);
            urlConnection.setReadTimeout(5 * 1000);
            urlConnection.setUseCaches(true);
            urlConnection.setDefaultUseCaches (true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.addRequestProperty("Connection", "Keep-Alive");

            urlConnection.connect();
            if (HttpURLConnection.HTTP_OK==urlConnection.getResponseCode()) {
                result = IOUtils.toString(urlConnection.getInputStream(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            Log.w(TAG,"Url:"+url+" load failed!");
        } finally {
            if(null!=urlConnection){
                urlConnection.disconnect();
            }
        }
        return result;
    }
}