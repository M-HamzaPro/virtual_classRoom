package com.example.fypwebhost;

public class MembersModelClass {

    String memberName, memberEmail, studentID;
    public MembersModelClass(String memberName, String memberEmail, String studentID)
    {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.studentID = studentID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }
}
