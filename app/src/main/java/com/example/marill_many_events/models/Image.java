package com.example.marill_many_events.models;

public class Image {

    private String ImageURL;
    boolean generated = true;

    public Image(String url, boolean generated){
        this.ImageURL = url;
        this.generated = generated;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }
}
