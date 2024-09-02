package by.vstu.schedule.services;

import by.vstu.schedule.models.DTO.DayOfWeekLessonNumber;
import by.vstu.schedule.models.DTO.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleService {

    Map<String, Map<String, List<Schedule>>> getAllScheduleByFrameAndStartDateAndEndDate(String frame, LocalDate startDate, LocalDate endDate);
}
