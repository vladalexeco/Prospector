package com.example.prospector;

public class Point {
    private String ord;
    private String search;
    private String mad;
    private String index;
    private String comment;
    private String latitude;
    private String longitude;
    private String date;
    private String time;

    public Point(String ord, String search, String mad, String index, String comment, String latitude,
                 String longitude, String date, String time) {
        this.ord = ord;
        this.search = search;
        this.mad = mad;
        this.index = index;
        this.comment = comment;
        this.latitude= latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
    }

    // getters
    public String getOrd() {
        return this.ord;
    }

    public String getSearch() {
        return this.search;
    }

    public String getMad() {
        return this.mad;
    }

    public String getIndex() {
        return this.index;
    }

    public String getComment() {
        return this.comment;
    }

    public String getLatitude() { return  this.latitude; }

    public String getLongitude() { return this.longitude; }

    public String getDate() {
        return this.date;
    }

    public  String getTime() {
        return this.time;
    }
    //--//--

    // setters
    public void setOrd(String newOrd) {
        this.ord = newOrd;
    }

    public void setSearch(String newSearch) {
        this.search = newSearch;
    }

    public void setMad(String newMad) {
        this.mad = newMad;
    }

    public void  setIndex(String newIngex) {
        this.index = newIngex;
    }

    public void setComment(String newComment) {
        this.comment = newComment;
    }

    public void setLatitude(String newLatitude) {
        this.latitude = newLatitude;
    }

    public void setLongitude(String newLongitude) {
        this.longitude = newLongitude;
    }
    //--//--


    @Override
    public String toString() {
        return "Point: " + this.ord + " " + this.search + " " + this.mad + " " + this.index + " " + this.comment + " " +
                this.latitude + " " + this.longitude + " " + this.date + " " + this.time;
    }

    public boolean equalsObj(Point another) {
        if (this.ord.equals(another.getOrd()) && this.search.equals(another.getSearch()) && this.mad.equals(another.getMad()) &&
        this.index.equals(another.getIndex()) && this.comment.equals(another.getComment()) && this.latitude.equals(another.getLatitude()) &&
        this.longitude.equals(another.getLongitude())) return true;
        else return false;
    }
}
