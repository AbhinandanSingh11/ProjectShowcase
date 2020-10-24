package com.nimus.android.AppData;

import com.nimus.android.Models.EventResource;
import com.nimus.android.Models.SpecialEvents;

import java.util.ArrayList;

public class EventResourceAppData {
    private static EventResourceAppData instance;
    private ArrayList<EventResource> eventResourceArrayList;

    private EventResourceAppData(){
        eventResourceArrayList = new ArrayList<>();
    }

    public static EventResourceAppData getInstance(){
        if(instance == null){
            instance = new EventResourceAppData();
        }

        return instance;
    }

    public ArrayList<EventResource> getEventResources(){
        return eventResourceArrayList;
    }
}
