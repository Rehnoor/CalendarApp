package persistence;

import model.CalendarEvent;
import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {
    protected void checkEvent(String title, int startDate, int endDate, String cat, CalendarEvent e) {
        assertEquals(title, e.getTitle());
        assertEquals(startDate, e.getStartDate());
        assertEquals(endDate, e.getEndDate());
        assertEquals(cat, e.getCategory());
    }
}
