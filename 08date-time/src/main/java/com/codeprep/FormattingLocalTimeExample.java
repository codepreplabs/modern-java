package com.codeprep;

/*
* parse - Converting a String to a LocalTime
* format - Converting a LocalTime to a String
*
* Common Time Patterns:
* - "HH:mm:ss" -> 14:30:45 (24-hour format)
* - "hh:mm:ss a" -> 02:30:45 PM (12-hour format with AM/PM)
* - "HH:mm" -> 14:30 (without seconds)
* - "HH|mm|ss" -> 14|30|45 (custom delimiter)
* */

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormattingLocalTimeExample {

    public static void parseLocalTime(){

        String time = "14:30:45"; // HH:mm:ss
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
        System.out.println("Parsed ISO_LOCAL_TIME: " + localTime);

        String time2 = "14:30";
        LocalTime localTime1 = LocalTime.parse(time2);
        System.out.println("Parsed default format: " + localTime1);

        String time3 = "14|30|45";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH|mm|ss");
        LocalTime localTime2 = LocalTime.parse(time3, dateTimeFormatter);
        System.out.println("Parsed custom format: " + localTime2);

        String time4 = "02:30:45 PM";
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH);
        LocalTime localTime3 = LocalTime.parse(time4, dateTimeFormatter2);
        System.out.println("Parsed 12-hour format: " + localTime3);
    }

    public static void formatLocalTime(){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH|mm|ss");
        LocalTime localTime = LocalTime.now();
        String formattedTime = localTime.format(dateTimeFormatter);
        System.out.println("Formatted time (custom): " + formattedTime);

        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH);
        String formattedTime2 = localTime.format(dateTimeFormatter2);
        System.out.println("Formatted time (12-hour): " + formattedTime2);

        String isoTime = localTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
        System.out.println("Formatted time (ISO): " + isoTime);
    }

    public static void main(String[] args) {

        parseLocalTime();
        System.out.println();
        formatLocalTime();
    }
}
