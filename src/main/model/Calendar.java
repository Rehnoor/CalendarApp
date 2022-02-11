package model;

import java.util.ArrayList;

public class Calendar {

    String month;
    int year;
    ArrayList<Event> listOfEvents;


    // EFFECTS: Instantiates a calendar with a month name,
    //          a year, and an empty list of events
    public Calendar(String month, int year) {
        this.month = month;
        this.year = year;
        listOfEvents = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds given event to list of events
    public void addEvent(Event e) {
        listOfEvents.add(e);
    }

    // REQUIRES: Event is already in the list of events
    // MODIFIES: this
    // EFFECTS: deletes the given event from the calendar
    public void deleteEvent(Event e) {
        listOfEvents.remove(e);
    }

    // EFFECTS: Returns true if the event is on the calendar,
    //          if not found, return false
    public boolean isOnCalendar(Event e) {
        return listOfEvents.contains(e);
    }

    // EFFECTS: Returns true if there is more than one occurrence of
    //          an event with a given name(but may be on different days)
    public boolean isThereSimilarEvent(String s) {
        int count = 0;
        for (Event i : listOfEvents) {
            if (s.equals(i.getTitle())) {
                count++;
            }
        }
        return (count > 1);
    }

    // REQUIRES: There is only one event with the name and
    //           the event is in the list
    // EFFECTS: finds and returns the event with the specified name
    public Event getEvent(String eventName) {
        Event i = null;
        for (Event e : listOfEvents) {
            if (e.getTitle().equals(eventName)) {
                i = e;
            }
        }
        return i;
    }

    // REQUIRES: There is more than one event with the same name and events are
    //           already in the list
    // EFFECTS: Returns the event with the specified name and start/end dates
    public Event getEvent(String eventName, int start, int end) {
        ArrayList<Event> same = sameNames(eventName);
        for (Event e : same) {
            if (e.getStartDate() == start && e.getEndDate() == end) {
                return e;
            }
        }
        return null;
    }

    // EFFECTS: Helper method for getEvent. Returns list of events with
    //          the same name.
    private ArrayList<Event> sameNames(String name) {
        ArrayList<Event> sameNames = new ArrayList<>();
        for (Event e : listOfEvents) {
            if (e.getTitle().equals(name)) {
                sameNames.add(e);
            }
        }


        return sameNames;
    }


    public String getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<Event> getListOfEvents() {
        return listOfEvents;
    }


}
