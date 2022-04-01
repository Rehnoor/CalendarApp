package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import exceptions.CanNotFindEvent;

import java.util.ArrayList;
// This class represents the Calendar object which has a month, a year, and a list of events scheduled on it.
// A Calendar is allowed to have more than one event of the same name

public class Calendar implements Writable {
    String month;
    int year;
    ArrayList<CalendarEvent> listOfEvents;

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;


    // EFFECTS: Instantiates a calendar with a month name,
    //          a year, and an empty list of events
    public Calendar(String month, int year) {
        this.month = month;
        this.year = year;
        listOfEvents = new ArrayList<>();
    }


    // MODIFIES: this
    // EFFECTS: adds given event to list of events
    public void addEvent(CalendarEvent e) {
        listOfEvents.add(e);
        EventLog.getInstance().logEvent(new Event("Event created with... \tTitle: " + e.getTitle()
                + "\tStart date: " + e.getStartDate() + "\tEnd date: " + e.getEndDate() + "\tCategory: "
                + e.getCategory()));
    }


    // MODIFIES: this
    // EFFECTS: deletes the given event from the calendar
    public void deleteEvent(CalendarEvent e) throws CanNotFindEvent {
        if (listOfEvents.contains(e)) {
            listOfEvents.remove(e);
            EventLog.getInstance().logEvent(new Event("Event named " + e.getTitle() + " deleted."));
        } else {
            throw new CanNotFindEvent();
        }
    }

    // EFFECTS: Returns true if the event is on the calendar,
    //          if not found, return false
    public boolean isOnCalendar(CalendarEvent e) {
        return listOfEvents.contains(e);
    }

    // EFFECTS: ReturnCs true if there is more than one occurrence of
    //          an event with a given name(but may be on different days)
    public boolean isThereSimilarEvent(String s) {
        int count = 0;
        for (CalendarEvent i : listOfEvents) {
            if (s.equals(i.getTitle())) {
                count++;
            }
        }
        return (count > 1);
    }

    // EFFECTS: finds and returns the event with the specified name
    public CalendarEvent getEvent(String eventName) {
        CalendarEvent i = null;
        for (CalendarEvent e : listOfEvents) {
            if (e.getTitle().equals(eventName)) {
                i = e;
            }
        }
        return i;
    }


    // EFFECTS: Returns the event with the specified name and start/end dates
    public CalendarEvent getEvent(String eventName, int start, int end) {
        ArrayList<CalendarEvent> same = sameNames(eventName);
        for (CalendarEvent e : same) {
            if (e.getStartDate() == start && e.getEndDate() == end) {
                return e;
            }
        }
        return null;
    }

    // EFFECTS: Helper method for getEvent. Returns list of events with
    //          the same name.
    private ArrayList<CalendarEvent> sameNames(String name) {
        ArrayList<CalendarEvent> sameNames = new ArrayList<>();
        for (CalendarEvent e : listOfEvents) {
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

    public ArrayList<CalendarEvent> getListOfEvents() {
        return listOfEvents;
    }

    // EFFECTS: Checks if listOfEvents contains an event with name
    public Boolean isEventOnCalendar(String name) {
        for (CalendarEvent e : listOfEvents) {
            return (e.getTitle().equals(name));
        }
        return false;
    }


    // EFFECTS: Represents the calendar object in JSON format
    // This method has taken inspiration from the Workroom class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("month", month);
        json.put("year", year);
        json.put("Events", eventsToJson());
        return json;
    }

    // EFFECTS: Convert each event in listOfEvents to JSON format
    // This method has taken inspiration from the Workroom class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private JSONArray eventsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (CalendarEvent e : listOfEvents) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }
}
