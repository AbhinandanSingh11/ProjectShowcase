package com.nimus.android.Models;

public class SpecialEvents {
    private String endDate;
    private String lastRegistrationDate;
    private String name;
    private String registrationStatus;
    private String startDate;
    private String status;
    private String image;
    private String description;
    private String domain;
    private String eventID;
    private String eventPermaLink;

    public SpecialEvents() {

    }

    public SpecialEvents(String endDate, String lastRegistrationDate, String name, String registrationStatus, String startDate, String status, String image, String description, String domain, String eventID, String eventPermaLink) {
        this.endDate = endDate;
        this.lastRegistrationDate = lastRegistrationDate;
        this.name = name;
        this.registrationStatus = registrationStatus;
        this.startDate = startDate;
        this.status = status;
        this.image = image;
        this.description = description;
        this.domain = domain;
        this.eventID = eventID;
        this.eventPermaLink = eventPermaLink;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLastRegistrationDate() {
        return lastRegistrationDate;
    }

    public void setLastRegistrationDate(String lastRegistrationDate) {
        this.lastRegistrationDate = lastRegistrationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventPermaLink() {
        return eventPermaLink;
    }

    public void setEventPermaLink(String eventPermaLink) {
        this.eventPermaLink = eventPermaLink;
    }
}
