package com.codeprep;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/*
 * LocalDate - A date without time-zone in the ISO-8601 calendar system (e.g., 2025-01-16)
 * Part of the java.time package introduced in Java 8 as a replacement for java.util.Date
 *
 * Key Characteristics:
 * - Immutable and thread-safe
 * - Represents date only (no time or timezone information)
 * - All modification methods return a new instance
 *
 * Commonly Used Methods:
 *
 * 1. CREATION METHODS:
 *    - now() - Gets current date from system clock
 *    - of(year, month, day) - Creates date with specific year, month, and day
 *    - parse(String) - Parses date from text in ISO format (e.g., "2025-01-16")
 *    - parse(String, DateTimeFormatter) - Parses date with custom formatter
 *
 * 2. GETTER METHODS:
 *    - getYear() - Returns the year (e.g., 2025)
 *    - getMonth() - Returns the Month enum (e.g., JANUARY)
 *    - getMonthValue() - Returns month as int (1-12)
 *    - getDayOfMonth() - Returns day of month (1-31)
 *    - getDayOfWeek() - Returns DayOfWeek enum (e.g., MONDAY, TUESDAY)
 *    - getDayOfYear() - Returns day of year (1-366)
 *
 * 3. ARITHMETIC OPERATIONS (all return new LocalDate):
 *    - plusDays(long) - Adds specified days
 *    - plusWeeks(long) - Adds specified weeks
 *    - plusMonths(long) - Adds specified months
 *    - plusYears(long) - Adds specified years
 *    - minusDays(long) - Subtracts specified days
 *    - minusWeeks(long) - Subtracts specified weeks
 *    - minusMonths(long) - Subtracts specified months
 *    - minusYears(long) - Subtracts specified years
 *    - plus/minus(long, TemporalUnit) - Generic addition/subtraction with ChronoUnit
 *
 * 4. MODIFICATION METHODS (all return new LocalDate):
 *    - withYear(int) - Returns copy with year changed
 *    - withMonth(int) - Returns copy with month changed
 *    - withDayOfMonth(int) - Returns copy with day of month changed
 *    - with(TemporalAdjuster) - Adjusts date using TemporalAdjusters
 *      (e.g., firstDayOfMonth(), lastDayOfMonth(), firstDayOfNextMonth())
 *    - with(TemporalField, long) - Sets specific field value using ChronoField
 *
 * 5. COMPARISON METHODS:
 *    - isAfter(LocalDate) - Checks if this date is after another
 *    - isBefore(LocalDate) - Checks if this date is before another
 *    - isEqual(LocalDate) - Checks if dates are equal
 *    - compareTo(LocalDate) - Compares dates, returns -1 (before), 0 (equal), or 1 (after)
 *
 * 6. UTILITY METHODS:
 *    - isLeapYear() - Checks if year is a leap year
 *    - lengthOfMonth() - Returns number of days in month (28-31)
 *    - lengthOfYear() - Returns number of days in year (365 or 366)
 *    - until(LocalDate, TemporalUnit) - Calculates amount of time until another date
 *
 * Example Usage:
 *    LocalDate today = LocalDate.now();
 *    LocalDate birthday = LocalDate.of(1990, 5, 15);
 *    LocalDate nextWeek = today.plusWeeks(1);
 *    boolean isAfter = today.isAfter(birthday);
 */
public class LocalDateExample {

    public static void main(String[] args) {

        LocalDate localDate = LocalDate.now();
        System.out.println("Current date is : " + localDate);

        LocalDate localDate1 = LocalDate.of(2025, 1, 16);
        System.out.println("today: " + localDate1);

        // getting values from local date
        System.out.println("Month: " + localDate.getMonth());
        System.out.println("Month value: " + localDate.getMonthValue());
        System.out.println("Day of the week: " + localDate.getDayOfWeek());
        System.out.println("Day of the year: " + localDate.getDayOfYear());
        System.out.println("year: " + localDate.getYear());
        System.out.println("Day of month using get: " + localDate.getDayOfMonth());

        /*
        * Modify Local date
        */

        System.out.println("plusDays: " + localDate.plusDays(2));
        System.out.println("plusMonth: " + localDate.plusMonths(2));
        System.out.println("minusDays: " + localDate.minusDays(2));
        System.out.println("withYear: " + localDate.withYear(2019));
        System.out.println("with chrono field: " + localDate.with(ChronoField.YEAR, 2020));
        System.out.println("with temporal adjusters: " + localDate.with(TemporalAdjusters.firstDayOfNextMonth()));
        System.out.println("chronounit minus: " + localDate.minus(1, ChronoUnit.YEARS));

        /*
        * Support methods
        */
        System.out.println("leap year: " + localDate.isLeapYear());
        System.out.println(localDate.isEqual(localDate1));
        System.out.println(localDate.isBefore(localDate1));
        System.out.println(localDate.isAfter(localDate1));
    }
}
