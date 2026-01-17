package com.codeprep;

/*
 * Instant Class in Java 8+ (java.time.Instant)
 *
 * Overview:
 * - Represents a point in time on the timeline (a timestamp)
 * - Stores time as seconds and nanoseconds since the Unix epoch (January 1, 1970 00:00:00 UTC)
 * - Immutable and thread-safe
 * - Part of the java.time package introduced in Java 8
 *
 * Commonly Used Methods:
 *
 * 1. Creating Instant objects:
 *    - Instant.now()                           // Current timestamp
 *    - Instant.ofEpochSecond(long)             // From epoch seconds
 *    - Instant.ofEpochMilli(long)              // From epoch milliseconds
 *    - Instant.parse("2026-01-16T10:15:30Z")   // From ISO-8601 string
 *
 * 2. Retrieving information:
 *    - getEpochSecond()                        // Get seconds since epoch
 *    - toEpochMilli()                          // Get milliseconds since epoch
 *    - getNano()                               // Get nanoseconds part
 *
 * 3. Comparison methods:
 *    - isBefore(Instant)                       // Check if before another instant
 *    - isAfter(Instant)                        // Check if after another instant
 *    - compareTo(Instant)                      // Compare two instants
 *
 * 4. Manipulation methods:
 *    - plus(long, TemporalUnit)                // Add time
 *    - plusSeconds(long)                       // Add seconds
 *    - plusMillis(long)                        // Add milliseconds
 *    - minus(long, TemporalUnit)               // Subtract time
 *    - minusSeconds(long)                      // Subtract seconds
 *
 * 5. Conversion methods:
 *    - atZone(ZoneId)                          // Convert to ZonedDateTime
 *    - toString()                              // ISO-8601 format string
 *
 * Common Use Cases:
 *
 * 1. Timestamps for logging and auditing:
 *    - Recording when events occur in applications
 *    - Database timestamps (created_at, updated_at)
 *
 * 2. Measuring elapsed time:
 *    - Performance monitoring and benchmarking
 *    - Calculating duration between two points
 *
 * 3. API responses and data exchange:
 *    - REST API timestamps (ISO-8601 format)
 *    - Inter-system communication with consistent time representation
 *
 * 4. Scheduling and time-based operations:
 *    - Token expiration times
 *    - Session timeout tracking
 *
 * 5. Working with UTC time:
 *    - When you need timezone-independent timestamps
 *    - Storing time in databases in UTC format
 *
 * Important Notes:
 * - Instant is always in UTC (no timezone information)
 * - For human-readable dates/times with timezones, use ZonedDateTime or LocalDateTime
 * - Instant is best for machine timestamps, not for displaying to users
 */

import java.time.Duration;
import java.time.Instant;

public class InstantExample {

    public static void main(String[] args) {

        Instant instant = Instant.now();
        System.out.println(instant);
        System.out.println("epoch time: " + instant.getEpochSecond());

        Instant instant1 = Instant.ofEpochSecond(0);
        System.out.println(instant1);

        Duration duration = Duration.between(instant1, instant);
        System.out.println(duration.getSeconds());
    }
}
