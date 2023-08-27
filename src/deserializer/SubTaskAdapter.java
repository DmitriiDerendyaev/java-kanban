package deserializer;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import models.SubTask;
import models.Task;
import models.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SubTaskAdapter extends TypeAdapter<SubTask> {

    private static final DateTimeFormatter ZONED_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    public void write(JsonWriter jsonWriter, SubTask subTask) throws IOException {
        if(subTask == null){
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.beginObject();

        jsonWriter.name("epicID").value(subTask.getEpicID());
        jsonWriter.name("taskName").value(subTask.getTaskName());
        jsonWriter.name("taskDescription").value(subTask.getTaskDescription());
        jsonWriter.name("taskID").value(subTask.getTaskID());
        jsonWriter.name("taskStatus").value(subTask.getTaskStatus().name());
        jsonWriter.name("startTime").value(ZONED_DATE_TIME_FORMATTER.format(subTask.getStartTime()));
        jsonWriter.name("endTime").value(ZONED_DATE_TIME_FORMATTER.format(subTask.getEndTime()));
        jsonWriter.name("duration").value(subTask.getDuration().toMillis());

        jsonWriter.endObject();
    }

    @Override
    public SubTask read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        jsonReader.beginObject();
        int epicID = -1;
        String taskName = null;
        String taskDescription = null;
//        int taskID = -1;
        TaskStatus taskStatus = null;
        ZonedDateTime startTime = null;
        ZonedDateTime endTime = null;
        Duration duration = null;

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            switch (name) {
                case "epicID":
                    epicID = jsonReader.nextInt();
                    break;
                case "taskName":
                    taskName = jsonReader.nextString();
                    break;
                case "taskDescription":
                    taskDescription = jsonReader.nextString();
                    break;
//                case "taskID":
//                    taskID = jsonReader.nextInt();
//                    break;
                case "taskStatus":
                    taskStatus = TaskStatus.valueOf(jsonReader.nextString());
                    break;
                case "duration":
                    duration = Duration.ofMillis(jsonReader.nextLong());
                    break;
                case "startTime":
                    startTime = ZonedDateTime.parse(jsonReader.nextString());
                    break;
                case "endTime":
                    endTime = ZonedDateTime.parse(jsonReader.nextString());
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }

        jsonReader.endObject();

        if (taskName != null && taskDescription != null /*&& taskID != -1*/ && startTime != null && endTime != null && duration != null) {
//            SubTask subTask = new SubTask(taskName, taskDescription, taskID, taskStatus, epicID, duration, startTime);
            SubTask subTask = new SubTask(taskName, taskDescription, taskStatus, duration, startTime, epicID);
            subTask.setStartTime(startTime);
            subTask.setEndTime(endTime);
            return subTask;
        } else {
            throw new IOException("Incomplete or invalid JSON for SubTask");
        }
    }

}
