package persistence;

import model.Calendar;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class TestCalendarSaveReader extends JsonTest{

    @Test
    void testReadNoSuchFile() {
        CalendarSaveReader reader = new CalendarSaveReader("./data/noSuchFile.json");
        try {
            Calendar cal = reader.read();
            fail("This file should not exist");
        } catch (IOException e) {
            // do nothing
        }
    }

    @Test
    void testReadEmptyFile() {
        CalendarSaveReader reader = new CalendarSaveReader("./data/testEmptyFileReader.json");
        try {
            Calendar cal = reader.read();
            assertEquals("FEBRUARY", cal.getMonth());
            assertEquals(2022, cal.getYear());
            assertTrue(cal.getListOfEvents().isEmpty());
        } catch (IOException e) {
            fail("File actually exists");
        }
    }

    @Test
    void testReadGeneral() {
        CalendarSaveReader reader = new CalendarSaveReader("./data/testGeneralReader.json");
        try {
            Calendar cal = reader.read();
            assertEquals("APRIL", cal.getMonth());
            assertEquals(2021, cal.getYear());
            assertEquals(5, cal.getListOfEvents().size());
            checkEvent("Final Exams", 12, 26, "school",
                    cal.getEvent("Final Exams"));
            checkEvent("Auditing", 27, 27, "work",
                    cal.getEvent("Auditing"));
            checkEvent("Weekend Road trip", 3, 4, "family",
                    cal.getEvent("Weekend Road trip"));
            checkEvent("Birthday Party", 13, 13, "friends",
                    cal.getEvent("Birthday Party"));
            checkEvent("Dentist Appointment", 19, 19, "personal",
                    cal.getEvent("Dentist Appointment"));
        } catch (IOException e) {
            fail("File exists and exception should not be thrown");
        }
    }

}
