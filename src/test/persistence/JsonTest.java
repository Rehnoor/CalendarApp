package persistence;

import model.Event;
import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {
    protected void checkEvent(String title, int startDate, int endDate, String cat, Event e) {
        assertEquals(title, e.getTitle());
        assertEquals(startDate, e.getStartDate());
        assertEquals(endDate, e.getEndDate());
        assertEquals(cat, e.getCategory());
    }
}
