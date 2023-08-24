package deserializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import models.Task;
import models.TaskStatus;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class TaskAdapter extends TypeAdapter<Task> {

    private static final DateTimeFormatter ZONED_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    public void write(JsonWriter out, Task task) throws IOException {
        if (task == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("taskName").value(task.getTaskName());
        out.name("taskDescription").value(task.getTaskDescription());
        out.name("taskID").value(task.getTaskID());
        out.name("taskStatus").value(task.getTaskStatus().name()); // Serialize TaskStatus as its name
        out.name("startTime").value(ZONED_DATE_TIME_FORMATTER.format(task.getStartTime()));
        out.name("endTime").value(ZONED_DATE_TIME_FORMATTER.format(task.getEndTime()));
        out.name("duration").value(task.getDuration().toMillis());
        // Write other properties here...
        out.endObject();
    }

    @Override
    public Task read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginObject();
        String taskName = null;
        String taskDescription = null;
        int taskID = -1;
        TaskStatus taskStatus = null;
        ZonedDateTime startTime = null;
        ZonedDateTime endTime = null;
        Duration duration = null;

        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "taskName":
                    taskName = in.nextString();
                    break;
                case "taskDescription":
                    taskDescription = in.nextString();
                    break;
                case "taskID":
                    taskID = in.nextInt();
                    break;
                case "taskStatus":
                    taskStatus = TaskStatus.valueOf(in.nextString());// Assuming valid TaskStatus values
                    break;
                case "duration":
                    in.beginObject();
                    long seconds = 0;
                    int nanos = 0;
                    while (in.hasNext()) {
                        String fieldName = in.nextName();
                        switch (fieldName) {
                            case "seconds":
                                seconds = in.nextLong();
                                break;
                            case "nanos":
                                nanos = in.nextInt();
                                break;
                            default:
                                in.skipValue();
                                break;
                        }
                    }
                    in.endObject();
                    duration = Duration.ofSeconds(seconds).plusNanos(nanos);
                    break;
                case "startTime":
                    in.beginObject();
                    ZonedDateTime dateTime = null;
                    ZoneId zoneId = null;
                    while (in.hasNext()) {
                        String fieldName = in.nextName();
                        switch (fieldName) {
                            case "dateTime":
                                dateTime = readDateTime(in);
                                break;
                            case "zone":
                                zoneId = readZoneId(in);
                                break;
                            default:
                                in.skipValue();
                                break;
                        }
                    }
                    in.endObject();
                    if (dateTime != null && zoneId != null) {
                        startTime = dateTime.withZoneSameInstant(zoneId);
                    }
                    break;

                case "endTime":
                    in.beginObject();
                    dateTime = null;
                    zoneId = null;
                    while (in.hasNext()) {
                        String fieldName = in.nextName();
                        switch (fieldName) {
                            case "dateTime":
                                dateTime = readDateTime(in);
                                break;
                            case "zone":
                                zoneId = readZoneId(in);
                                break;
                            default:
                                in.skipValue();
                                break;
                        }
                    }
                    in.endObject();
                    if (dateTime != null && zoneId != null) {
                        endTime = dateTime.withZoneSameInstant(zoneId);
                    }
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();

        if (taskName != null && taskDescription != null && taskID != -1 && startTime != null && endTime != null && duration != null) {
            Task task = new Task(taskName, taskDescription, taskID, taskStatus, duration, startTime);
            task.setStartTime(startTime);
            task.setEndTime(endTime);
            return task;
        } else {
            throw new IOException("Incomplete or invalid JSON for Task");
        }


    }

    private ZonedDateTime readDateTime(JsonReader in) throws IOException {
        in.beginObject();
        LocalDate date = null;
        LocalTime time = null;
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "date":
                    date = readLocalDate(in);
                    break;
                case "time":
                    time = readLocalTime(in);
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        if (date != null && time != null) {
            return ZonedDateTime.of(date, time, ZoneId.systemDefault()); // You might need to adjust the time zone
        }
        return null;
    }

    private LocalDate readLocalDate(JsonReader in) throws IOException {
        in.beginObject();
        int year = -1, month = -1, day = -1;
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "year":
                    year = in.nextInt();
                    break;
                case "month":
                    month = in.nextInt();
                    break;
                case "day":
                    day = in.nextInt();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        if (year != -1 && month != -1 && day != -1) {
            return LocalDate.of(year, month, day);
        }
        return null;
    }

    private LocalTime readLocalTime(JsonReader in) throws IOException {
        in.beginObject();
        int hour = -1, minute = -1, second = -1;
        while (in.hasNext()) {
            String fieldName = in.nextName();
            switch (fieldName) {
                case "hour":
                    hour = in.nextInt();
                    break;
                case "minute":
                    minute = in.nextInt();
                    break;
                case "second":
                    second = in.nextInt();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        if (hour != -1 && minute != -1 && second != -1) {
            return LocalTime.of(hour, minute, second);
        }
        return null;
    }

    private ZoneId readZoneId(JsonReader in) throws IOException {
        in.beginObject();
        String zoneName = null;
        while (in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("id")) {
                zoneName = in.nextString();
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        if (zoneName != null) {
            return ZoneId.of(zoneName);
        }
        return null;
    }
}
