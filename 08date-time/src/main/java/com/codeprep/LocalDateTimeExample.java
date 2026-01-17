package com.codeprep;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

public class LocalDateTimeExample {

    public static void main(String[] args) {

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("localDateTime: " + localDateTime);

        LocalDateTime localDateTime1 = LocalDateTime.of(2025, 1, 16, 14, 52, 33, 23);
        System.out.println(localDateTime1);

        LocalDateTime localDateTime2 = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        System.out.println(localDateTime2);

        /*
        * getting the date and time from the LocalDateTime instance
        * */

        System.out.println("hour: " + localDateTime.getHour());
        System.out.println("minute: " + localDateTime.getMinute());
        System.out.println("getDayOfMonth: " + localDateTime.getDayOfMonth());

        System.out.println("get method: " + localDateTime.get(ChronoField.DAY_OF_MONTH));

        /*
        * modifying local date time
        * */

        System.out.println("plus hours: " + localDateTime.plusHours(2));
        System.out.println("minus days: " + localDateTime.minusDays(2));
        System.out.println("updated month: " + localDateTime.withMonth(12));

        /*
        * Converting LocalDate, LocalTime to LocalDateTime and vice-versa
        * */

        LocalDate localDate = LocalDate.of(2019, 01, 01);
        LocalDateTime localDateTime3 = localDate.atTime(23, 33);
        System.out.println("at time: " + localDate.atTime(23, 33));

        LocalTime localTime = LocalTime.of(23, 39);
        LocalDateTime localDateTime4 = localTime.atDate(localDate);
        System.out.println(localDateTime4);

        LocalDate localDate1 = localDateTime.toLocalDate();
        System.out.println("toLocalDate: " + localDate1);

        LocalTime localTime1 = localDateTime.toLocalTime();
        System.out.println("to LocalTime: " + localTime1);
    }
}
