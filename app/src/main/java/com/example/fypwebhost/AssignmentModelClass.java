package com.example.fypwebhost;

public class AssignmentModelClass {
    private String title, dueDate,  postDate, assignmentID;

    public AssignmentModelClass(String title, String dueDate, String postDate, String assignmentID) {
        this.title = title;
        this.dueDate = dueDate;
        this.postDate = postDate;
        this.assignmentID = assignmentID;
    }

    public String getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(String assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}
