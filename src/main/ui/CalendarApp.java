package ui;

import exceptions.*;
import model.Calendar;
import model.Event;
import persistence.CalendarSaveReader;
import persistence.CalendarSaveWriter;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// This class runs and implements all parts of the Calendar application. The functions include: initializing the month,
// any necessary constants, and adding, deleting, and editing events
public class CalendarApp extends JFrame {
    private static final String calData = "./data/calendar.json";
    private Calendar cal;
    private Scanner scnr;
    private CalendarSaveReader calReader;
    private CalendarSaveWriter calSaver;
    private ArrayList<JButton> listOfButtons = new ArrayList<>();

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    //Constants
    String month = LocalDate.now().getMonth().toString();
    int year = LocalDate.now().getYear();
    String dayOfWeek = LocalDate.now().getDayOfWeek().toString();
    int dayOfMonth = LocalDate.now().getDayOfMonth();
    int maxDay = LocalDate.now().lengthOfMonth();
    LocalDate today = LocalDate.of(year, LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
    LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
    String dayOfWeekOfFirstDay = firstDayOfMonth.getDayOfWeek().toString();

    // EFFECTS: Initializes calendar and runs it
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
                recordSelectionFirstMenu(selection);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Initialize Cal with the current Month and Year and initialize the GUI
    private void initializeCal() {
        cal = new Calendar(month, year);
        scnr = new Scanner(System.in);
        scnr.useDelimiter("\n");
        calSaver = new CalendarSaveWriter(calData);
        calReader = new CalendarSaveReader(calData);

        initializeGUI();
    }

    // MODIFIES: this
    // EFFECTS: Creates a Calendar GUI
    private void initializeGUI() {

        JPanel panel = new JPanel();
        panel.setSize(WIDTH, HEIGHT);
        panel.setBorder(BorderFactory.createTitledBorder(month));
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("CalendarApp");
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setUpDaysOfWeek(panel);
        setUpDates(panel);
        panel.setLayout(new GridLayout(6, 7, 5, 2));
    }

    // MODIFIES: this
    // EFFECTS: Creates buttons for each date
    private void setUpDates(JPanel panel) {
        int day = firstDayOfMonth.getDayOfMonth();
        setUpEmptySpaceBeforeFirstDay(panel);
        while (day <= maxDay) {
            JButton button = new JButton(Integer.toString(day));
            listOfButtons.add(button);
            panel.add(button);
            day++;
        }
    }

    // MODIFIES: this
    // EFFECTS: Determines amount of "empty" day slots before first day of month
    //          and adds it
    private void setUpEmptySpaceBeforeFirstDay(JPanel panel) {
        int numEmpty = 0;
        switch (dayOfWeekOfFirstDay) {
            case "MONDAY": numEmpty = 1;
                break;
            case "TUESDAY": numEmpty = 2;
                break;
            case "WEDNESDAY": numEmpty = 3;
                break;
            case "THURSDAY": numEmpty = 4;
                break;
            case "FRIDAY": numEmpty = 5;
                break;
            case "SATURDAY": numEmpty = 6;
                break;
            default:
                break;
        }
        addEmptySpace(numEmpty, panel);
    }

    // MODIFIES: this
    // EFFECTS: Adds empty space to the panel
    private void addEmptySpace(int numEmpty, JPanel panel) {
        while (numEmpty > 0) {
            JButton button = new JButton();
            panel.add(button);
            numEmpty--;
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds day of week labels on top of each calendar column
    private void setUpDaysOfWeek(JPanel panel) {
        JLabel sundayLabel = new JLabel("SUN");
        sundayLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
        panel.add(sundayLabel);

        JLabel mondayLabel = new JLabel("MON");
        mondayLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
        panel.add(mondayLabel);

        JLabel tuesdayLabel = new JLabel("TUES");
        tuesdayLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
        panel.add(tuesdayLabel);

        JLabel wednesdayLabel = new JLabel("WED");
        wednesdayLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
        panel.add(wednesdayLabel);

        JLabel thursdayLabel = new JLabel("THURS");
        thursdayLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
        panel.add(thursdayLabel);

        JLabel fridayLabel = new JLabel("FRI");
        fridayLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
        panel.add(fridayLabel);

        JLabel saturdayLabel = new JLabel("SAT");
        saturdayLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
        panel.add(saturdayLabel);
    }


    // EFFECTS: Displays main menu for user to select options
    private void mainMenu() {
        System.out.println("Today is: " + dayOfWeek + ", " + month + " " + dayOfMonth + ", " + year);
        System.out.println("\nPlease select one of the following options and press ENTER:");
        System.out.println("\ts -> Select a day to display");
        System.out.println("\ta -> Add an event to your calendar");
        System.out.println("\te -> Edit an event");
        System.out.println("\td -> Delete an event");
        System.out.println("\tl -> Load a previous calendar");
        System.out.println("\t* -> Save your current calendar");
        System.out.println("\t& -> Display Calendar");
        System.out.println("\tq -> Quit");
    }


    // EFFECTS: Uses selection to use appropriate method for what the user wants to do
    private void recordSelectionFirstMenu(String selection) {
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
                recordSelectionSecondMenu(selection);
                break;
        }
    }

    // EFFECTS: Second menu for selection to get around checkstyle issue.
    private void recordSelectionSecondMenu(String selection) {
        switch (selection) {
            case "l":
                loadCalendar();
                break;
            case "*":
                saveCalendar();
                break;
            case "&":
                displayCalendar();
                break;
        }
    }

    // EFFECTS: Saves the current state of the CalendarApp to ./data/calendar.json
    // This method has taken inspiration from the saveWorkRoom method in the WorkRoomApp class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void saveCalendar() {
        try {
            calSaver.open();
            calSaver.write(cal);
            calSaver.close();
            System.out.println("\nYour calendar has been saved to " + calData);
        } catch (IOException e) {
            System.out.println("\nYour hard drive is corrupted...HAHA JK I couldn't find the save file... ");
        }
    }

    // MODIFIES: cal
    // EFFECTS: Loads any saved Calendar data from ./data/calendar.json
    // This method has taken inspiration from the loadWorkRoom method in the WorkRoomApp class in
    private void loadCalendar() {
        try {
            cal = calReader.read();
            System.out.println("\nI have loaded the calendar from the month of " + cal.getMonth()
                    + "," + cal.getYear() + " from " + calData);
            displayCalendar();
        } catch (IOException e) {
            System.out.println("\nUnable to read file");
        }
    }

    // EFFECTS: Displays current calendar
    private void displayCalendar() {
        System.out.println("\n" + month + " " + year + ":");
        if (cal.getListOfEvents().isEmpty()) {
            System.out.println("\nYou have no events planned... ");
        } else {
            for (Event e : cal.getListOfEvents()) {
                System.out.println("_________________");
                System.out.println("\t" + "Title: " + e.getTitle());
                System.out.println("\t" + "Start Date: " + e.getStartDate());
                System.out.println("\t" + "End Date: " + e.getEndDate());
                System.out.println("\t" + "Category: " + e.getCategory());
            }
        }
    }


    // EFFECTS: Display the events planned for a valid date entered. If the date is not valid,
    // tell the user to enter a valid date
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
            showDay();
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

    // EFFECTS: Helper method that collects a name input
    private String getNameInput() {
        System.out.println("\nPlease enter the name of the new event... ");
        return scnr.next();
    }

    // EFFECTS: Helper method that collects a start date input
    private int getStartDateInput() {
        System.out.println("\nPlease enter the start date of the new event... ");
        return parseInt(scnr.next());
    }

    // EFFECTS: Helper method that collects an end date input
    private int getEndDateInput() {
        System.out.println("\nPlease enter the end date of the new event... ");
        return parseInt(scnr.next());
    }

    // EFFECTS: Helper method that collects a category input
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


    // EFFECTS: Finds the event that the user wishes to edit. If there is a similarly named event to the one entered,
    //          determine which one the user wishes to edit
    private void editEvent() {
        String name;
        int initStart;
        int initEnd;
        Event eventToEdit;
        System.out.println("\nPlease enter the name of the event you would like to edit... ");
        name = scnr.next();
        if (cal.isEventOnCalendar(name)) {
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
            editSelections(eventToEdit);
        } else {
            System.out.println("\nI can not find the event you are looking for... ");
        }
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






