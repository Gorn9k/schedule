package by.vstu.schedule.controllers;

import by.vstu.schedule.models.DTO.DayOfWeekLessonNumber;
import by.vstu.schedule.models.DTO.Schedule;
import by.vstu.schedule.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    @CrossOrigin
    public Map<String, Map<String, List<Schedule>>> getSchedulePage(@RequestParam String startDate,
                                                                    @RequestParam String endDate,
                                                                    @RequestParam String frame) {
        return scheduleService.getAllScheduleByFrameAndStartDateAndEndDate(frame, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @ExceptionHandler(Exception.class)
    public String redirectToMainPage(Exception exception) {
        System.out.println(exception.getMessage());
        return "redirect:/";
    }
}
