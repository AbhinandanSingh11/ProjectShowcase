package com.nimus.android.Models;

public class EventResource extends ProjectModel {
    private String eventID;
    private int type;
    private SpecialEvents specialEvent;
    private String resType;

    //TYPE 0 == Video
    //TYPE 1 == Normal Project


    public EventResource(String category, String cost, String date, String desc, String email, String image, String imageURL, String title, String uid, String url, String user, String eventID, int type, SpecialEvents specialEvent) {
        super(category, cost, date, desc, email, image, imageURL, title, uid, url, user);
        this.eventID = eventID;
        this.type = type;
        this.specialEvent = specialEvent;
    }

    public EventResource() {

    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SpecialEvents getSpecialEvent() {
        return specialEvent;
    }

    public void setSpecialEvent(SpecialEvents specialEvent) {
        this.specialEvent = specialEvent;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }
}
