package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestEvent {
    Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event("Fun Event", 3, 15, "Personal");
    }

    @Test
    void testConstructor() {
        assertEquals("Fun Event", testEvent.getTitle());
        assertEquals(3, testEvent.getStartDate());
        assertEquals(15, testEvent.getEndDate());
        assertEquals("Personal", testEvent.getCategory());
    }

    @Test
    void testSetTitle() {
        testEvent.setTitle("Boring Event");
        assertEquals("Boring Event", testEvent.getTitle());
        assertEquals(3, testEvent.getStartDate());
        assertEquals(15, testEvent.getEndDate());
        assertEquals("Personal", testEvent.getCategory());
    }


    @Test
    void testSetDatesSameDay() {
        testEvent.setDates(15, 15);
        assertEquals(15, testEvent.getStartDate());
        assertEquals(15, testEvent.getEndDate());
        assertEquals("Fun Event", testEvent.getTitle());
        assertEquals("Personal", testEvent.getCategory());
    }

    @Test
    void testSetDatesNotSameDay() {
        testEvent.setDates(1, 20);
        assertEquals(1, testEvent.getStartDate());
        assertEquals(20, testEvent.getEndDate());
        assertEquals("Fun Event", testEvent.getTitle());
        assertEquals("Personal", testEvent.getCategory());
    }

    @Test
    void testSetCategory() {
        testEvent.setCategory("School");
        assertEquals("School", testEvent.getCategory());
        assertEquals("Fun Event", testEvent.getTitle());
        assertEquals(3, testEvent.getStartDate());
        assertEquals(15, testEvent.getEndDate());
    }


}
