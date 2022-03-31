package model;

import exceptions.CanNotFindEvent;
import exceptions.InvalidCategory;
import exceptions.InvalidDates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCalendar {
    Calendar testCalendar;
    CalendarEvent eventA;
    CalendarEvent eventB;

    @BeforeEach
    void setUp() {
        testCalendar = new Calendar("March", 2022);
        try {
            eventA = new CalendarEvent("Event A", 2, 5, "school");
            eventB = new CalendarEvent("Event B", 15, 20, "family");
        } catch (InvalidCategory invalidCategory) {
            fail("Category is actually correct");
        } catch (InvalidDates invalidDates) {
            fail("Dates are actually correct");
        }
    }


    @Test
    void testConstructor() {
        assertEquals("March", testCalendar.getMonth());
        assertEquals(2022, testCalendar.getYear());
        assertTrue(testCalendar.getListOfEvents().isEmpty());
    }

    @Test
    void testAddEvent() {
        testCalendar.addEvent(eventA);
        testCalendar.addEvent(eventB);
        assertFalse(testCalendar.getListOfEvents().isEmpty());
        assertTrue(testCalendar.isOnCalendar(eventA));
        assertTrue(testCalendar.isOnCalendar(eventB));
    }


    @Test
    void testDeleteEventAddOneDeleteOne() {
        try {
            testCalendar.addEvent(eventA);
            testCalendar.deleteEvent(eventA);
            assertTrue(testCalendar.listOfEvents.isEmpty());
        } catch (CanNotFindEvent e) {
            fail("listOfEvents should be empty");
        }

    }


    @Test
    void testDeleteEventAddOneCantFindOneToDelete() {
        try {
            testCalendar.addEvent(eventA);
            testCalendar.deleteEvent(eventB);
            fail("Can not delete eventB since it is not on Calendar");
        } catch (CanNotFindEvent e) {
            // do nothing
        }
    }

    @Test
    void testDeleteEventAddTwoDeleteOne() {
        try {
            testCalendar.addEvent(eventA);
            testCalendar.addEvent(eventB);
            testCalendar.deleteEvent(eventB);
            assertFalse(testCalendar.isOnCalendar(eventB));
            assertTrue(testCalendar.isOnCalendar(eventA));
        } catch (CanNotFindEvent e) {
            fail("Should have correctly deleted eventB");
        }
    }

    @Test
    void testIsOnCalendarNothingOnCalendar() {
        assertFalse(testCalendar.isOnCalendar(eventA));
    }

    @Test
    void testIsOnCalendarAddOneHasOne() {
        testCalendar.addEvent(eventA);
        assertTrue(testCalendar.isOnCalendar(eventA));
    }

    @Test
    void testIsOnCalendarAddTwoHasTwo() {
        testCalendar.addEvent(eventA);
        testCalendar.addEvent(eventB);
        assertTrue(testCalendar.isOnCalendar(eventB));
        assertTrue(testCalendar.isOnCalendar(eventA));
    }

    @Test
    void testIsOnCalendarAddTwoDoesntHaveThird() {
        try {
            CalendarEvent eventC = new CalendarEvent("Event C", 5, 15, "personal");
            testCalendar.addEvent(eventA);
            testCalendar.addEvent(eventB);
            assertFalse(testCalendar.isOnCalendar(eventC));
        } catch (InvalidCategory e) {
            fail("Category is actually valid");
        } catch (InvalidDates e) {
            fail("Dates are actually valid");
        }
    }

    @Test
    void testIsThereSimilarEventNoSimilar() {
        testCalendar.addEvent(eventA);
        assertFalse(testCalendar.isThereSimilarEvent("Event A"));
    }

    @Test
    void testIsThereSimilarEventOneSimilar() {
        try {
            CalendarEvent eventACopy = new CalendarEvent("Event A", 3, 9, "work");
            testCalendar.addEvent(eventA);
            testCalendar.addEvent(eventACopy);
            assertTrue(testCalendar.isThereSimilarEvent("Event A"));
        } catch (InvalidCategory invalidCategory) {
            fail("Category is actually valid");
        } catch (InvalidDates invalidDates) {
            fail("Dates are actually valid");
        }
    }

    @Test
    void testIsThereSimilarEventTwoSimilar() {
        try {
            CalendarEvent eventACopyA = new CalendarEvent("Event A", 3, 9, "work");
            CalendarEvent eventACopyB = new CalendarEvent("Event A", 13, 13, "personal");
            testCalendar.addEvent(eventA);
            testCalendar.addEvent(eventACopyA);
            testCalendar.addEvent(eventACopyB);
            assertTrue(testCalendar.isThereSimilarEvent("Event A"));
        } catch (InvalidCategory invalidCategory) {
            fail("Both categories should be valid");
        } catch (InvalidDates invalidDates) {
            fail("Both event's start/end dates should be valid");
        }

    }

    @Test
    void testGetEventByNameAdd2DifferentEvents() {
        testCalendar.addEvent(eventA);
        testCalendar.addEvent(eventB);
        assertEquals(eventA, testCalendar.getEvent("Event A"));
        assertEquals(eventB, testCalendar.getEvent("Event B"));
    }


    @Test
    void testGetEventWithUsedNameButDifferentDates() {
        try {
            CalendarEvent eventACopy = new CalendarEvent("Event A", 3, 9, "work");
            testCalendar.addEvent(eventACopy);
            testCalendar.addEvent(eventA);
            assertEquals(eventA, testCalendar.getEvent("Event A", 2, 5));
            assertEquals(eventACopy, testCalendar.getEvent("Event A", 3, 9));
        } catch (InvalidCategory invalidCategory) {
            fail("Category should be valid");
        } catch (InvalidDates invalidDates) {
            fail("Dates should be valid");
        }
    }

    @Test
    void testIsEventOnCalendarItIs() {
        testCalendar.addEvent(eventA);
        assertTrue(testCalendar.isEventOnCalendar("Event A"));
    }

    @Test
    void testIsEventOnCalendarItIsnt() {
        testCalendar.addEvent(eventA);
        assertFalse(testCalendar.isEventOnCalendar("Event B"));
    }

}
