package application.configuration;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampParser {
    public static Timestamp toTimeStamp(String str){
        if(str == null)
            return null;
        String s = ZonedDateTime.parse(str).format(DateTimeFormatter.ISO_INSTANT);
        return new Timestamp(ZonedDateTime.parse(s).toLocalDateTime().toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    public static String fromTimestamp(Timestamp timestamp){
        if(timestamp == null)
            return null;
        return timestamp.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
