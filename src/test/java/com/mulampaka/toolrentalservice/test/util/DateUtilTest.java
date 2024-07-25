package com.mulampaka.toolrentalservice.test.util;

import com.mulampaka.toolrentalservice.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class DateUtilTest {


    @Test
    public void testIsHoliday () throws  Exception {
        // Independence day
        LocalDate holiday = LocalDate.of(2024, Month.JULY, 4);
        boolean isHoliday = DateUtil.isHoliday(holiday);
        assertTrue (isHoliday);
        // Labor Day
        holiday = LocalDate.of(2024, Month.SEPTEMBER, 2);
        isHoliday = DateUtil.isHoliday(holiday);
        assertTrue (isHoliday);
    }

    @Test
    public void testIsNotHoliday () throws Exception {
        LocalDate notAHoliday = LocalDate.of(2024, Month.JULY, 1);
        boolean isHoliday = DateUtil.isHoliday(notAHoliday);
        assertFalse (isHoliday);
    }


    @Test
    public void testIsWeekend () throws Exception {
        LocalDate saturday = LocalDate.of(2024, Month.JULY, 20);
        boolean isSaturday = DateUtil.isWeekend(saturday);
        assertTrue(isSaturday);
        LocalDate sunday = LocalDate.of(2024, Month.JULY, 20);
        boolean isSunday = DateUtil.isWeekend(sunday);
        assertTrue(isSunday);
    }

    @Test
    public void testIsWeekday () throws Exception {
        LocalDate monday = LocalDate.of(2024, Month.JULY, 22);
        boolean isWeekend = DateUtil.isWeekend(monday);
        assertFalse(isWeekend);
    }
}
