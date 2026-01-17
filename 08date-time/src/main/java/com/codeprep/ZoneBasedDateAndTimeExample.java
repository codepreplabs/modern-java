package com.codeprep;

import java.time.*;

public class ZoneBasedDateAndTimeExample {

    public static void main(String[] args) {

        // ZoneId - Represents a time zone identifier (e.g., "America/New_York", "Asia/Tokyo")
        // Used to identify a region where the same standard time is used
        // It handles daylight saving time (DST) rules automatically
        ZoneId zoneId = ZoneId.of("America/New_York");
        System.out.println("ZoneId: " + zoneId);

        // ZoneOffset - Represents a fixed offset from UTC/Greenwich (e.g., +05:30, -08:00)
        // It's a fixed time difference and doesn't handle DST changes
        // Useful when you need a constant offset without time zone rules
        ZoneOffset zoneOffset = ZoneOffset.of("+05:30");
        System.out.println("ZoneOffset: " + zoneOffset);

        // ZonedDateTime - Represents a date-time with a time zone in the ISO-8601 calendar system
        // Combines LocalDateTime with ZoneId to include time zone information
        // Automatically adjusts for daylight saving time based on the zone rules
        // Format: 2026-01-17T10:30:00+05:30[Asia/Kolkata]
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println("ZonedDateTime (current): " + zonedDateTime);

        // get the offset time
        System.out.println("offset : " +zonedDateTime.getOffset());

        // get the zone id
        System.out.println("zoneId : "+ zonedDateTime.getZone());

        // Creating ZonedDateTime with specific zone (CST. EST. MST, PST)
        System.out.println("Chicago CST : " + ZonedDateTime.now(ZoneId.of("America/Chicago")));
        System.out.println("Detroit EST : " + ZonedDateTime.now(ZoneId.of("America/Detroit")));
        System.out.println("LA PST : " + ZonedDateTime.now(ZoneId.of("America/Los_Angeles")));
        System.out.println("Denver PST : " + ZonedDateTime.now(ZoneId.of("America/Denver")));

        // Creating ZonedDateTime from LocalDateTime and ZoneId
        LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 17, 14, 30);
        ZonedDateTime zonedWithZoneId = localDateTime.atZone(ZoneId.of("Europe/Paris"));
        System.out.println("Paris Time: " + zonedWithZoneId);

        // Creating a ZoneDateTime from Instant and ZoneId
        ZonedDateTime zonedDateTime1 = Instant.now().atZone(ZoneId.of("America/Detroit"));
        System.out.println("Detroit time: " + zonedDateTime1);

        // Converting between time zones
        ZonedDateTime tokyoTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        System.out.println("Tokyo Time: " + tokyoTime);

        // Available zones
        long zoneCount = ZoneId.getAvailableZoneIds()
                .stream()
                .count();
        System.out.println("total number of zones: "+ zoneCount);
    }
}
