package com.codeprep;

/*
* parse - Converting a String to a LocalDateTime
* format - Converting a LocalDateTime to a String
*
* LocalDateTime combines both date and time information
*
* Common DateTime Patterns:
* - "yyyy-MM-dd'T'HH:mm:ss" -> 2025-01-17T14:30:45 (ISO format)
* - "dd/MM/yyyy HH:mm:ss" -> 17/01/2025 14:30:45
* - "MMM dd, yyyy hh:mm a" -> Jan 17, 2025 02:30 PM
* - "yyyy-MM-dd HH:mm:ss" -> 2025-01-17 14:30:45
* - "EEEE, MMMM dd, yyyy HH:mm:ss" -> Friday, January 17, 2025 14:30:45
* */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatLocalDateTimeExample {

    public static void parseLocalDateTime(){

        String dateTime = "2025-01-17T14:30:45"; // ISO format
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println("Parsed ISO_LOCAL_DATE_TIME: " + localDateTime);

        String dateTime2 = "2025-01-17T14:30:45";
        LocalDateTime localDateTime1 = LocalDateTime.parse(dateTime2);
        System.out.println("Parsed default format: " + localDateTime1);

        String dateTime3 = "2018|04|28|14|30|45";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy|MM|dd|HH|mm|ss");
        LocalDateTime localDateTime2 = LocalDateTime.parse(dateTime3, dateTimeFormatter);
        System.out.println("Parsed custom delimiter: " + localDateTime2);

        String dateTime4 = "17/01/2025 14:30:45";
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime localDateTime3 = LocalDateTime.parse(dateTime4, dateTimeFormatter2);
        System.out.println("Parsed dd/MM/yyyy format: " + localDateTime3);

        String dateTime5 = "Jan 17, 2025 02:30:45 PM";
        DateTimeFormatter dateTimeFormatter3 = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH);
        LocalDateTime localDateTime4 = LocalDateTime.parse(dateTime5, dateTimeFormatter3);
        System.out.println("Parsed 12-hour format: " + localDateTime4);
    }

    public static void formatLocalDateTime(){

        LocalDateTime localDateTime = LocalDateTime.now();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy|MM|dd|HH|mm|ss");
        String formattedDateTime = localDateTime.format(dateTimeFormatter);
        System.out.println("Formatted (custom delimiter): " + formattedDateTime);

        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime2 = localDateTime.format(dateTimeFormatter2);
        System.out.println("Formatted (dd/MM/yyyy): " + formattedDateTime2);

        DateTimeFormatter dateTimeFormatter3 = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH);
        String formattedDateTime3 = localDateTime.format(dateTimeFormatter3);
        System.out.println("Formatted (12-hour): " + formattedDateTime3);

        String isoDateTime = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println("Formatted (ISO): " + isoDateTime);

        DateTimeFormatter dateTimeFormatter4 = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
        String formattedDateTime4 = localDateTime.format(dateTimeFormatter4);
        System.out.println("Formatted (full): " + formattedDateTime4);
    }

    public static void main(String[] args) {

        parseLocalDateTime();
        System.out.println();
        formatLocalDateTime();
    }
}
