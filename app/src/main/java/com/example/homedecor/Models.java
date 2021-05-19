package com.example.homedecor;


///This class is used to get database attributes value such as models ID,image URL, and TEXT from google forebase DB.
//and this class will me used to populate the Recycler CArd view present at bottom of screen shows Models image.
public class Models {
    String image,text,id;

    public Models() {
    }



    public Models(String image, String text, String id) {
        this.image = image;
        this.text = text;
        this.id=id;


    }
//to get ID of Model
    public String getId() {
        return id;
    }
//to set ID of MODel
    public void setId(String id) {
        this.id = id;
    }
//similar to ID
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