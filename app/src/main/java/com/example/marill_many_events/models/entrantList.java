package com.example.marill_many_events.models;

import com.example.marill_many_events.models.User;

import java.util.ArrayList;
import java.util.List;

public class entrantList {
    private List<Entrant> entrants = new ArrayList<>();

    public entrantList(){
    }

    /**
     * Add entrant to list. They are added as waitlisted.
     *
     * @param entrant           User to add
     * @return The list of entrants in this event under that status
     */
    public void addEntrant(Entrant entrant){
        this.entrants.add(entrant);
    }

    /**
     * Set the status of an entrant in the list
     *
     * @param user              The user to apply the status to
     * @param Status           The status to apply for: Waitlist, Cancelled, Enrolled, Invited
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
     * Gets a list of entrants under specified status
     *
     * @param Status           The status to search for: Waitlist, Cancelled, Enrolled, Invited
     * @return The list of entrants in this event under that status
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
     * Remove a User from the list.
     *
     * @param user  The user to remove
     */
    public void removeEntrant(User user){
        for (Entrant entrant: this.entrants) {
            User currentuser = entrant.getUser();
            if(currentuser == user){
                this.entrants.remove(entrant);
            }
        }
    }
}
