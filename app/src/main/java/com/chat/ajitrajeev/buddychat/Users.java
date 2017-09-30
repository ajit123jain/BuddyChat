package com.chat.ajitrajeev.buddychat;

/**
 * Created by ajit on 30/9/17.
 */

public class Users {
    public String name;
    public String status;
    public String image;


    public Users(String name, String status, String image) {
        this.name = name;
        this.status = status;
        this.image = image;
    }
    public Users(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String staus) {
        this.status = staus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
