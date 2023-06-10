package com.example.studenttrack;




public class Studentitem {

    private int roll;
    private String name;
    private String status;



    private long sid;

    public Studentitem(long sid, int roll, String name) {
        this.sid = sid;
        this.roll = roll;
        this.name = name;
        status="";
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRoll() {
        return roll;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }



}
