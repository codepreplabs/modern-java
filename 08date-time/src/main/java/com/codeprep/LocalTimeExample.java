package com.codeprep;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/*
 * LocalTime - A time without time-zone in the ISO-8601 calendar system (e.g., 10:15:30)
 * Part of the java.time package introduced in Java 8 as a replacement for java.util.Date
 *
 * Key Characteristics:
 * - Immutable and thread-safe
 * - Represents time only (no date or timezone information)
 * - All modification methods return a new instance
 * - Precision up to nanoseconds
 *
 * Commonly Used Methods:
 *
 * 1. CREATION METHODS:
 *    - now() - Gets current time from system clock
 *    - of(hour, minute) - Creates time with hour and minute
 *    - of(hour, minute, second) - Creates time with hour, minute, and second
 *    - of(hour, minute, second, nano) - Creates time with full precision
 *    - parse(String) - Parses time from text in ISO format (e.g., "10:15:30")
 *    - parse(String, DateTimeFormatter) - Parses time with custom formatter
 *
 * 2. GETTER METHODS:
 *    - getHour() - Returns the hour (0-23)
 *    - getMinute() - Returns the minute (0-59)
 *    - getSecond() - Returns the second (0-59)
 *    - getNano() - Returns the nanosecond (0-999,999,999)
 *
 * 3. ARITHMETIC OPERATIONS (all return new LocalTime):
 *    - plusHours(long) - Adds specified hours
 *    - plusMinutes(long) - Adds specified minutes
 *    - plusSeconds(long) - Adds specified seconds
 *    - plusNanos(long) - Adds specified nanoseconds
 *    - minusHours(long) - Subtracts specified hours
 *    - minusMinutes(long) - Subtracts specified minutes
 *    - minusSeconds(long) - Subtracts specified seconds
 *    - minusNanos(long) - Subtracts specified nanoseconds
 *    - plus/minus(long, TemporalUnit) - Generic addition/subtraction with ChronoUnit
 *
 * 4. MODIFICATION METHODS (all return new LocalTime):
 *    - withHour(int) - Returns copy with hour changed
 *    - withMinute(int) - Returns copy with minute changed
 *    - withSecond(int) - Returns copy with second changed
 *    - withNano(int) - Returns copy with nanosecond changed
 *    - with(TemporalAdjuster) - Adjusts time using temporal adjuster
 *      (e.g., LocalTime.MIDNIGHT, LocalTime.NOON, LocalTime.MAX, LocalTime.MIN)
 *    - with(TemporalField, long) - Sets specific field value using ChronoField
 *
 * 5. COMPARISON METHODS:
 *    - isAfter(LocalTime) - Checks if this time is after another
 *    - isBefore(LocalTime) - Checks if this time is before another
 *    - compareTo(LocalTime) - Compares times, returns -1 (before), 0 (equal), or 1 (after)
 *    - equals(Object) - Checks if times are equal
 *
 * 6. UTILITY METHODS:
 *    - toSecondOfDay() - Converts time to seconds since midnight (0-86399)
 *    - toNanoOfDay() - Converts time to nanoseconds since midnight
 *    - until(LocalTime, TemporalUnit) - Calculates amount of time until another time
 *
 * Constants:
 *    - LocalTime.MIDNIGHT - 00:00:00
 *    - LocalTime.NOON - 12:00:00
 *    - LocalTime.MIN - 00:00:00 (earliest time)
 *    - LocalTime.MAX - 23:59:59.999999999 (latest time)
 *
 * Example Usage:
 *    LocalTime now = LocalTime.now();
 *    LocalTime meeting = LocalTime.of(14, 30);
 *    LocalTime laterTime = now.plusHours(2);
 *    boolean isAfter = now.isAfter(meeting);
 */
public class LocalTimeExample {

    public static void main(String[] args) {

        LocalTime localTime = LocalTime.now();
        System.out.println("localtime: " + localTime);

        LocalTime localTime1 = LocalTime.of(5, 30, 33);
        System.out.println(localTime1);

        LocalTime localTime2 = LocalTime.of(5, 30);
        System.out.println(localTime2);

        /*
        * getting values from local time instance
        * */

        System.out.println("getHour: " + localTime.getHour());
        System.out.println("getMinute: " + localTime.getMinute());

        /*
        * Modify the values of local time
        * */

        System.out.println(localTime.minusHours(1));
        System.out.println(localTime.minusMinutes(30));
        System.out.println(localTime.minus(3, ChronoUnit.HOURS));

        System.out.println(localTime.with(LocalTime.MIDNIGHT));
        System.out.println(localTime.with(ChronoField.HOUR_OF_DAY, 22));

        System.out.println(localTime.plusMinutes(10));
        System.out.println(localTime.withHour(2));

        /*
        * Comparison methods
        * */

        LocalTime time1 = LocalTime.of(10, 30);
        LocalTime time2 = LocalTime.of(14, 45);
        LocalTime time3 = LocalTime.of(10, 30);

        System.out.println("\nComparison Examples:");
        System.out.println("time1 (10:30): " + time1);
        System.out.println("time2 (14:45): " + time2);
        System.out.println("time3 (10:30): " + time3);

        System.out.println("\nisAfter examples:");
        System.out.println("time2.isAfter(time1): " + time2.isAfter(time1)); // true
        System.out.println("time1.isAfter(time2): " + time1.isAfter(time2)); // false
        System.out.println("time1.isAfter(time3): " + time1.isAfter(time3)); // false

        System.out.println("\nisBefore examples:");
        System.out.println("time1.isBefore(time2): " + time1.isBefore(time2)); // true
        System.out.println("time2.isBefore(time1): " + time2.isBefore(time1)); // false
        System.out.println("time1.isBefore(time3): " + time1.isBefore(time3)); // false

        System.out.println("\nequals examples:");
        System.out.println("time1.equals(time2): " + time1.equals(time2)); // false
        System.out.println("time1.equals(time3): " + time1.equals(time3)); // true

        System.out.println("\ncompareTo examples:");
        System.out.println("time1.compareTo(time2): " + time1.compareTo(time2)); // negative (time1 < time2)
        System.out.println("time2.compareTo(time1): " + time2.compareTo(time1)); // positive (time2 > time1)
        System.out.println("time1.compareTo(time3): " + time1.compareTo(time3)); // 0 (equal)
    }
}
