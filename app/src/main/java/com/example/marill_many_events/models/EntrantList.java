package com.example.marill_many_events.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * entrantList represents a list of {@link Entrant} objects.
 * It provides methods for adding, updating, retrieving, and removing entrants based on their status.
 */
public class EntrantList {
    private List<Entrant> entrants = new ArrayList<>();

    /**
     * Default constructor for entrantList.
     */
    public EntrantList(){
    }

    /**
     * Adds an entrant to the list. The entrant is initially added with a default status, such as "Waitlist".
     *
     * @param entrant The {@link Entrant} object to be added to the list.
     */
    public void addEntrant(Entrant entrant){
        this.entrants.add(entrant);
    }

    /**
     * Updates the status of an entrant in the list.
     *
     * @param user   The {@link User} whose status needs to be updated.
     * @param Status The new status to be assigned (e.g., Waitlist, Cancelled, Enrolled, Invited).
     */
    public void setEntrantStatus(User user, String Status){
        for (Entrant entrant: this.entrants) {
            User currentuser = entrant.getUser();
            if(currentuser == user){
                entrant.setStatus(Status);
                return;
            }
        }
    }

    /**
     * Retrieves a list of entrants who match the specified status.
     *
     * @param Status The status to filter entrants by (e.g., Waitlist, Cancelled, Enrolled, Invited).
     * @return A list of {@link Entrant} objects that have the specified status.
     */
    public List<Entrant> getStatusEntrantsList(String Status){
        List<Entrant> entrantsOfStatus = new ArrayList<Entrant>();
        for (Entrant entrant: this.entrants) {
            if(entrant.getStatus().equals(Status)){
                entrantsOfStatus.add(entrant);
            }
        }
        return entrantsOfStatus;
    }

    /**
     * Removes an entrant from the list based on the provided {@link User}.
     *
     * @param user The {@link User} to be removed from the entrant list.
     */
    public void removeEntrant(User user){
        for (Entrant entrant: this.entrants) {
            User currentuser = entrant.getUser();
            if(currentuser == user){
                this.entrants.remove(entrant);
                break; // Exit loop after removing to prevent concurrent modification
            }
        }
    }

    /**
     * Retrieves the list of all entrants.
     *
     * @return A list of {@link Entrant} objects.
     */
    public List<Entrant> getEntrants() {
        return new ArrayList<>(entrants);
    }
}
