package application;

import application.configuration.TimeStampParser;
import application.exceptions.DuplicateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

public class Main {
    public static void main(String[] args) {
        Timestamp got = TimeStampParser.toTimeStamp("2024-01-03T10:18:08.108Z");
        System.out.println(got);
    }
}
