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
        if(jsonReader.peek() == JsonToken.NULL){
            jsonReader.nextNull();;
            return null;
        }

        jsonReader.beginObject();
        int epicID = -1;
        String taskName = null;
        String taskDescription = null;
        int taskID = -1;
        TaskStatus taskStatus = null;
        ZonedDateTime startTime = null;
        ZonedDateTime endTime = null;
        Duration duration = null;

        while ((jsonReader.hasNext())){
            String name = jsonReader.nextName();

            switch (name){
                case "epicID":
                    epicID = jsonReader.nextInt();
                    break;
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
                    jsonReader.beginObject();
                    long seconds = 0;
                    int nanos = 0;
                    while (jsonReader.hasNext()){
                        String fieldName = jsonReader.nextName();
                        switch (fieldName){
                            case "seconds":
                                seconds = jsonReader.nextLong();
                                break;
                            case "nanos":
                                nanos = jsonReader.nextInt();
                                break;
                            default:
                                jsonReader.skipValue();
                                break;
                        }
                    }
                    jsonReader.endObject();
                    duration = Duration.ofSeconds(seconds).plusNanos(nanos);
                    break;
                case "startTime":
                    jsonReader.beginObject();
                    ZonedDateTime dateTime = null;
                    ZoneId zoneId = null;
                    while (jsonReader.hasNext()){
                        String fieldName = jsonReader.nextName();
                        switch (fieldName){
                            case "dateTime":
                                dateTime = UtilAdapter.readDateTime(jsonReader);
                                break;
                            case "zone":
                                zoneId = UtilAdapter.readZoneId(jsonReader);
                                break;
                            default:
                                jsonReader.skipValue();
                                break;
                        }
                    }
                    jsonReader.endObject();
                    if (dateTime != null && zoneId != null) {
                        startTime = dateTime.withZoneSameInstant(zoneId);
                    }
                    break;
                case "endTime":
                    jsonReader.beginObject();
                    dateTime = null;
                    zoneId = null;
                    while (jsonReader.hasNext()) {
                        String fieldName = jsonReader.nextName();
                        switch (fieldName) {
                            case "dateTime":
                                dateTime = UtilAdapter.readDateTime(jsonReader);
                                break;
                            case "zone":
                                zoneId = UtilAdapter.readZoneId(jsonReader);
                                break;
                            default:
                                jsonReader.skipValue();
                                break;
                        }
                    }
                    jsonReader.endObject();
                    if (dateTime != null && zoneId != null) {
                        endTime = dateTime.withZoneSameInstant(zoneId);
                    }
                    break;
                default:
                    jsonReader.skipValue();
                    break;

            }
        }

        jsonReader.endObject();

        if (taskID != 0 && taskName != null && taskDescription != null && taskID != -1 && startTime != null && endTime != null && duration != null) {
            SubTask subTask = new SubTask(taskName, taskDescription, taskID, taskStatus, epicID, duration, startTime);
            subTask.setStartTime(startTime);
            subTask.setEndTime(endTime);
            return subTask;
        } else {
            throw new IOException("Incomplete or invalid JSON for Task");
        }
    }
}
