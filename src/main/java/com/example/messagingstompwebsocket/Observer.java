package com.example.messagingstompwebsocket;

public class Observer {

    private String content;
    private String file;

    public Observer() {
    }

    public Observer(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}