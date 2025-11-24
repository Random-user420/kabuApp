package org.kabuapp.kabuapp.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeUtils {
    private DateTimeUtils() {}

    public static LocalTime getLocalTime() {
        return LocalTime.now();
    }

    public static LocalDate getLocalDate() {
        return LocalDate.now();
    }
}
