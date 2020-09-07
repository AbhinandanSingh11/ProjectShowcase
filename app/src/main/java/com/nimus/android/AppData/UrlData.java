package com.nimus.android.AppData;

import com.nimus.android.Models.URL;

public class UrlData {
    private static UrlData instance;
    private static URL url;

    private UrlData(){
        url = new URL();
    }

    public static UrlData getInstance(){
        if(instance == null){
            instance = new UrlData();
        }

        return instance;
    }

    public static URL getUrl(){
        return url;
    }

    public static void setUrl(URL url){
         UrlData.url = url;
    }
}
