package deserializer;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class UtilAdapter {
    public static ZonedDateTime readDateTime(JsonReader in) throws IOException {
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

    public static LocalDate readLocalDate(JsonReader in) throws IOException {
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

    public static LocalTime readLocalTime(JsonReader in) throws IOException {
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

    public static ZoneId readZoneId(JsonReader in) throws IOException {
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
