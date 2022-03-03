package model;

import exceptions.GreaterThanLastDay;
import exceptions.InvalidCategory;
import exceptions.InvalidDates;
import exceptions.StartGreaterThanEnd;

import java.time.LocalDate;
import java.util.ArrayList;
// This class represents the Event object. The event object consists of a title (name), a start date, an end date,
// a specific category (School, Work, Family, Friends, or Personal), and a list of days indicating which days the event
// occurs on

public class Event {
    static final int maxDay = LocalDate.now().lengthOfMonth();

    String title;
    int startDate;
    int endDate;
    String category;
    ArrayList<Integer> listOfDays = new ArrayList<>();


    // EFFECTS: Instantiates an event object with a title, start/end date, and a category
    //          Also adds a list of the dates the event occurs on to listOfDays
    public Event(String title, int startDate, int endDate, String category) throws InvalidCategory, InvalidDates {
        checkInvalidDates(startDate, endDate);
        checkInvalidCategory(category);
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


    // MODIFIES: this
    // EFFECTS: Changes the start date and end date of the event
    public void setDates(int start, int end) throws InvalidDates {
        checkInvalidDates(start, end);
        startDate = start;
        endDate = end;
        listOfDays.clear();
        for (int x = start; x <= end; x++) {
            listOfDays.add(x);
        }
    }

    // MODIFIES: this
    // EFFECTS: Changes the category of the event
    public void setCategory(String category) throws InvalidCategory {
        checkInvalidCategory(category);
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


    // EFFECTS: Checks whether the given start and end date is invalid. If it is, throw the corresponding
    //          exception. If not, do nothing.
    private void checkInvalidDates(int start, int end) throws InvalidDates {
        if (start > end) {
            throw new StartGreaterThanEnd();
        } else if (start > maxDay || end > maxDay) {
            throw new GreaterThanLastDay();
        }
    }

    //EFFECTS: Checks whether the given category is invalid. If it is, throw an exception, if not, do nothing
    private void checkInvalidCategory(String cat) throws InvalidCategory {
        if (!(cat.equals("school") || cat.equals("work") || cat.equals("family")
                || cat.equals("friends") || cat.equals("personal"))) {
            throw new InvalidCategory();
        }
    }
}
