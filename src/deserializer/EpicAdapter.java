package deserializer;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import models.Epic;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

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
        return null;
    }
}
