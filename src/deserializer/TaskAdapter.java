package deserializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import models.Task;
import models.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
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
                    startTime = ZonedDateTime.parse(in.nextString(), ZONED_DATE_TIME_FORMATTER);
                    break;
                case "endTime":
                    endTime = ZonedDateTime.parse(in.nextString(), ZONED_DATE_TIME_FORMATTER);
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
}
