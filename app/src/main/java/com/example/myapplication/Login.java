package com.example.myapplication;

public class Login {
    String id;
    String name;
    long pass;
    public Login(){
    }
    public Login(String id,String name,long pass) {
        this.id=id;
        this.name = name;
        this.pass=pass;
    }
    public String getId(){
        return id;
    }
    public String getName() {
        return name;
    }

    public long getPass() {
        return pass;
    }
}
