package persistence;

import org.json.JSONObject;

// This whole interface has been taken from
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writable {
    JSONObject toJson();
}
