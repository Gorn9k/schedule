package by.vstu.schedule.services;

import by.vstu.schedule.models.DTO.DayOfWeekLessonNumber;
import by.vstu.schedule.models.DTO.Schedule;
import by.vstu.schedule.models.DTO.ScheduleResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleService {

    ScheduleResponse getAllScheduleByFrameAndStartDateAndEndDate(String frame, LocalDate startDate, LocalDate endDate);
}
