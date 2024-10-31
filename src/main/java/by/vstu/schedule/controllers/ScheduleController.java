package by.vstu.schedule.controllers;

import by.vstu.schedule.models.DTO.ScheduleResponse;
import by.vstu.schedule.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ScheduleResponse getSchedulePage(@RequestParam String startDate,
                                            @RequestParam String endDate,
                                            @RequestParam String frame) {
        return scheduleService.getAllScheduleByFrameAndStartDateAndEndDate(frame, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }
}
