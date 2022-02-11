package ui;

import model.Calendar;
import model.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// This class runs and implements all parts of the Calendar application. The funtions include: initializing the month,
// any necessary constants, and adding, deleting, and editing events
public class CalendarApp {
    private Calendar cal;
    private Scanner scnr;

    //Constants
    String month = LocalDate.now().getMonth().toString();
    int year = LocalDate.now().getYear();
    String dayOfWeek = LocalDate.now().getDayOfWeek().toString();
    int dayOfMonth = LocalDate.now().getDayOfMonth();
    int maxDay = LocalDate.now().lengthOfMonth();

    // EFFECTS: Constructor for the CalendarApp class
    public CalendarApp() {
        runCalendar();
    }

    // MODIFIES: this
    // EFFECTS: runs the CalendarApp
    // NOTE: This method has taken some inspiration from the TellerApp example
    private void runCalendar() {
        boolean active = true;
        String selection;
        initializeCal();

        while (active) {
            mainMenu();
            selection = scnr.next();
            selection = selection.toLowerCase();

            if (selection.equals("q")) {
                active = false;
            } else {
                recordSelection(selection);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Initialize Cal with the current Month and Year
    private void initializeCal() {
        cal = new model.Calendar(month, year);
        scnr = new Scanner(System.in);
        scnr.useDelimiter("\n");
    }

    // EFFECTS: Displays main menu for user to select options
    private void mainMenu() {
        System.out.println("Today is: " + dayOfWeek + ", " + month + " " + dayOfMonth + ", " + year);
        System.out.println("\nPlease select one of the following options and press ENTER:");
        System.out.println("\ts -> Select a day to display");
        System.out.println("\ta -> Add an event to your calendar"); // TODO implement recurring events along with this
        System.out.println("\te -> Edit an event");
        System.out.println("\td -> Delete an event");
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
            default:
                System.out.println("Sorry! I could not process your request. Please try again... ");
                break;
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
        String eventName;
        int startDate;
        int endDate;
        String category;
        System.out.println("\nPlease enter the name of the new event... ");
        eventName = scnr.next();

        System.out.println("\nPlease enter the start date of the new event... ");
        startDate = ensureValidDate(parseInt(scnr.next()));

        System.out.println("\nPlease enter the end date of the new event... ");
        endDate = ensureValidDate(parseInt(scnr.next()));

        if (startDate > endDate) {
            System.out.println("\nInvalid dates. The start date can not be greater than the end date... ");
        } else {
            System.out.println("\nThe categories are: School, Work, Family, Personal, and Friends");
            System.out.println("\nPlease enter which category this event falls into... ");
            category = ensureValidCategory(scnr.next());

            Event e = new Event(eventName, startDate, endDate, category);
            cal.addEvent(e);
        }
    }

    // EFFECTS: Ensures that the date entered by the user is valid. If not,
    //          ask user to enter valid date then return the valid date
    private int ensureValidDate(int enteredDate) {
        while (!isValidDate(enteredDate)) {
            System.out.println("\nPlease enter a valid date... ");
            enteredDate = parseInt(scnr.next());
        }
        return enteredDate;
    }

    // EFFECTS: Ensures that the category entered by the user is valid. If not,
    //          ask user to enter valid category then return it
    private String ensureValidCategory(String cat) {
        while (!isValidCategory(cat)) {
            System.out.println("\nPlease enter a valid category... ");
            cat = scnr.next();
        }
        return cat;
    }


    //EFFECTS: Returns true if the given date is valid (Date is non-zero and non-negative and
    //         the date does not exceed the length of the month)
    private Boolean isValidDate(int date) {
        return (date > 0 && date <= maxDay);
    }

    // EFFECTS: Returns true if the given category is valid
    //          That is, if the category is: School, Work, Family, Personal, or Friends return true
    //          else, return false
    private Boolean isValidCategory(String cat) {
        return (cat.equals("School") || cat.equals("Work") || cat.equals("Personal") || cat.equals("Friends"));
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

        if (newStart > newEnd) {
            System.out.println("\nThe start date can not be after the end date. Please try again... ");
        } else {
            e.setDates(newStart, newEnd);
            System.out.println("\nThe event has been updated!");
        }
    }

    // MODIFIES: this
    // EFFECTS: Changes the category of the event
    public void changeCat(Event e) {
        String newCat;
        System.out.println("\nPlease enter the new category of this event... ");
        newCat = scnr.next();

        if (isValidCategory(newCat)) {
            e.setCategory(newCat);
        } else {
            System.out.println("\nSorry that was an invalid category. Please try again... ");
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
        cal.deleteEvent(eventToDelete);
    }

}






