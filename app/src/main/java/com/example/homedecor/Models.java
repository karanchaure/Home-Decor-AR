package com.example.homedecor;

public class Models {
    String image,text,id;

    public Models() {
    }



    public Models(String image, String text, String id) {
        this.image = image;
        this.text = text;
        this.id=id;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}