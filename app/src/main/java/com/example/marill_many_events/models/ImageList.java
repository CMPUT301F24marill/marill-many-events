package com.example.marill_many_events.models;

import java.util.ArrayList;
import java.util.List;

/**
 * ImageList represents a collection of {@link Entrant} objects that store image-related data.
 */
//public class ImageList {
//
//    private List<Entrant> ImageList = new ArrayList<>();
//}

public class ImageList {

    private List<Entrant> imageList = new ArrayList<>();

    /**
     * Adds an entrant to the image list.
     *
     * @param entrant The entrant to add.
     */
    public void addEntrant(Entrant entrant) {
        imageList.add(entrant);
    }

    /**
     * Retrieves the list of entrants.
     *
     * @return A list of entrants.
     */
    public List<Entrant> getEntrants() {
        return new ArrayList<>(imageList);
    }

    /**
     * Removes an entrant from the image list.
     *
     * @param entrant The entrant to remove.
     */
    public void removeEntrant(Entrant entrant) {
        imageList.remove(entrant);
    }

    /**
     * Clears all entrants from the list.
     */
    public void clearList() {
        imageList.clear();
    }
}

