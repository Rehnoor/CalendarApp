package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCalendar {
    Calendar testCalendar;
    Event eventA;
    Event eventB;

    @BeforeEach
    void setUp() {
        testCalendar = new Calendar("February", 2022);
        eventA = new Event("Event A", 2, 5, "School");
        eventB = new Event("Event B", 15, 20, "Family");
    }

    @Test
    void testConstructor() {
        assertEquals("February", testCalendar.getMonth());
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
        testCalendar.addEvent(eventA);
        testCalendar.deleteEvent(eventA);
        assertTrue(testCalendar.listOfEvents.isEmpty());
    }

    @Test
    void testDeleteEventAddOneCantFindOneToDelete() {
        testCalendar.addEvent(eventA);
        testCalendar.deleteEvent(eventB);
        assertTrue(testCalendar.isOnCalendar(eventA));
        assertFalse(testCalendar.listOfEvents.isEmpty());
    }

    @Test
    void testDeleteEventAddTwoDeleteOne() {
        testCalendar.addEvent(eventA);
        testCalendar.addEvent(eventB);
        testCalendar.deleteEvent(eventB);
        assertFalse(testCalendar.isOnCalendar(eventB));
        assertTrue(testCalendar.isOnCalendar(eventA));
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
        Event eventC = new Event("Event C", 5, 15, "Personal");
        testCalendar.addEvent(eventA);
        testCalendar.addEvent(eventB);
        assertFalse(testCalendar.isOnCalendar(eventC));
    }

    @Test
    void testIsThereSimilarEventNoSimilar() {
        testCalendar.addEvent(eventA);
        assertFalse(testCalendar.isThereSimilarEvent("Event A"));
    }

    @Test
    void testIsThereSimilarEventOneSimilar() {
        Event eventACopy = new Event("Event A", 3, 9, "Work");
        testCalendar.addEvent(eventA);
        testCalendar.addEvent(eventACopy);
        assertTrue(testCalendar.isThereSimilarEvent("Event A"));
    }

    @Test
    void testIsThereSimilarEventTwoSimilar() {
        Event eventACopyA = new Event("Event A", 3, 9, "Work");
        Event eventACopyB = new Event("Event A", 13, 13, "Personal");
        testCalendar.addEvent(eventA);
        testCalendar.addEvent(eventACopyA);
        testCalendar.addEvent(eventACopyB);
        assertTrue(testCalendar.isThereSimilarEvent("Event A"));
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
        Event eventACopy = new Event("Event A", 3, 9, "Work");
        testCalendar.addEvent(eventACopy);
        testCalendar.addEvent(eventA);
        assertEquals(eventA, testCalendar.getEvent("Event A", 2, 5));
        assertEquals(eventACopy, testCalendar.getEvent("Event A", 3, 9));
    }

}
