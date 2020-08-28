package com.nimus.android.AppData;

import com.nimus.android.Models.ProjectModel;

import java.util.ArrayList;

public class AppDataModel {
    private static AppDataModel instance;
    private ArrayList<ProjectModel> arrayList;


    private AppDataModel(){
        arrayList = new ArrayList<>();
    }


    public static AppDataModel getInstance(){
        if(instance ==  null){
            instance = new AppDataModel();
        }
        return instance;
    }


    public ArrayList<ProjectModel> getArrayList(){
        return arrayList;
    }


}
