package com.example.fypwebhost;

public class JoinedClassesModel {

    private String id, name,  subject;

    public JoinedClassesModel(String id, String name, String subject) {
        this.id = id;
        this.name = name;

        this.subject = subject;
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
