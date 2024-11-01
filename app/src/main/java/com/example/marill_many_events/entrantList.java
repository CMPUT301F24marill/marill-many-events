package com.example.marill_many_events;

import java.util.ArrayList;
import java.util.List;

public class entrantList {
    private List<Entrant> entrants = new ArrayList<>();

    public entrantList(){
    }

    //Add entrant as waitlisted
    public void addEntrant(User user){
        Entrant entrant = new Entrant("Waitlist", user);
        this.entrants.add(entrant);
    }

    //set user status to user in list
    public void setEntrantStatus(User user, String Status){
        for (Entrant entrant: this.entrants) {
            User currentuser = entrant.getUser();
            if(currentuser == user){
                entrant.setStatus(Status);
                return;
            }
        }
    }

    //get list of users under status
    public List<Entrant> getStatusEntrantsList(String Status){
        List<Entrant> entrantsOfStatus = new List<Entrant>;
        for (Entrant entrant: this.entrants) {
            if(entrant.getStatus().equals(Status)){
                entrantsOfStatus.add(entrant);
            }
        }
        return entrantsOfStatus;
    }

    //Remove a user from the list
    public void removeEntrant(User user){
        for (Entrant entrant: this.entrants) {
            User currentuser = entrant.getUser();
            if(currentuser == user){
                this.entrants.remove(entrant);
            }
        }
    }
}
