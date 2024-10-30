package by.vstu.schedule.services;

import by.vstu.schedule.models.DTO.DayOfWeekLessonNumber;
import by.vstu.schedule.models.DTO.Schedule;
import by.vstu.schedule.models.DTO.ScheduleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static by.vstu.schedule.services.DateService.*;

@RequiredArgsConstructor
public class RestClientScheduleService implements ScheduleService {

    private final RestClient restClient;

    private final Map<String, String[]> frame_classroomNumbers_Map;

    @Override
    public ScheduleResponse getAllScheduleByFrameAndStartDateAndEndDate(String frame,
                                                                        LocalDate startDate,
                                                                        LocalDate endDate) {
        String[] classes = frame_classroomNumbers_Map.get(switch (frame) {
            case "FIRST" -> "firstFrame";
            case "FOURTH" -> "fourthFrame";
            default -> throw new IllegalStateException("Unexpected value: " + frame);
        });

        Set<Schedule> schedules = new HashSet<>() {{
            add(new Schedule(1L,
                    "122",
                    1, 1, "FIRST", "123", "123", "123"));
        }};
//                this.restClient
//                .get()
//                .uri(String.format("/api/rooms/byRoomAndDate?weekType=%s,ALWAYS&startDate=%s&endDate=%s&roomNumbers=%s&frame=%s",
//                        checkWeekType(startDate),
//                        startDate,
//                        endDate,
//                        Arrays.toString(classes).replaceAll("[\\[\\] ]", ""),
//                        frame))
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {
//                });

        schedules = schedules.stream().sorted(Comparator.comparing(Schedule::day)).sorted(Comparator.comparing(Schedule::lessonNumber))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        SortedSet<String> classesNumbers = schedules.stream().map(Schedule::roomNumber).collect(Collectors.toCollection(TreeSet::new));
        SortedSet<Integer> days = schedules.stream().map(Schedule::day).collect(Collectors.toCollection(TreeSet::new));
        SortedSet<Integer> lessonsNumbers = schedules.stream().map(Schedule::lessonNumber).collect(Collectors.toCollection(TreeSet::new));

        return new ScheduleResponse(schedules, classesNumbers, days, lessonsNumbers);
    }
}
