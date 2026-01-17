package com.codeprep;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/*
 * Duration Class - Part of java.time package (Java 8+)
 *
 * Duration represents a time-based amount of time (e.g., "34.5 seconds").
 * It measures time in seconds and nanoseconds and is ideal for measuring
 * time intervals between two Instant or LocalTime objects.
 *
 * Key Characteristics:
 * - Immutable and thread-safe
 * - Measures time in seconds and nanoseconds
 * - Does not work with dates (use Period for date-based durations)
 * - Precision up to nanoseconds
 *
 * Common Methods:
 *
 * Creation Methods:
 * - Duration.ofDays(long days) - Creates duration from days
 * - Duration.ofHours(long hours) - Creates duration from hours
 * - Duration.ofMinutes(long minutes) - Creates duration from minutes
 * - Duration.ofSeconds(long seconds) - Creates duration from seconds
 * - Duration.ofMillis(long millis) - Creates duration from milliseconds
 * - Duration.ofNanos(long nanos) - Creates duration from nanoseconds
 * - Duration.between(Temporal start, Temporal end) - Duration between two time points
 * - Duration.parse(CharSequence text) - Parses string like "PT20.345S" or "PT15M"
 *
 * Extraction Methods:
 * - getSeconds() - Gets total seconds in duration
 * - getNano() - Gets nanoseconds component (0-999,999,999)
 * - toDays() - Converts duration to days
 * - toHours() - Converts duration to hours
 * - toMinutes() - Converts duration to minutes
 * - toMillis() - Converts duration to milliseconds
 * - toNanos() - Converts duration to nanoseconds
 *
 * Arithmetic Operations:
 * - plus(Duration) / plusDays/Hours/Minutes/Seconds/Millis/Nanos() - Adds duration
 * - minus(Duration) / minusDays/Hours/Minutes/Seconds/Millis/Nanos() - Subtracts duration
 * - multipliedBy(long multiplicand) - Multiplies duration
 * - dividedBy(long divisor) - Divides duration
 * - negated() - Returns negative of duration
 * - abs() - Returns absolute value of duration
 *
 * Comparison Methods:
 * - compareTo(Duration other) - Compares two durations
 * - isNegative() - Checks if duration is negative
 * - isZero() - Checks if duration is zero
 *
 * Common Use Cases:
 * 1. Measuring execution time of code blocks
 * 2. Calculating time differences between events
 * 3. Setting timeouts and delays
 * 4. Performance monitoring and benchmarking
 * 5. Scheduling and time-based operations
 * 6. API rate limiting and throttling
 * 7. Session timeout management
 * 8. Cache expiration times
 *
 * Example Format (ISO-8601):
 * - "PT20.345S" - 20.345 seconds
 * - "PT15M" - 15 minutes
 * - "PT10H" - 10 hours
 * - "P2D" - 2 days (but prefer Period for date-based durations)
 * - "PT2H30M" - 2 hours and 30 minutes
 */
public class DurationExample {
    public static void main(String[] args) {

        LocalTime localTime1 = LocalTime.of(7, 20);
        LocalTime localTime2 = LocalTime.of(8, 20);

        long diff = localTime1.until(localTime2, ChronoUnit.MINUTES);
        System.out.println("diff: " + diff);

        Duration duration = Duration.between(localTime1, localTime2);
        System.out.println("to minutes: " + duration.toMinutes());
    }
}
