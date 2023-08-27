package deserializer;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import models.Epic;
import models.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EpicAdapter extends TypeAdapter<Epic> {

    private static final DateTimeFormatter ZONED_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        if(epic == null){
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.beginObject();

        jsonWriter.name("taskName").value(epic.getTaskName());
        jsonWriter.name("taskDescription").value(epic.getTaskDescription());
        jsonWriter.name("taskID").value(epic.getTaskID());
        jsonWriter.name("taskStatus").value(epic.getTaskStatus().name());
        jsonWriter.name("startTime").value(ZONED_DATE_TIME_FORMATTER.format(epic.getStartTime()));
        jsonWriter.name("endTime").value(ZONED_DATE_TIME_FORMATTER.format(epic.getEndTime()));
        jsonWriter.name("duration").value(epic.getDuration().toMillis());
        jsonWriter.name("subTasksList").beginArray();
        for (Integer currentTaskID : epic.getTaskCollection()) {
            jsonWriter.value(currentTaskID);
        }
        jsonWriter.endArray().endObject();
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        jsonReader.beginObject();
        String taskName = null;
        String taskDescription = null;
        int taskID = -1;
        TaskStatus taskStatus = null;
        ZonedDateTime startTime = null;
        ZonedDateTime endTime = null;
        Duration duration = null;
        List<Integer> subTasksList = new ArrayList<>();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            switch (name) {
                case "taskName":
                    taskName = jsonReader.nextString();
                    break;
                case "taskDescription":
                    taskDescription = jsonReader.nextString();
                    break;
                case "taskID":
                    taskID = jsonReader.nextInt();
                    break;
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
                case "subTasksList":
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        subTasksList.add(jsonReader.nextInt());
                    }
                    jsonReader.endArray();
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }

        jsonReader.endObject();

        if (taskName != null && taskDescription != null && taskID != -1 && startTime != null && endTime != null && duration != null) {
            Epic epic = new Epic(taskName, taskDescription, taskID, taskStatus, subTasksList, duration, startTime);
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setTaskCollection(subTasksList);
            return epic;
        } else {
            throw new IOException("Incomplete or invalid JSON for Epic");
        }
    }

}
