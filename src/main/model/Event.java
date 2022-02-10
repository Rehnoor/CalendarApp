package model;

public class Event {

    String title;
    int startDate;
    int endDate;
    String category;



    // REQUIRES: The category name must be one of: School, Work, Family,
    //           Friends, and Personal
    // EFFECTS: Instantiates an event object with a title, start/end date, and a category
    public Event(String title, int startDate, int endDate, String category) {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: Changes the title of the event
    public void setTitle(String title) {
        // stub
    }

    // REQUIRES: startDate can not be greater than endDate
    // MODIFIES: this
    // EFFECTS: Changes the start date and end date of the event
    public void setDates(int start, int end) {
        // stub
    }

    // REQUIRES: The category name must be one of: School, Work, Family,
    //           Friends, and Personal
    // MODIFIES: this
    // EFFECTS: Changes the category of the event
    public void setCategory(String category) {
        // stub
    }


    public String getTitle() {
        return null; // stub
    }

    public int getStartDate() {
        return -1; // stub
    }

    public int getEndDate() {
        return -1; // stub
    }
}
