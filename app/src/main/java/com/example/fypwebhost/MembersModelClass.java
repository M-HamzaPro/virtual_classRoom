package com.example.fypwebhost;

public class MembersModelClass {

    String memberName, memberEmail;
    public MembersModelClass(String memberName, String memberEmail)
    {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
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
