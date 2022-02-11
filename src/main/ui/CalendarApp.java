package ui;

import model.Calendar;

import java.time.LocalDate;


public class CalendarApp {
    Calendar cal;

    //Constants
    String month = LocalDate.now().getMonth().toString();
    int year = LocalDate.now().getYear();
    String dayOfWeek = LocalDate.now().getDayOfWeek().toString();
    int dayOfMonth = LocalDate.now().getDayOfMonth();

    // EFFECTS: Runs the calendar application
    public CalendarApp() {
        runCalendar();
    }

    // MODIFIES: this
    // EFFECTS:
    private void runCalendar() {
        boolean active = true;
        initializeCal();

        while (active) {
            mainMenu();
        }
    }

    // MODIFIES: this
    // EFFECTS: Initialize Cal with the current Month and Year
    private void initializeCal() {
        cal = new model.Calendar(month, year);
    }

    private void mainMenu() {
        System.out.println("Today is: " + dayOfWeek + ", " + month + dayOfMonth + ", " + year);
        System.out.println("\nPlease select one of the following options and press ENTER:");
        System.out.println("\ts -> Select a day");
        System.out.println("\ta -> Add an event to your calendar");
        System.out.println("\tf -> Filter you");
    }


}
