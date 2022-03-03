package ui;

import exceptions.*;
import model.Calendar;
import model.Event;
import persistence.CalendarSaveReader;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// This class runs and implements all parts of the Calendar application. The funtions include: initializing the month,
// any necessary constants, and adding, deleting, and editing events
public class CalendarApp {
    private static final String calData = "./data/calendar.json";
    private Calendar cal;
    private Scanner scnr;
    private CalendarSaveReader calSave;

    //Constants
    String month = LocalDate.now().getMonth().toString();
    int year = LocalDate.now().getYear();
    String dayOfWeek = LocalDate.now().getDayOfWeek().toString();
    int dayOfMonth = LocalDate.now().getDayOfMonth();
    int maxDay = LocalDate.now().lengthOfMonth();

    // EFFECTS: Constructs calendar and runs it
    public CalendarApp() {
        initializeCal();
        runCalendar();

    }

    // MODIFIES: this
    // EFFECTS: runs the CalendarApp
    // NOTE: This method has taken some inspiration from the TellerApp example
    private void runCalendar() {
        boolean active = true;
        String selection;

        while (active) {
            mainMenu();
            selection = scnr.next();
            selection = selection.toLowerCase();

            if (selection.equals("q")) {
                System.out.println("\nThank you for using the Calendar App!");
                active = false;
            } else {
                recordSelection(selection);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Initialize Cal with the current Month and Year
    private void initializeCal() {
        cal = new Calendar(month, year);
        scnr = new Scanner(System.in);
        scnr.useDelimiter("\n");
        //jsonWriter = new JsonWriter(JSON_STORE);
        calSave = new CalendarSaveReader(calData);
    }

    // EFFECTS: Displays main menu for user to select options
    private void mainMenu() {
        System.out.println("Today is: " + dayOfWeek + ", " + month + " " + dayOfMonth + ", " + year);
        System.out.println("\nPlease select one of the following options and press ENTER:");
        System.out.println("\ts -> Select a day to display");
        System.out.println("\ta -> Add an event to your calendar"); // TODO implement recurring events along with this
        System.out.println("\te -> Edit an event");
        System.out.println("\td -> Delete an event");
        System.out.println("\tl -> Load a previous calendar");
        System.out.println("\tq -> Quit");
    }


    // EFFECTS: Uses selection to implement appropriate method for what user wants
    private void recordSelection(String selection) {
        switch (selection) {
            case "s":
                showDay();
                break;
            case "a":
                addEvent();
                break;
            case "e":
                editEvent();
                break;
            case "d":
                deleteEvent();
                break;
            case "l":
                loadCalendar();
                break;
            default:
                System.out.println("Sorry! I could not process your request. Please try again... ");
                break;
        }
    }

    private void loadCalendar() {
        try {
            cal = calSave.read();
            System.out.println("\nI have loaded the calendar from the month of " + cal.getMonth()
                    + "," + cal.getYear() + " from " + calData);
        } catch (IOException e) {
            System.out.println("\nUnable to read file");
        }
    }


    // EFFECTS: Displays the day a user wishes to select
    private void showDay() {
        System.out.println("Please enter the day of the month you would like to view... ");
        int input = parseInt(scnr.next());
        if (isValidDate(input)) {
            ArrayList<Event> eventsPlanned = getEventsPlanned(input);
            System.out.println("You have selected " + month + " " + input);

            if (eventsPlanned.isEmpty()) {
                System.out.println("You don't have any events planned for this day... ");
            } else {
                System.out.println("\nYou have the following events planned for this day: ");
                for (Event e : eventsPlanned) {
                    System.out.println("\n-" + e.getTitle());
                }
            }
        } else {
            System.out.println("\nPlease enter a valid date... ");
        }
    }

    // EFFECTS: Returns a list of Events occurring on the given date
    private ArrayList<Event> getEventsPlanned(int date) {
        ArrayList<Event> listOfEventsOnDate = new ArrayList<>();
        for (Event e : cal.getListOfEvents()) {
            for (int dt : e.getListOfDays()) {
                if (date == dt) {
                    listOfEventsOnDate.add(e);
                    break;
                }
            }
        }
        return listOfEventsOnDate;

    }


    // MODIFIES: this
    // EFFECTS: Adds (initializes) a new event with a name, start/end dates, and a category
    private void addEvent() {
        String eventName = getNameInput();
        int startDate = getStartDateInput();
        int endDate = getEndDateInput();
        String category = getCategoryInput();

        try {
            Event e = new Event(eventName, startDate, endDate, category);
            cal.addEvent(e);
        } catch (InvalidCategory invalidCategory) {
            System.out.println("\nYou entered an invalid category... ");
            System.out.println("\nPlease try adding the event again... ");
            addEvent();
        } catch (StartGreaterThanEnd startGreaterThanEnd) {
            System.out.println("\nThe start date can not be greater than the end date."
                    + " Please try adding the event again... ");
            addEvent();
        } catch (GreaterThanLastDay greaterThanLastDay) {
            System.out.println("\nA date can not be greater than the last day of the month."
                    + " Please try adding the event again... ");
            addEvent();
        } catch (InvalidDates invalidDates) {
            System.out.println("\nInvalid date(s), please try adding the event again... ");
            addEvent();
        }
    }

    private String getNameInput() {
        System.out.println("\nPlease enter the name of the new event... ");
        return scnr.next();
    }

    private int getStartDateInput() {
        System.out.println("\nPlease enter the start date of the new event... ");
        return parseInt(scnr.next());
    }

    private int getEndDateInput() {
        System.out.println("\nPlease enter the end date of the new event... ");
        return parseInt(scnr.next());
    }

    private String getCategoryInput() {
        System.out.println("\nThe categories are: School, Work, Family, Personal, and Friends");
        System.out.println("\nPlease enter which category this event falls into... ");
        return scnr.next().toLowerCase();
    }

    //EFFECTS: Returns true if the given date is valid (Date is non-zero and non-negative and
    //         the date does not exceed the length of the month)
    private Boolean isValidDate(int date) {
        return (date > 0 && date <= maxDay);
    }


    // EFFECTS: Finds the event that the user wishes to edit
    private void editEvent() {
        String name;
        int initStart;
        int initEnd;
        Event eventToEdit;
        System.out.println("\nPlease enter the name of the event you would like to edit... ");
        name = scnr.next();
        if (cal.isThereSimilarEvent(name)) {
            System.out.println("\nThere are multiple events with that name. "
                    + "Please enter the start date of the event you would like to edit... ");
            initStart = parseInt(scnr.next());
            System.out.println("\nPlease enter the end date of the event you would like to edit... ");
            initEnd = parseInt(scnr.next());
            eventToEdit = cal.getEvent(name, initStart, initEnd);
        } else {
            eventToEdit = cal.getEvent(name);
        }
        editEventIfFound(eventToEdit);
    }


    // EFFECTS: If the event is found direct the user to the edit menu, if not, then display error message
    private void editEventIfFound(Event e) {
        if (eventNotFound(e)) {
            System.out.println("\nSorry I could not find the event you were looking for... ");
        } else {
            editSelections(e);
        }
    }

    // EFFECTS: Returns true if event is not found (event == null)
    private Boolean eventNotFound(Event e) {
        return (e == null);
    }

    // EFFECTS: User selects one of the 3 options for editing the event
    private void editSelections(Event e) {
        String selection;
        System.out.println("\nPlease select from the following options and press ENTER... ");
        System.out.println("\tn -> Edit the name of this event");
        System.out.println("\td -> Edit the start and end dates of this event");
        System.out.println("\tc -> Change the category of this event");
        selection = scnr.next();

        switch (selection) {
            case "n":
                changeName(e);
                break;
            case "d":
                changeDates(e);
                break;
            case "c":
                changeCat(e);
                break;
            default:
                System.out.println("\nPlease enter a valid edit... ");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: Asks user for a new name to replace with the old name of the event
    public void changeName(Event e) {
        String newName;
        System.out.println("\nWhat would you like to use for the new name? ");
        newName = scnr.next();
        e.setTitle(newName);
        System.out.println("\nThe event has been updated!");
    }

    // MODIFIES: this
    // EFFECTS: changes the start/end dates of the given event based on user choice
    public void changeDates(Event e) {
        int newStart;
        int newEnd;
        System.out.println("\nWhat is the new start date for this event? ");
        newStart = parseInt(scnr.next());
        System.out.println("\nWhat is the new end date for this event? ");
        newEnd = parseInt(scnr.next());

        try {
            e.setDates(newStart, newEnd);
            System.out.println("\nThe event has been updated!");
        } catch (StartGreaterThanEnd startGreaterThanEnd) {
            System.out.println("\nThe start date is greater than the end date. Please enter the dates again... ");
            changeDates(e);
        } catch (GreaterThanLastDay greaterThanLastDay) {
            System.out.println("\nYou can not choose an start/end date that is greater than the last day of the month"
                    + ". Please try entering the dates again... ");
            changeDates(e);
        } catch (InvalidDates invalidDates) {
            System.out.println("\nInvalid dates! Please try entering the dates again... ");
            changeDates(e);
        }
    }

    // MODIFIES: this
    // EFFECTS: Changes the category of the event
    public void changeCat(Event e) {
        String newCat;
        System.out.println("\nPlease enter the new category of this event... ");
        newCat = scnr.next();
        try {
            e.setCategory(newCat);
        } catch (InvalidCategory invalidCategory) {
            System.out.println("\nYou entered an invalid category. Please enter the category again... ");
            changeCat(e);
        }

    }

    // MODIFIES: this
    // EFFECTS: Deletes the event the user selects
    public void deleteEvent() {
        String name;
        int initStart;
        int initEnd;
        Event eventToDelete;
        System.out.println("\nPlease enter the name of the event you would like to delete... ");
        name = scnr.next();
        if (cal.isThereSimilarEvent(name)) {
            System.out.println("\nThere are multiple events with that name. "
                    + "Please enter the start date of the event you would like to delete... ");
            initStart = parseInt(scnr.next());
            System.out.println("\nPlease enter the end date of the event you would like to delete... ");
            initEnd = parseInt(scnr.next());
            eventToDelete = cal.getEvent(name, initStart, initEnd);
        } else {
            eventToDelete = cal.getEvent(name);
        }
        try {
            cal.deleteEvent(eventToDelete);
            System.out.println("\nThe event has been deleted... ");
        } catch (CanNotFindEvent canNotFindEvent) {
            System.out.println("\nCan not find the event to delete. Please try again");
        }
    }


}






