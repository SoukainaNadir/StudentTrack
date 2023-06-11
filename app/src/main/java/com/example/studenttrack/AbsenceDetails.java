package com.example.studenttrack;

public class AbsenceDetails {
    private long cid;
    private String date;
    private String status;
    private String subjectName;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }



    public AbsenceDetails(long cid, String date, String status, String subjectName) {
        this.cid = cid;
        this.date = date;
        this.status = status;
        this.subjectName = subjectName;
    }


    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

