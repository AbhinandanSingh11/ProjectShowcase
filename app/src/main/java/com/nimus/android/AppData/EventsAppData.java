package com.nimus.android.AppData;

import com.nimus.android.Models.SpecialEvents;

import java.util.ArrayList;

public class EventsAppData {
    private static EventsAppData instance;
    private ArrayList<SpecialEvents> specialEventsArrayList;

    private EventsAppData(){
        specialEventsArrayList = new ArrayList<>();
    }

    public static EventsAppData getInstance(){
        if(instance == null){
            instance = new EventsAppData();
        }

        return instance;
    }

    public ArrayList<SpecialEvents> getSpecialEvents(){
        return specialEventsArrayList;
    }
}
