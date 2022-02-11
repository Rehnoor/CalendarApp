package ui;

import model.Calendar;
import model.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;


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
    // TODO TIME PERMITTING, CONVERT TO SWITCH STATEMENT
    private void recordSelection(String selection) {
        if (selection.equals("s")) {
            showDay();
        } else if (selection.equals("a")) {
            addEvent();
        } else if (selection.equals("e")) {
            //editEvent();
        } else if (selection.equals("d")) {
            //deleteEvent();
        } else {
            System.out.println("Sorry! I could not process your request. Please try again... ");
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


    // MODIFIES: cal
    // EFFECTS: Adds (initializes) a new event with a name, start/end dates, and a category
    // TODO TIME PERMITTING ADD HELPER METHODS FOR THIS
    private void addEvent() {
        String eventName;
        int startDate;
        int endDate;
        String category;

        System.out.println("\nPlease enter the name of the new event... ");
        eventName = scnr.next();

        System.out.println("\nPlease enter the start date of the new event... ");
        startDate = parseInt(scnr.next());
        while (!isValidDate(startDate)) {
            System.out.println("\nPlease enter a valid date... ");
            startDate = parseInt(scnr.next());
        }

        System.out.println("\nPlease enter the end date of the new event... ");
        endDate = parseInt(scnr.next());
        while (!isValidDate(endDate)) {
            System.out.println("\nPlease enter a valid date... ");
            endDate = parseInt(scnr.next());
        }

        System.out.println("\nThe categories are: School, Work, Family, Personal, and Friends");
        System.out.println("\nPlease enter which category this event falls into... ");
        category = scnr.next();
        while (!isValidCategory(category)) {
            System.out.println("\nPlease enter a valid category... ");
            category = scnr.next();
        }

        Event e = new Event(eventName, startDate, endDate, category);
        cal.addEvent(e);
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
}






