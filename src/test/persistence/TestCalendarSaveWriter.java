package persistence;

import exceptions.InvalidCategory;
import exceptions.InvalidDates;
import model.Calendar;
import model.CalendarEvent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class TestCalendarSaveWriter extends JsonTest{
    @Test
    void testWriterInvalidFile() {
        try {
            Calendar cal = new Calendar("DECEMBER", 2019);
            CalendarSaveWriter writer = new CalendarSaveWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("File should not exist");
        } catch (FileNotFoundException e) {
            // do nothing
        }
    }

    @Test
    void testWriterEmptyFile() {
        try {
            Calendar cal = new Calendar("JANUARY", 2022);
            CalendarSaveWriter writer = new CalendarSaveWriter("./data/testEmptyWriter.json");
            writer.open();
            writer.write(cal);
            writer.close();

            CalendarSaveReader reader = new CalendarSaveReader("./data/testEmptyWriter.json");
            cal = reader.read();
            assertEquals("JANUARY", cal.getMonth());
            assertEquals(2022, cal.getYear());
            assertTrue(cal.getListOfEvents().isEmpty());
        } catch (FileNotFoundException e) {
            fail("File should exist");
        } catch (IOException e) {
            fail("File exists and is in correct form");
        }
    }

    @Test
    void testWriterGeneral() {
        try {
            Calendar cal = new Calendar("SEPTEMBER", 2018);
            CalendarSaveWriter writer = new CalendarSaveWriter("./data/testGeneralWriter.json");
            CalendarEvent fam = new CalendarEvent("Park Day", 2, 2, "family");
            CalendarEvent sch = new CalendarEvent("Imagine Day", 8, 8, "school");
            CalendarEvent pers = new CalendarEvent("Google Course", 2, 16, "personal");
            CalendarEvent frnds = new CalendarEvent("Party", 19, 19, "friends");
            CalendarEvent work = new CalendarEvent("PD week", 10, 17, "work");

            cal.addEvent(fam);
            cal.addEvent(sch);
            cal.addEvent(pers);
            cal.addEvent(frnds);
            cal.addEvent(work);

            writer.open();
            writer.write(cal);
            writer.close();

            CalendarSaveReader reader = new CalendarSaveReader("./data/testGeneralWriter.json");
            cal = reader.read();
            assertEquals("SEPTEMBER", cal.getMonth());
            assertEquals(2018, cal.getYear());
            checkEvent("Park Day", 2, 2, "family",
                    cal.getEvent("Park Day"));
            checkEvent("Imagine Day", 8, 8, "school",
                    cal.getEvent("Imagine Day"));
            checkEvent("Google Course", 2, 16, "personal",
                    cal.getEvent("Google Course"));
            checkEvent("Party", 19, 19, "friends",
                    cal.getEvent("Party"));
            checkEvent("PD week", 10, 17, "work",
                    cal.getEvent("PD week"));

        } catch (InvalidCategory invalidCategory) {
            fail("All events added should be correct");
        } catch (InvalidDates invalidDates) {
            fail("All events added should be correct");
        } catch (FileNotFoundException e) {
            fail("The file should be correct");
        } catch (IOException e) {
            fail("The file should be correct");
        }
    }
}
