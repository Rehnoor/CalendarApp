package model;

import exceptions.GreaterThanLastDay;
import exceptions.InvalidCategory;
import exceptions.InvalidDates;
import exceptions.StartGreaterThanEnd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class TestEvent {
    Event testEvent;

    @BeforeEach
    void setUp() {
        try {
            testEvent = new Event("Fun Event", 3, 15, "personal");
        } catch (InvalidCategory invalidCategory) {
            fail("Category is actually correct");
        } catch (InvalidDates invalidDates) {
            fail("Dates are actually correct");
        }
    }

    @Test
    void testConstructorNoExceptions() {
        assertEquals("Fun Event", testEvent.getTitle());
        assertEquals(3, testEvent.getStartDate());
        assertEquals(15, testEvent.getEndDate());
        assertEquals("personal", testEvent.getCategory());
        ArrayList<Integer> listOfDays = new ArrayList<>();
        for (int x = 3; x <= 15; x++) {
            listOfDays.add(x);
        }
        assertEquals(listOfDays, testEvent.getListOfDays());
    }

    @Test
    void testConstructorInvalidCategory() {
        try {
            Event e = new Event("Not fun event", 7, 7, "dentist");
            fail("dentist is an invalid category");
        } catch (InvalidCategory ic) {
            // do nothing
        } catch (InvalidDates id) {
            fail("The dates are actually valid");
        }
    }

    @Test
    void testConstructorStartGreaterThanEnd() {
        try {
            Event e = new Event("Funny", 17, 12, "school");
            fail("StartGreaterThanEnd exception should have been thrown");
        } catch (InvalidCategory invalidCategory) {
            fail("Category is actually valid");
        } catch (StartGreaterThanEnd startGreaterThanEnd) {
            // do nothing
        } catch (GreaterThanLastDay greaterThanLastDay) {
            fail("Both dates do not exceed the last day of the month");
        } catch (InvalidDates invalidDates) {
            fail("StartGreaterThanEnd exception should have been thrown");
        }
    }


    @Test
    void testSetTitle() {
        testEvent.setTitle("Boring Event");
        assertEquals("Boring Event", testEvent.getTitle());
        assertEquals(3, testEvent.getStartDate());
        assertEquals(15, testEvent.getEndDate());
        assertEquals("personal", testEvent.getCategory());
    }


    @Test
    void testSetDatesSameDay() {
        try {
            testEvent.setDates(15, 15);
            assertEquals(15, testEvent.getStartDate());
            assertEquals(15, testEvent.getEndDate());
            assertEquals("Fun Event", testEvent.getTitle());
            assertEquals("personal", testEvent.getCategory());
        } catch (InvalidDates e) {
            fail("Dates are actually valid");
        }
    }


    @Test
    void testSetDatesNotSameDay() {
        try {
            testEvent.setDates(1, 20);
            assertEquals(1, testEvent.getStartDate());
            assertEquals(20, testEvent.getEndDate());
            assertEquals("Fun Event", testEvent.getTitle());
            assertEquals("personal", testEvent.getCategory());
        } catch (InvalidDates e) {
            fail("Dates are actually valid.");
        }
    }

    @Test
    void testSetDatesStartGreaterThanEnd() {
        try {
            testEvent.setDates(14, 13);
            fail("StartGreaterThanEnd exception is expected");
        } catch (StartGreaterThanEnd startGreaterThanEnd) {
            // do nothing
        } catch (InvalidDates invalidDates) {
            fail("StartGreaterThanEnd exception should have been thrown and caught");
        }
    }

    @Test
    void testSetDatesOneDateGreaterThanLastDay() {
        try {
            testEvent.setDates(13, 35);
            fail("GreaterThanLastDay exception expected");
        } catch (GreaterThanLastDay greaterThanLastDay) {
            //do nothing
        } catch (InvalidDates e) {
            fail("Should have caught GreaterThanLastDay");
        }
    }

    @Test
    void testSetDatesBothDatesGreaterThanLastDay() {

        try {
            testEvent.setDates(40, 49);
            fail("GreaterThanLastDay exception expected");
        } catch (GreaterThanLastDay e) {
            // do nothing
        } catch (InvalidDates e) {
            fail("Should have caught GreaterThanLastDay exception");
        }

    }


    @Test
    void testSetCategory() {
        try {
            testEvent.setCategory("school");
            assertEquals("school", testEvent.getCategory());
            assertEquals("Fun Event", testEvent.getTitle());
            assertEquals(3, testEvent.getStartDate());
            assertEquals(15, testEvent.getEndDate());
        } catch (InvalidCategory e) {
            fail("Category is actually valid");
        }
    }

    @Test
    void testSetCategoryInvalidCategory() {
        try {
            testEvent.setCategory("Driving");
            fail("InvalidCategory exception expected");
        } catch (InvalidCategory e) {
            // do nothing
        }
    }


}
