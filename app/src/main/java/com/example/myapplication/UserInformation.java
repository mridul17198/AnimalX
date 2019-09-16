package com.example.myapplication;

public class UserInformation {
    String Name;
    long pass;

    public UserInformation() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getPass() {
        return pass;
    }

    public void setPass(long pass) {
        this.pass = pass;
    }
}
