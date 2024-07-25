package com.mulampaka.toolrentalservice.util;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;

import static java.time.temporal.TemporalAdjusters.firstInMonth;

/**
 * Utility class to validate dates
 */
@Slf4j
public class DateUtil {

    /**
     * Checks if the specified date is a holiday.
     * Currently checks for Independence day and Labor day only
     * @param day LocalDate
     * @return boolean - true if holiday otherwise false
     */
    public static boolean isHoliday(LocalDate day) {
        log.debug("Checking if date is a holiday:{}", day);
        // check for Independence day - July 4th
        // -If falls on weekend, it is observed on the closest weekday (if Sat, then Friday before, if Sunday, then Monday after)
        LocalDate july = day.withMonth(Month.JULY.getValue());
        LocalDate july4th = july.withDayOfMonth(4);
        DayOfWeek dayOfWeek = july4th.getDayOfWeek();
        LocalDate independenceDay = july4th;
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            independenceDay = july.withDayOfMonth(3);
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            independenceDay = july.withDayOfMonth(5);
        }
        log.trace("Independence Day:{}", independenceDay);
        if (day.equals(independenceDay)) {
            return true;
        }
        // check for labor day
        LocalDate sep = day.withMonth(Month.SEPTEMBER.getValue());
        LocalDate laborDay = sep.with(firstInMonth(DayOfWeek.MONDAY));
        log.debug("Labor Day:{}", laborDay);
        if (day.equals(laborDay)) {
            return true;
        }
        return false;
    }


    /**
     * Checks if the specified day is weekend
     * @param day LocalDate
     * @return boolean - true if weekend otherwise false
     */
    public static boolean isWeekend(final LocalDate day) {
        DayOfWeek dayOfWeek = DayOfWeek.of(day.get(ChronoField.DAY_OF_WEEK));
        return dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY;
    }


}
