package persistence;

import model.Calendar;
import model.Event;
import org.json.JSONObject;


import java.io.*;

// Represents a writer that writes Calendar as a JSON object
// Some methods taken from JSONWriter class in
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class CalendarSaveWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String dest;

    // EFFECTS: constructs writer to write to destination file
    public CalendarSaveWriter(String dest) {
        this.dest = dest;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(dest));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(Calendar cal) {
        JSONObject json = cal.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
