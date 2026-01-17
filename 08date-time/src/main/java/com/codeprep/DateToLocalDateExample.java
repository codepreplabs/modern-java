package com.codeprep;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateToLocalDateExample {

    public static void main(String[] args) {

        // Legacy java.util.Date - represents a specific instant in time
        Date date = new Date();
        System.out.println("java.util.Date: " + date);

        // Converting java.util.Date to LocalDate
        // Step 1: Convert Date to Instant
        // Step 2: Apply ZoneId to get ZonedDateTime
        // Step 3: Extract LocalDate from ZonedDateTime
        LocalDate localDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        System.out.println("Converted to LocalDate: " + localDate);

        // Converting java.util.Date to LocalDateTime
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        System.out.println("Converted to LocalDateTime: " + localDateTime);

        // Converting java.util.Date to ZonedDateTime
        ZonedDateTime zonedDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault());
        System.out.println("Converted to ZonedDateTime: " + zonedDateTime);

        // Converting LocalDate back to java.util.Date
        LocalDate today = LocalDate.now();
        Date dateFromLocalDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println("LocalDate to java.util.Date: " + dateFromLocalDate);

        // java.sql.Date - extends java.util.Date, used for SQL DATE type
        // Constructor requires a long timestamp (milliseconds since epoch)
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        System.out.println("java.sql.Date: " + sqlDate);

        // Converting java.sql.Date to LocalDate (simpler method available)
        LocalDate localDateFromSql = sqlDate.toLocalDate();
        System.out.println("java.sql.Date to LocalDate: " + localDateFromSql);

        // Converting LocalDate to java.sql.Date
        java.sql.Date sqlDateFromLocal = java.sql.Date.valueOf(today);
        System.out.println("LocalDate to java.sql.Date: " + sqlDateFromLocal);
    }
}
