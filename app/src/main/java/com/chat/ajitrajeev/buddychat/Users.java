package com.chat.ajitrajeev.buddychat;

/**
 * Created by ajit on 30/9/17.
 */

public class Users {
    public String name;
    public String staus;
    public String image;


    public Users(String name, String staus, String image) {
        this.name = name;
        this.staus = staus;
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

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
