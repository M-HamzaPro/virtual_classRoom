package com.example.fypwebhost;

public class JoinedClassesModel {

    private String id, name,  subject, classID;

    public JoinedClassesModel(String id, String name, String subject, String classID) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.classID = classID;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
