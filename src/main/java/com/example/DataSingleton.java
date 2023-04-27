package com.example;

import java.net.Socket;

public class DataSingleton {

    private static final DataSingleton instance = new DataSingleton();

    public String username;
    public Socket socket;

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

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
