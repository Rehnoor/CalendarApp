package persistence;

import exceptions.InvalidCategory;
import exceptions.InvalidDates;
import model.CalendarEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import model.Calendar;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads cal from JSON data stored in the date file
// Some methods taken from JSONReader class in
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class CalendarSaveReader {
    private String src;

    // EFFECTS: constructs reader to read from source file
    public CalendarSaveReader(String src) {
        this.src = src;
    }

    // EFFECTS: reads calendar from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Calendar read() throws IOException {
        String jsonData = readFile(src);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCalendar(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses calendar from JSON object and returns it
    private Calendar parseCalendar(JSONObject jsonObject) {
        String month = jsonObject.getString("month");
        int year = jsonObject.getInt("year");
        Calendar cal = new Calendar(month, year);
        addEvents(cal, jsonObject);
        return cal;
    }

    // MODIFIES: cal
    // EFFECTS: parses events from JSON object and adds them to calendar
    private void addEvents(Calendar cal, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Events");
        for (Object json : jsonArray) {
            JSONObject nextEvent = (JSONObject) json;
            addEvent(cal, nextEvent);
        }
    }

    // MODIFIES: wr
    // EFFECTS: parses an event from JSON object and adds it to calendar
    private void addEvent(Calendar cal, JSONObject jsonObject) {
        try {
            String title = jsonObject.getString("title");
            int startDate = jsonObject.getInt("start date");
            int endDate = jsonObject.getInt("end date");
            String category = jsonObject.getString("category");
            CalendarEvent e = new CalendarEvent(title, startDate, endDate, category);
            cal.addEvent(e);
        } catch (InvalidCategory ex) {
            System.out.println("The category was invalid... ");
        } catch (InvalidDates ex) {
            System.out.println("The dates were invalid... ");
        }
    }
}
