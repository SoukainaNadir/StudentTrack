package com.example.studenttrack;

public class Classitem {
    private String className;

    public String getClassName(){
        return className;
    }

    public void setClassName(String className){
        this.className = className;
    }



    private String subjectName;

    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }


    public Classitem(String className, String subjectName){
        this.className = className;
        this.subjectName = subjectName;

    }

}
