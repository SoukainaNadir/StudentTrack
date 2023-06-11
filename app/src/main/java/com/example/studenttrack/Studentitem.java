package com.example.studenttrack;




public class Studentitem {

    private int roll;
    private int apogee;
    private String name;
    private String status;



    private long sid;





    public Studentitem(long sid, int roll, String name, int apogee) {
        this.sid = sid;
        this.roll = roll;
        this.name = name;
        this.apogee=apogee;
        status="";
    }
    public void setApogee(int apogee) {
        this.apogee = apogee;
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
    public int getApogee() {
        return apogee;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }



}
