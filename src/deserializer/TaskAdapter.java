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
        out.endObject();
    }

    @Override
    public Task read(JsonReader in) throws IOException {
        // TODO: duration также не должен приходить извне,
        //  иначе его не посчитать никак или убрать конец выполнения, ЧТО БОЛЕЕ ЛОГИЧНО
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
                    taskStatus = TaskStatus.valueOf(in.nextString()); // Assuming valid TaskStatus values
                    break;
                case "duration":
                    long durationMillis = in.nextLong();
                    duration = Duration.ofMillis(durationMillis);
                    break;
                case "startTime":
                    startTime = ZonedDateTime.parse(in.nextString());
                    break;
                case "endTime":
                    endTime = ZonedDateTime.parse(in.nextString());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();

        if (taskName != null && taskDescription != null /*&& taskID != -1*/ && startTime != null && duration != null) {
            if (endTime == null) {
                endTime = startTime.plus(duration);
            }

            Task task;
            if(taskID != -1) {
                task = new Task(taskName, taskDescription, taskID, taskStatus, duration, startTime);
            } else {
                task = new Task(taskName, taskDescription, taskStatus, duration, startTime);
            }
            task.setStartTime(startTime);
            task.setEndTime(endTime);
            return task;
        } else {
            throw new IOException("Incomplete or invalid JSON for Task");
        }
    }



}
