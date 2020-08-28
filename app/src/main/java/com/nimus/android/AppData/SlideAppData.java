package com.nimus.android.AppData;

import com.nimus.android.Models.Slide;

import java.util.ArrayList;

public class SlideAppData {
    private static SlideAppData instance;
    private ArrayList<Slide> arrayList;

    private SlideAppData(){
        arrayList = new ArrayList<>();
    }

    public static SlideAppData getInstance(){
        if(instance == null){
            instance = new SlideAppData();
        }

        return instance;
    }


    public ArrayList<Slide> getArrayList(){
        return arrayList;
    }

}