package model;

import java.util.ArrayList;
// This class represents the Event object. The event object consists of a title (name), a start date, an end date,
// a specific category (School, Work, Family, Friends, or Personal), and a list of days indicating which days the event
// occurs on

public class Event {

    String title;
    int startDate;
    int endDate;
    String category;
    ArrayList<Integer> listOfDays = new ArrayList<>();


    // REQUIRES: The category name must be one of: School, Work, Family,
    //           Friends, and Personal
    //           start/end date can not be greater than the last day of the month
    //           (example: You can not have a start/end date of 32 January)
    // EFFECTS: Instantiates an event object with a title, start/end date, and a category
    //          Also adds a list of the dates the event occurs on to listOfDays
    public Event(String title, int startDate, int endDate, String category) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        for (int x = startDate; x <= endDate; x++) {
            listOfDays.add(x);
        }
    }

    // MODIFIES: this
    // EFFECTS: Changes the title of the event
    public void setTitle(String title) {
        this.title = title;
    }

    // REQUIRES: start can not be greater than end
    // MODIFIES: this
    // EFFECTS: Changes the start date and end date of the event
    public void setDates(int start, int end) {
        startDate = start;
        endDate = end;
        listOfDays.clear();
        for (int x = start; x <= end; x++) {
            listOfDays.add(x);
        }
    }

    // REQUIRES: The category name must be one of: School, Work, Family,
    //           Friends, and Personal
    // MODIFIES: this
    // EFFECTS: Changes the category of the event
    public void setCategory(String category) {
        this.category = category;
    }


    public String getTitle() {
        return title;
    }

    public int getStartDate() {
        return startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<Integer> getListOfDays() {
        return listOfDays;
    }
}
