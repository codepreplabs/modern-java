package com.codeprep;

import java.time.LocalDate;
import java.time.Period;

/*
 * Period Class - Java 8 Date/Time API
 *
 * The Period class represents a date-based amount of time in the ISO-8601 calendar system,
 * such as "2 years, 3 months, and 4 days". It is used to measure time in terms of years,
 * months, and days (without time components like hours, minutes, or seconds).
 *
 * Key Characteristics:
 * - Immutable and thread-safe
 * - Represents a quantity of time in terms of years, months, and days
 * - Part of java.time package (introduced in Java 8)
 * - Works with LocalDate, LocalDateTime, and ZonedDateTime
 *
 * Important Use Cases:
 *
 * 1. Calculating Age:
 *    - Calculate a person's age from birth date to current date
 *    - Example: Period.between(birthDate, LocalDate.now())
 *
 * 2. Date Arithmetic:
 *    - Add or subtract periods from dates
 *    - Example: date.plus(Period.ofMonths(3)) or date.minus(Period.ofYears(1))
 *
 * 3. Subscription/Membership Duration:
 *    - Calculate time remaining in subscriptions, memberships, or contracts
 *    - Example: Period.between(startDate, endDate)
 *
 * 4. Project Timeline Management:
 *    - Calculate project duration in years, months, and days
 *    - Useful for project planning and milestone tracking
 *
 * 5. Legal/Financial Calculations:
 *    - Calculate tenure, service periods, loan durations
 *    - Age verification for legal requirements (e.g., voting age, retirement age)
 *
 * 6. Date Range Validation:
 *    - Check if a certain period has elapsed between two dates
 *    - Validate minimum/maximum age requirements
 *
 * 7. Human-Readable Time Representation:
 *    - Display time differences in a user-friendly format
 *    - Example: "2 years, 3 months, and 15 days" instead of total days
 *
 * Common Methods:
 * - Period.of(years, months, days) - Create a period with specific values
 * - Period.ofYears(int), ofMonths(int), ofDays(int) - Create single-unit periods
 * - Period.between(LocalDate start, LocalDate end) - Calculate period between two dates
 * - getYears(), getMonths(), getDays() - Extract individual components
 * - plus/minus methods - Add or subtract periods
 * - isNegative(), isZero() - Check period characteristics
 *
 * Note: Period is different from Duration
 * - Period: Date-based (years, months, days)
 * - Duration: Time-based (hours, minutes, seconds, nanoseconds)
 */
public class PeriodExample {

    public static void main(String[] args) {

        LocalDate localDate1 = LocalDate.of(2025, 1, 1);
        LocalDate localDate2 = LocalDate.of(2025, 12, 31);

        Period period1 = localDate1.until(localDate2);

        System.out.println("Period between " + localDate1 + " and " + localDate2 + ":");
        System.out.println("Full Period: " + period1); // Shows the complete period
        System.out.println("Years: " + period1.getYears());
        System.out.println("Months: " + period1.getMonths());
        System.out.println("Days: " + period1.getDays());
        System.out.println("(This means: " + period1.getYears() + " years, " +
                          period1.getMonths() + " months, and " + period1.getDays() + " days)\n");

        Period period2 = Period.ofDays(10);
        System.out.println("days in period: " +period2.getDays());

        Period period3 = Period.ofYears(10);
        System.out.println("years in period: " + period3.getYears());
        System.out.println("months in period: " + period3.toTotalMonths());

        Period period4 = Period.between(localDate1, localDate2);
        System.out.println("\nPeriod.between() result:");
        System.out.println("Full Period: " + period4);
        System.out.println("Days component only: " + period4.getDays());
        System.out.println("(Same as period1 - both show 11 months and 30 days)");
    }
}
