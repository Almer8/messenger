package com.example;

public class DataSingleton {

    private static final DataSingleton instance = new DataSingleton();

    public String username;

    private DataSingleton(){}

    public static DataSingleton getInstance(){
        return instance;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return instance.username;
    }


}
