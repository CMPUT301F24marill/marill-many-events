package com.example.marill_many_events.models;

/**
 * Image represents an image with a URL and a flag indicating whether the image was generated.
 */
public class Image {

    private String ImageURL;
    boolean generated = true;

    /**
     * Constructs an Image object with a specified URL and generated status.
     *
     * @param url       The URL of the image.
     * @param generated A boolean indicating whether the image was generated (true) or provided (false).
     */
    public Image(String url, boolean generated){
        this.ImageURL = url;
        this.generated = generated;
    }

    /**
     * Retrieves the URL of the image.
     *
     * @return The URL of the image.
     */
    public String getImageURL() {
        return ImageURL;
    }

    /**
     * Sets the URL of the image.
     *
     * @param imageURL The new URL for the image.
     */
    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    /**
     * Checks if the image was generated.
     *
     * @return True if the image was generated, otherwise false.
     */
    public boolean isGenerated() {
        return generated;
    }

    /**
     * Sets the generated status of the image.
     *
     * @param generated A boolean indicating the generated status of the image.
     */
    public void setGenerated(boolean generated) {
        this.generated = generated;
    }
}
