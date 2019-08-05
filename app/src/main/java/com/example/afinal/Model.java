package com.example.afinal;

public class Model {
    private  String imgUrl;
    private String title;
    private Double lat;
    private Double lng;
    private String eventLocation;
    private String eventDateTime;
    private String eventDesc;

    public Model(){
        /*
        empty
         */
    }

    public Model(String imgUrl, String title, Double lat, Double lng, String eventLocation, String eventDateTime, String eventDesc) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.eventLocation = eventLocation;
        this.eventDateTime = eventDateTime;
        this.eventDesc = eventDesc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

}
