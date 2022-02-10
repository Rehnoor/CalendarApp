package model;

import java.util.ArrayList;

public class Calender {

    String month;
    int year;
    ArrayList<Event> listOfEvents;



    // EFFECTS: Instantiates a calendar with a month name,
    //          a year, and an empty list of events
    public Calender(String month, int year) {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: adds given events to list of events
    public void addEvent(Event e) {
        // stub
    }


    // MODIFIES: this
    // EFFECTS: deletes the given event from the calendar
    public void deleteEvent(Event e) {
        // stub
    }

    // EFFECTS: Returns true if the event is on the calendar,
    //          if not found, return false
    public boolean isOnCalendar(Event e) {
        return false; // stub
    }

    // EFFECTS: Returns true if there is more than one occurrence of
    //          the same event (but may be on different days)
    public boolean isThereSimilarEvent(Event e) {
        return false; // stub
    }

    // REQUIRES: There is only one event with the name and
    //           the event is in the list
    // EFFECTS: returns the event with the specified name
    public Event getEvent(String eventName) {
        return null; // stub
    }

    // REQUIRES: There is more than one event and event is
    //           already in the list
    // EFFECTS: Returns the event with the specified name and start/end dates
    public Event getEvent(String eventName, int start, int end) {
        return null;
    }




    public String getMonth() {
        return null; // stub
    }

    public int getYear() {
        return 0; // stub
    }




}
