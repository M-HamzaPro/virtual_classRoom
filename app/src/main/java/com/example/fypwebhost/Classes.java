package com.example.fypwebhost;

public class Classes {

    private String classID, classCode, name,  subject;

    public Classes(String classID, String classCode, String name, String subject) {
        this.classCode = classCode;
        this.name = name;
        this.classID = classID;
        this.subject = subject;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
