package by.vstu.schedule.services;

import by.vstu.schedule.models.DTO.ScheduleResponse;

import java.time.LocalDate;

public interface ScheduleService {

    ScheduleResponse getAllScheduleByFrameAndStartDateAndEndDate(String frame, LocalDate startDate, LocalDate endDate);
}
