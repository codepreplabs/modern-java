package com.codeprep;

/*
* parse - Converting a String to a LocalDate/LocalTime/LocalDateTime
* format - Converting a LocalDate/LocalTime/LocalDateTime to a String
*
* DateTimeFormatter Class:
* -----------------------
* DateTimeFormatter is used to format and parse date-time objects in Java 8+ Date/Time API.
* It is immutable and thread-safe, making it safe for concurrent use.
*
* Common Use Cases:
* 1. Parsing Strings to Date/Time objects - parse()
* 2. Formatting Date/Time objects to Strings - format()
* 3. Custom date patterns using ofPattern()
* 4. Localized formatting for different regions
*
* Predefined Formatters (ISO standards):
* - ISO_LOCAL_DATE: yyyy-MM-dd (e.g., "2025-01-17")
* - ISO_LOCAL_TIME: HH:mm:ss (e.g., "14:30:45")
* - ISO_LOCAL_DATE_TIME: yyyy-MM-dd'T'HH:mm:ss (e.g., "2025-01-17T14:30:45")
* - BASIC_ISO_DATE: yyyyMMdd (e.g., "20250117")
* - ISO_OFFSET_DATE_TIME: Date-time with offset (e.g., "2025-01-17T14:30:45+01:00")
*
* Custom Patterns (using ofPattern):
* - "dd/MM/yyyy" -> 17/01/2025
* - "MMM dd, yyyy" -> Jan 17, 2025
* - "yyyy-MM-dd HH:mm:ss" -> 2025-01-17 14:30:45
* - "EEEE, MMMM dd, yyyy" -> Friday, January 17, 2025
*
* Common Pattern Symbols:
* - y: Year (e.g., 2025)
* - M: Month in year (1-12 or Jan-Dec)
* - d: Day in month (1-31)
* - H: Hour in day (0-23)
* - m: Minute in hour (0-59)
* - s: Second in minute (0-59)
* - E: Day name in week (e.g., Mon, Monday)
* */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormattingLocalDateExample {

    public static void parseLocalDate(){

        String date = "2025-01-17"; // yyyy-mm-dd
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println(localDate);

        String date2 = "20250117"; // yyyymmdd
        LocalDate localDate1 = LocalDate.parse(date2, DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(localDate1);
        
        String date3 = "2018|04|28";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy|MM|dd");
        LocalDate localDate2 = LocalDate.parse(date3, dateTimeFormatter);
        System.out.println(localDate2);

    }

    public static void formatLocalDate(){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy|MM|dd");
        LocalDate localDate = LocalDate.now();
        String formattedDate = localDate.format(dateTimeFormatter);
        System.out.println("formatted date: " + formattedDate);
    }

    public static void main(String[] args) {

        parseLocalDate();
        formatLocalDate();
    }
}
