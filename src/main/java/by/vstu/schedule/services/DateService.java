package by.vstu.schedule.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateService {

    public static final DateTimeFormatter BACK_DATE_FORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();
    public static final DateTimeFormatter FRONT_DATE_FORMAT = new DateTimeFormatterBuilder().appendPattern("dd.MM.yyyy").toFormatter();
    public static final DateTimeFormatter FRONT_TIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("HH:mm").toFormatter();

    public static String checkWeekType(LocalDate date) {
        LocalDate startDate = LocalDate.parse(new StringBuilder().append(date.getYear()).append(date.getMonth().getValue() >= 9 ||
                date.getMonth().getValue() == 1 ? "-09" : "-02").append("-01"));
        if (startDate.getDayOfWeek() == DayOfWeek.SATURDAY || startDate.getDayOfWeek() == DayOfWeek.SUNDAY)
            startDate = startDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        int datesDifference = date.get(WeekFields.of(Locale.getDefault()).weekOfYear()) - startDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        if (datesDifference % 2 == 0)
            return "NUMERATOR";
        else
            return "DENOMINATOR";
    }

    public static LocalDate[] evaluateDatePeriod(LocalDate startDate) {
        LocalDate firstDayOfWeek = startDate.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(6);
        return new LocalDate[] {firstDayOfWeek, lastDayOfWeek};
    }

    public static LocalDate[] evaluateDatePeriod() {
        LocalDate firstDayOfWeek = LocalDate.now().with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(6);
        return new LocalDate[] {firstDayOfWeek, lastDayOfWeek};
    }
}
