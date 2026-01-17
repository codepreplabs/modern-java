package com.codeprep;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/*
 * Java 8 Date and Time API (java.time package)
 *
 * Java 8 introduced a new Date and Time API to address the shortcomings of the old java.util.Date
 * and java.util.Calendar classes. Key additions include:
 *
 * 1. LocalDate - Represents a date without time or timezone (e.g., 2026-01-15)
 * 2. LocalTime - Represents a time without date or timezone (e.g., 10:30:45)
 * 3. LocalDateTime - Represents both date and time without timezone (e.g., 2026-01-15T10:30:45)
 * 4. ZonedDateTime - Represents date and time with timezone
 * 5. Instant - Represents a timestamp (point in time)
 * 6. Period - Represents a date-based amount of time (years, months, days)
 * 7. Duration - Represents a time-based amount of time (hours, minutes, seconds)
 *
 * Benefits:
 * - Immutable and thread-safe
 * - Clear and consistent API design
 * - Better separation of concerns (date, time, timezone)
 * - Follows ISO-8601 calendar system by default
 */
public class LocalDateAndTimeExamples {

    public static void main(String[] args) {

        // localDate
        LocalDate localDate = LocalDate.now();
        System.out.println("localDate: " + localDate);

        // LocalTime
        LocalTime localTime = LocalTime.now();
        System.out.println("localtime : " + localTime);

        // LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("localDateTime" + localDateTime);
    }
}