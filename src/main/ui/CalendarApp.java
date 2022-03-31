package ui;

import exceptions.*;
import model.Calendar;
import model.CalendarEvent;
import model.EventLog;
import model.Event;
import persistence.CalendarSaveReader;
import persistence.CalendarSaveWriter;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

// This class runs and implements all parts of the Calendar application. The functions include: initializing the month,
// any necessary constants, and adding, deleting, and editing events
public class CalendarApp extends JFrame {
    private static final String calData = "./data/calendar.json";
    private Calendar cal;
    private Scanner scnr;
    private CalendarSaveReader calReader;
    private CalendarSaveWriter calSaver;
    private ArrayList<Day> listOfDays = new ArrayList<>();
    private ArrayList<JLabel> listOfEmptyDays = new ArrayList<>();


    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    //Constants
    String month = LocalDate.now().getMonth().toString();
    int year = LocalDate.now().getYear();
    String dayOfWeek = LocalDate.now().getDayOfWeek().toString();
    int dayOfMonth = LocalDate.now().getDayOfMonth();
    int maxDay = LocalDate.now().lengthOfMonth();
    LocalDate today = LocalDate.of(year, LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
    LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
    String dayOfWeekOfFirstDay = firstDayOfMonth.getDayOfWeek().toString();
    int currentHourOfDay = LocalTime.now().getHour();

    JPanel panel = new JPanel();

    // EFFECTS: Initializes calendar and runs it
    public CalendarApp() {
        initializeCal(); // for GUI program
        //runCalendar(); // just for console based program
    }


    // MODIFIES: this
    // EFFECTS: runs the CalendarApp
    // NOTE: This method has taken some inspiration from the TellerApp example
//    private void runCalendar() {
//        boolean active = true;
//        String selection;
//
//        while (active) {
//            mainMenu();
//            selection = scnr.next();
//            selection = selection.toLowerCase();
//
//            if (selection.equals("q")) {
//                System.out.println("\nThank you for using the Calendar App!");
//                active = false;
//            } else {
//                recordSelectionFirstMenu(selection);
//            }
//        }
//    }

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
    // EFFECTS: Adds the menu bar with a menu for adding events, saving, and loading the calendar
    private void addMenu() {
        JMenuBar options = new JMenuBar();

        JMenu saveLoadMenu = new JMenu("Calendar");
        saveLoadMenu.add(new JMenuItem(new SaveCalendarEvent()));
        saveLoadMenu.add(new JMenuItem(new LoadCalendarEvent()));
        options.add(saveLoadMenu);

        JMenu addEventMenu = new JMenu("Add Event");
        addEventMenu.add(new JMenuItem(new AddFamilyEvent()));
        addEventMenu.add(new JMenuItem(new AddFriendsEvent()));
        addEventMenu.add(new JMenuItem(new AddPersonalEvent()));
        addEventMenu.add(new JMenuItem(new AddSchoolEvent()));
        addEventMenu.add(new JMenuItem(new AddWorkEvent()));
        options.add(addEventMenu);

        JMenu showCoolPicture = new JMenu("Cool picture");
        showCoolPicture.add(new JMenuItem(new ShowCoolPicture()));
        options.add(showCoolPicture);

        setJMenuBar(options);

    }


    // MODIFIES: this
    // EFFECTS: Creates a Calendar GUI
    private void initializeGUI() {
        panel.setSize(WIDTH, HEIGHT);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 3),
                month + " " + year, 0, 0,
                new Font("Helvetica Neue", Font.BOLD, 20)));
        panel.setBackground(Color.white);

        add(panel);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event loggedEvent: EventLog.getInstance()) {
                    System.out.println(loggedEvent.getDescription());
                }
                System.exit(0);
            }
        });
        setTitle("CalendarApp");
        setSize(WIDTH, HEIGHT);
        setUpDaysOfWeek();
        addDateBoxes();

        panel.setLayout(new GridLayout(6, 7, 5, 2));
        addMenu();
        setVisible(true);
    }


    // MODIFIES: this
    // EFFECTS: Creates Jlabel boxes for each date
    private void addDateBoxes() {
        int day = 1;
        setUpEmptySpaceBeforeFirstDay();
        while (day <= maxDay) {
            Day daySlot = new Day(Integer.toString(day));
            daySlot.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            daySlot.setHorizontalAlignment(SwingConstants.RIGHT);
            daySlot.setVerticalAlignment(SwingConstants.TOP);
            if (day == dayOfMonth) {
                daySlot.setForeground(Color.red);
            }
            listOfDays.add(daySlot);
            panel.add(daySlot);
            day++;
        }

    }


    // MODIFIES: this
    // EFFECTS: Removes old calendar information and updates it with new information
    private void updateDateBoxes() {
        setVisible(true);
        panel.removeAll();
        setUpDaysOfWeek();
        setUpEmptySpaceBeforeFirstDay();
        for (Day day : listOfDays) {
            panel.add(day);
        }
        setVisible(true);
    }


    // MODIFIES: this
    // EFFECTS: Determines amount of "empty" day slots before first day of month
    //          and adds it
    private void setUpEmptySpaceBeforeFirstDay() {
        int numEmpty = 0;
        switch (dayOfWeekOfFirstDay) {
            case "MONDAY": numEmpty = 1;
                break;
            case "TUESDAY":
                numEmpty = 2;
                break;
            case "WEDNESDAY":
                numEmpty = 3;
                break;
            case "THURSDAY":
                numEmpty = 4;
                break;
            case "FRIDAY":
                numEmpty = 5;
                break;
            case "SATURDAY":
                numEmpty = 6;
                break;
            default:
                break;
        }
        addEmptySpace(numEmpty);
    }

    // MODIFIES: this
    // EFFECTS: Adds empty space to the panel
    private void addEmptySpace(int numEmpty) {
        while (numEmpty > 0) {
            JLabel label = new JLabel();
            label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            panel.add(label);
            listOfEmptyDays.add(label);
            numEmpty--;
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds day of week labels on top of each calendar column
    private void setUpDaysOfWeek() {
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


//    // EFFECTS: Displays main menu for user to select options
//    private void mainMenu() {
//        System.out.println("Today is: " + dayOfWeek + ", " + month + " " + dayOfMonth + ", " + year);
//        System.out.println("\nPlease select one of the following options and press ENTER:");
//        System.out.println("\ts -> Select a day to display");
//        System.out.println("\ta -> Add an event to your calendar");
//        System.out.println("\te -> Edit an event");
//        System.out.println("\td -> Delete an event");
//        System.out.println("\tl -> Load a previous calendar");
//        System.out.println("\t* -> Save your current calendar");
//        System.out.println("\t& -> Display Calendar");
//        System.out.println("\tq -> Quit");
//    }


    // EFFECTS: Uses selection to use appropriate method for what the user wants to do
//    private void recordSelectionFirstMenu(String selection) {
//        switch (selection) {
//            case "s":
//                showDay();
//                break;
//            case "a":
//                addEvent();
//                break;
//            case "e":
//                editEvent();
//                break;
//            case "d":
//                deleteEvent();
//                break;
//            default:
//                recordSelectionSecondMenu(selection);
//                break;
//        }
//    }

    // EFFECTS: Second menu for selection to get around checkstyle issue.
//    private void recordSelectionSecondMenu(String selection) {
//        switch (selection) {
//            case "l":
//                loadCalendar();
//                break;
//            case "*":
//                saveCalendar();
//                break;
//            case "&":
//                displayCalendar();
//                break;
//        }
//    }

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
            //displayCalendar();
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
            for (CalendarEvent e : cal.getListOfEvents()) {
                System.out.println("_________________");
                System.out.println("\t" + "Title: " + e.getTitle());
                System.out.println("\t" + "Start Date: " + e.getStartDate());
                System.out.println("\t" + "End Date: " + e.getEndDate());
                System.out.println("\t" + "Category: " + e.getCategory());
            }
        }
    }


//    // EFFECTS: Display the events planned for a valid date entered. If the date is not valid,
//    // tell the user to enter a valid date
//    private void showDay() {
//        System.out.println("Please enter the day of the month you would like to view... ");
//        int input = parseInt(scnr.next());
//        if (isValidDate(input)) {
//            ArrayList<Event> eventsPlanned = getEventsPlanned(input);
//            System.out.println("You have selected " + month + " " + input);
//
//            if (eventsPlanned.isEmpty()) {
//                System.out.println("You don't have any events planned for this day... ");
//            } else {
//                System.out.println("\nYou have the following events planned for this day: ");
//                for (Event e : eventsPlanned) {
//                    System.out.println("\n-" + e.getTitle());
//                }
//            }
//        } else {
//            System.out.println("\nPlease enter a valid date... ");
//            showDay();
//        }
//    }
//
//    // EFFECTS: Returns a list of Events occurring on the given date
//    private ArrayList<Event> getEventsPlanned(int date) {
//        ArrayList<Event> listOfEventsOnDate = new ArrayList<>();
//        for (Event e : cal.getListOfEvents()) {
//            for (int dt : e.getListOfDays()) {
//                if (date == dt) {
//                    listOfEventsOnDate.add(e);
//                    break;
//                }
//            }
//        }
//        return listOfEventsOnDate;
//
//    }


    // MODIFIES: this
    // EFFECTS: Adds (initializes) a new event with a name, start/end dates, and a category
//    private void addEvent() {
//        String eventName = getNameInput();
//        int startDate = getStartDateInput();
//        int endDate = getEndDateInput();
//        String category = getCategoryInput();
//
//        try {
//            Event e = new Event(eventName, startDate, endDate, category);
//            cal.addEvent(e);
//        } catch (InvalidCategory invalidCategory) {
//            System.out.println("\nYou entered an invalid category... ");
//            System.out.println("\nPlease try adding the event again... ");
//            addEvent();
//        } catch (StartGreaterThanEnd startGreaterThanEnd) {
//            System.out.println("\nThe start date can not be greater than the end date."
//                    + " Please try adding the event again... ");
//            addEvent();
//        } catch (GreaterThanLastDay greaterThanLastDay) {
//            System.out.println("\nA date can not be greater than the last day of the month."
//                    + " Please try adding the event again... ");
//            addEvent();
//        } catch (InvalidDates invalidDates) {
//            System.out.println("\nInvalid date(s), please try adding the event again... ");
//            addEvent();
//        }
//    }

    // EFFECTS: Helper method that collects a name input
//    private String getNameInput() {
//        System.out.println("\nPlease enter the name of the new event... ");
//        return scnr.next();
//    }

    // EFFECTS: Helper method that collects a start date input
//    private int getStartDateInput() {
//        System.out.println("\nPlease enter the start date of the new event... ");
//        return parseInt(scnr.next());
//    }

    // EFFECTS: Helper method that collects an end date input
//    private int getEndDateInput() {
//        System.out.println("\nPlease enter the end date of the new event... ");
//        return parseInt(scnr.next());
//    }

    // EFFECTS: Helper method that collects a category input
//    private String getCategoryInput() {
//        System.out.println("\nThe categories are: School, Work, Family, Personal, and Friends");
//        System.out.println("\nPlease enter which category this event falls into... ");
//        return scnr.next().toLowerCase();
//    }

    //EFFECTS: Returns true if the given date is valid (Date is non-zero and non-negative and
    //         the date does not exceed the length of the month)
//    private Boolean isValidDate(int date) {
//        return (date > 0 && date <= maxDay);
//    }


    // EFFECTS: Finds the event that the user wishes to edit. If there is a similarly named event to the one entered,
    //          determine which one the user wishes to edit
//    private void editEvent() {
//        String name;
//        int initStart;
//        int initEnd;
//        Event eventToEdit;
//        System.out.println("\nPlease enter the name of the event you would like to edit... ");
//        name = scnr.next();
//        if (cal.isEventOnCalendar(name)) {
//            if (cal.isThereSimilarEvent(name)) {
//                System.out.println("\nThere are multiple events with that name. "
//                        + "Please enter the start date of the event you would like to edit... ");
//                initStart = parseInt(scnr.next());
//                System.out.println("\nPlease enter the end date of the event you would like to edit... ");
//                initEnd = parseInt(scnr.next());
//                eventToEdit = cal.getEvent(name, initStart, initEnd);
//            } else {
//                eventToEdit = cal.getEvent(name);
//            }
//            editSelections(eventToEdit);
//        } else {
//            System.out.println("\nI can not find the event you are looking for... ");
//        }
//    }


    // EFFECTS: User selects one of the 3 options for editing the event
//    private void editSelections(Event e) {
//        String selection;
//        System.out.println("\nPlease select from the following options and press ENTER... ");
//        System.out.println("\tn -> Edit the name of this event");
//        System.out.println("\td -> Edit the start and end dates of this event");
//        System.out.println("\tc -> Change the category of this event");
//        selection = scnr.next();
//
//        switch (selection) {
//            case "n":
//                changeName(e);
//                break;
//            case "d":
//                changeDates(e);
//                break;
//            case "c":
//                changeCat(e);
//                break;
//            default:
//                System.out.println("\nPlease enter a valid edit... ");
//                break;
//        }
//    }

    // MODIFIES: this
    // EFFECTS: Asks user for a new name to replace with the old name of the event
//    public void changeName(Event e) {
//        String newName;
//        System.out.println("\nWhat would you like to use for the new name? ");
//        newName = scnr.next();
//        e.setTitle(newName);
//        System.out.println("\nThe event has been updated!");
//    }

    // MODIFIES: this
    // EFFECTS: changes the start/end dates of the given event based on user choice
//    public void changeDates(Event e) {
//        int newStart;
//        int newEnd;
//        System.out.println("\nWhat is the new start date for this event? ");
//        newStart = parseInt(scnr.next());
//        System.out.println("\nWhat is the new end date for this event? ");
//        newEnd = parseInt(scnr.next());
//
//        try {
//            e.setDates(newStart, newEnd);
//            System.out.println("\nThe event has been updated!");
//        } catch (StartGreaterThanEnd startGreaterThanEnd) {
//            System.out.println("\nThe start date is greater than the end date. Please enter the dates again... ");
//            changeDates(e);
//        } catch (GreaterThanLastDay greaterThanLastDay) {
//            System.out.println("\nYou can not choose an start/end date that is greater than the last day of the month"
//                    + ". Please try entering the dates again... ");
//            changeDates(e);
//        } catch (InvalidDates invalidDates) {
//            System.out.println("\nInvalid dates! Please try entering the dates again... ");
//            changeDates(e);
//        }
//    }

    // MODIFIES: this
    // EFFECTS: Changes the category of the event
//    public void changeCat(Event e) {
//        String newCat;
//        System.out.println("\nPlease enter the new category of this event... ");
//        newCat = scnr.next();
//        try {
//            e.setCategory(newCat);
//        } catch (InvalidCategory invalidCategory) {
//            System.out.println("\nYou entered an invalid category. Please enter the category again... ");
//            changeCat(e);
//        }
//
//    }

    // MODIFIES: this
    // EFFECTS: Deletes the event the user selects
//    public void deleteEvent() {
//        String name;
//        int initStart;
//        int initEnd;
//        Event eventToDelete;
//        System.out.println("\nPlease enter the name of the event you would like to delete... ");
//        name = scnr.next();
//        if (cal.isThereSimilarEvent(name)) {
//            System.out.println("\nThere are multiple events with that name. "
//                    + "Please enter the start date of the event you would like to delete... ");
//            initStart = parseInt(scnr.next());
//            System.out.println("\nPlease enter the end date of the event you would like to delete... ");
//            initEnd = parseInt(scnr.next());
//            eventToDelete = cal.getEvent(name, initStart, initEnd);
//        } else {
//            eventToDelete = cal.getEvent(name);
//        }
//        try {
//            cal.deleteEvent(eventToDelete);
//            System.out.println("\nThe event has been deleted... ");
//        } catch (CanNotFindEvent canNotFindEvent) {
//            System.out.println("\nCan not find the event to delete. Please try again");
//        }
//    }


    // EFFECTS: adds event with name, start, end, and category to the calendar and GUI
    private void addEventToGUI(CalendarEvent e) {
        switch (e.getCategory()) {
            case "family":
                addEventButtons(e, Color.RED);
                break;
            case "friends":
                addEventButtons(e, Color.MAGENTA);
                break;
            case "personal":
                addEventButtons(e, Color.GREEN);
                break;
            case "school":
                addEventButtons(e, Color.BLUE);
                break;
            default:
                addEventButtons(e, Color.ORANGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: Add buttons to the day labels corresponding to the new event
    //          and create the new event
    private void addEventButtons(CalendarEvent e, Color clr) {
        int counter = 1;
        int start = e.getStartDate();
        int end = e.getEndDate();
        String name = e.getTitle();

        ArrayList<Day> newListOfDays = new ArrayList<>();
        for (Day day : listOfDays) {
            if (counter >= start && counter <= end) {
                JButton btn = new JButton(name);
                btn.setOpaque(true);
                btn.setBackground(clr);
                day.add(btn);
                day.addToListOfEventButtons(btn);
                btn.addMouseListener(new DateClick(btn, e));
                day.setLayout(new BoxLayout(day, BoxLayout.PAGE_AXIS));
//                 }
                //day.setComponentPopupMenu(rightClickMenu);

            }
            counter++;
            newListOfDays.add(day);
        }
        listOfDays = newListOfDays;
        updateDateBoxes();
    }


    // This abstract class handles the add menu and is implemented depending on which category the user selects
    private abstract class AddEvent extends AbstractAction {
        String cat;

        // text fields for getting user input
        JTextField nameField = new JTextField(5);
        JTextField startDateField = new JTextField(5);
        JTextField endDateField = new JTextField(5);


        // EFFECTS: Creates menu option with name cat and stores cat as a field
        AddEvent(String cat) {
            super(cat);
            this.cat = cat;
        }

        // MODIFIES: CalendarApp
        // EFFECTS: Creates a multi-response pop-up window that gets the event name, and start/end dates from user
        //          to create a new event
        // This method was created with help from:
        // https://stackoverflow.com/questions/6555040/multiple-input-in-joptionpane-showinputdialog
        @Override
        public void actionPerformed(ActionEvent e) {
            JPanel addEventPanel = new JPanel();
            setUpAddMenu(addEventPanel);
            int result = JOptionPane.showConfirmDialog(null, addEventPanel,
                    "Please enter event details", JOptionPane.OK_CANCEL_OPTION);
            String eventName = nameField.getText();
            int startDate = parseInt(startDateField.getText());
            int endDate = parseInt(endDateField.getText());
            if (result == JOptionPane.OK_OPTION) {
                try {
                    CalendarEvent evnt = new CalendarEvent(eventName, startDate, endDate, cat);
                    cal.addEvent(evnt);
                    addEventToGUI(evnt);
                } catch (InvalidCategory ex) {
                    ex.printStackTrace();
                } catch (InvalidDates ex) {
                    JOptionPane.showMessageDialog(null, "INVALID INPUT FOR DATE(S)");
                }
            }


        }

        private void setUpAddMenu(JPanel pnl) {
            pnl.add(new JLabel("Event Name:"));
            pnl.add(nameField);
            pnl.add(Box.createHorizontalStrut(15)); // a spacer
            pnl.add(new JLabel("Start Date:"));
            pnl.add(startDateField);
            pnl.add(Box.createHorizontalStrut(15)); // a spacer
            pnl.add(new JLabel("End Date:"));
            pnl.add(endDateField);
        }


    }


    private class AddFamilyEvent extends AddEvent {

        AddFamilyEvent() {
            super("family");
        }

    }

    private class AddFriendsEvent extends AddEvent {

        AddFriendsEvent() {
            super("friends");
        }
    }

    private class AddPersonalEvent extends AddEvent {

        AddPersonalEvent() {
            super("personal");
        }
    }

    private class AddSchoolEvent extends AddEvent {

        AddSchoolEvent() {
            super("school");
        }
    }

    private class AddWorkEvent extends AddEvent {

        AddWorkEvent() {
            super("work");
        }
    }

    private class ShowCoolPicture extends AbstractAction {
        public ShowCoolPicture() {
            super("Cool picture");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ImageIcon background = new ImageIcon("./data/IMG_1165.jpg");
            Image bg = background.getImage().getScaledInstance(500, 750, java.awt.Image.SCALE_SMOOTH);
            ImageIcon scaledBG = new ImageIcon(bg);
            JLabel backgroundLabel = new JLabel(scaledBG);
            int result = JOptionPane.showConfirmDialog(null, backgroundLabel,
                    "Do you like it?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.out.println("Check out my photography page!!");
            }
        }
    }

    // this class implements the save function of the calendar
    private class SaveCalendarEvent extends AbstractAction {

        public SaveCalendarEvent() {
            super("Save");
        }

        // EFFECTS: Saves the current state of the calendar ONLY if the user wishes to do so
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(null,
                    "Do you wish to save this Calendar?", "Calendar Saver", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                saveCalendar();
                JOptionPane.showMessageDialog(null, "Your calendar has been saved!");
            }
        }
    }

    // this class implements the load function of the calendar
    private class LoadCalendarEvent extends AbstractAction {

        public LoadCalendarEvent() {
            super("Load");
        }

        // MODIFIES: this
        // EFFECTS: Loads previous save ONLY if user chooses to do so
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(null,
                    "Do you wish to load a previously saved calendar?",
                    "Calendar Loader", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                loadCalendar();
                clearEachDay();
                JOptionPane.showMessageDialog(null, "Loaded calendar from " + calData);
                addLoadedCalendarToGUI();
                setVisible(true);
                // TODO: FIGURE OUT HOW TO MAKE SURE FRAME UPDATES WITHOUT RESIZING
            }
        }

        private void clearEachDay() {
            for (Day day : listOfDays) {
                day.removeAll();
                day.clearListOfEventButtons();
            }
        }


        // MODIFIES: this
        // EFFECTS: Adds all events from saved calendar onto the GUI as buttons
        private void addLoadedCalendarToGUI() {
            for (CalendarEvent e : cal.getListOfEvents()) {
                addEventToGUI(e);
            }
            setVisible(true);
        }
    }


    // this code has taken inspiration from this tutorial:
    // http://www.java2s.com/Tutorial/Java/0260__Swing-Event/DetectingDoubleandTripleClicks.htm
    private class DateClick extends MouseAdapter {
        JButton clickedButton;
        CalendarEvent event;

        String oldTitle;
        int oldStart;
        int oldEnd;
        String oldCategory;

        String newTitle;
        int newStart;
        int newEnd;
        String newCategory;

        JPanel editMenuPanel = new JPanel();
        JTextField nameField = new JTextField(5);
        JTextField startField = new JTextField(5);
        JTextField endField = new JTextField(5);
        String[] categories = {"family", "friends", "personal", "school", "work"};
        JComboBox categorySelect = new JComboBox(categories);

        DateClick(JButton btn, CalendarEvent event) {
            clickedButton = btn;
            this.event = event;

            oldTitle = event.getTitle();
            oldStart = event.getStartDate();
            oldEnd = event.getEndDate();
            oldCategory = event.getCategory();


        }

        public void mouseClicked(MouseEvent mouseEvent) {
            if (mouseEvent.getClickCount() == 1) {
                editMenu();
            } else if (mouseEvent.getClickCount() == 2) {
                //confirmDelete();
            }
        }

        private void editMenu() {
            setUpEditMenu();
            int result = JOptionPane.showConfirmDialog(null, editMenuPanel,
                    "Please enter event details", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                newTitle = nameField.getText();
                newStart = parseInt(startField.getText());
                newEnd = parseInt(endField.getText());
                newCategory = (String) categorySelect.getSelectedItem();
                applyChange();
                updateDateBoxes();
            }
            //displayCalendar();
        }

        private void applyChange() {
            if (oldStart != newStart || oldEnd != newEnd) {
                try {
                    event.setDates(newStart, newEnd);
                    changeEventButtonDates();
                } catch (InvalidDates e) {
                    JOptionPane.showMessageDialog(null, "INVALID INPUT FOR DATE(S)");
                }
            }
            if (!oldCategory.equals(newCategory)) {
                try {
                    event.setCategory(newCategory);
                    changeEventButtonCategory();
                } catch (InvalidCategory e) {
                    System.out.println("INVALID CATEGORY");
                }
            }
            if (!oldTitle.equals(newTitle)) {
                event.setTitle(newTitle);
                changeEventButtonName();
            }
            clickedButton.addMouseListener(new DateClick(clickedButton, event));
        }

        private void changeEventButtonDates() {
            for (Day d : listOfDays) {
                for (JButton eventButton : d.getListOfEventButtons()) {
                    if (eventButton.getText().equals(oldTitle)) {
                        if (parseInt(d.getText()) < newStart || parseInt(d.getText()) > newEnd) {
                            d.remove(eventButton);
                            d.getListOfEventButtons().remove(eventButton);
                        }
                    }
                }
                if ((parseInt(d.getText()) >= newStart && parseInt(d.getText()) < oldStart)
                        || (parseInt(d.getText()) <= newEnd && parseInt(d.getText()) > oldEnd)) {
                    d.add(clickedButton);
                    d.getListOfEventButtons().add(clickedButton);
                }
            }
        }

        private void changeEventButtonName() {
            for (Day d : listOfDays) {
                for (JButton eventButton : d.getListOfEventButtons()) {
                    if (eventButton.getText().equals(oldTitle)) {
                        eventButton.setText(newTitle);
                    }
                }
            }
        }

        private void changeEventButtonCategory() {
            for (Day d : listOfDays) {
                for (JButton eventButton : d.getListOfEventButtons()) {
                    if (eventButton.getText().equals(oldTitle)) {
                        eventButton.setBackground(getCategoryColor(newCategory));
                    }
                }
            }
        }

        private Color getCategoryColor(String category) {
            switch (category) {
                case "family":
                    return Color.RED;
                case "friends":
                    return Color.MAGENTA;
                case "personal":
                    return Color.GREEN;
                case "school":
                    return Color.BLUE;
                default:
                    return Color.ORANGE;
            }
        }


        private void setUpEditMenu() {
            editMenuPanel.add(new JLabel("Name:"));
            editMenuPanel.add(nameField);
            nameField.setText(oldTitle);
            editMenuPanel.add(Box.createHorizontalStrut(15)); // a spacer

            editMenuPanel.add(new JLabel("Start:"));
            editMenuPanel.add(startField);
            startField.setText(Integer.toString(oldStart));
            editMenuPanel.add(Box.createHorizontalStrut(15)); // a spacer

            editMenuPanel.add(new JLabel("End:"));
            editMenuPanel.add(endField);
            endField.setText(Integer.toString(oldEnd));
            editMenuPanel.add(Box.createHorizontalStrut(15)); // a spacer

            editMenuPanel.add(new JLabel("Category:"));
            editMenuPanel.add(categorySelect);
            categorySelect.setSelectedItem(oldCategory);
        }


    }


//


    private class Day extends JLabel {
        ArrayList<JButton> listOfEventButtons;

        Day(String dayOfMonth) {
            super(dayOfMonth);
            listOfEventButtons = new ArrayList<>();
        }

        public void addToListOfEventButtons(JButton btn) {
            listOfEventButtons.add(btn);
        }

        public void removeFromListOfEventButtons(JButton btn) {
            listOfEventButtons.remove(btn);
        }

        public void clearListOfEventButtons() {
            listOfEventButtons.clear();
        }

        public ArrayList<JButton> getListOfEventButtons() {
            return listOfEventButtons;
        }

    }
}











