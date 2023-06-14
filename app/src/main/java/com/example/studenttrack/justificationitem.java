package com.example.studenttrack;

public class justificationitem {
    private String justification;
    long sid;



    public justificationitem(long sid, String justification) {
        this.justification = justification;
        this.sid = sid;
    }


    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public justificationitem(String justification) {
        this.justification = justification;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }
}
