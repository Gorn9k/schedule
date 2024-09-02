package by.vstu.schedule.services;

import by.vstu.schedule.models.DTO.DayOfWeekLessonNumber;
import by.vstu.schedule.models.DTO.Schedule;
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
    public Map<String, Map<String, List<Schedule>>> getAllScheduleByFrameAndStartDateAndEndDate(String frame,
                                                                                                               LocalDate startDate,
                                                                                                               LocalDate endDate) {
        String[] classes = frame_classroomNumbers_Map.get(switch (frame) {
            case "FIRST" -> "firstFrame";
            case "FOURTH" -> "fourthFrame";
            default -> throw new IllegalStateException("Unexpected value: " + frame);
        });
        List<Schedule> schedules = this.restClient
                .get()
                .uri(String.format("/api/rooms/byRoomAndDate?weekType=%s,ALWAYS&startDate=%s&endDate=%s&roomNumbers=%s&frame=%s",
                        checkWeekType(startDate),
                        startDate,
                        endDate,
                        Arrays.toString(classes).replaceAll("[\\[\\] ]", ""),
                        frame))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Map<String, List<Schedule>>> responseMap = new LinkedHashMap<>();
        if (schedules != null && !schedules.isEmpty()) {
            Set<DayOfWeekLessonNumber> dlrSet = schedules.stream().map(schedule -> new DayOfWeekLessonNumber(schedule.day(),
                            schedule.lessonNumber()))
                    .sorted(Comparator.comparing(DayOfWeekLessonNumber::day)
                    .thenComparing(DayOfWeekLessonNumber::lessonNumber)).collect(Collectors.toCollection(LinkedHashSet::new));
            for (DayOfWeekLessonNumber dlr : dlrSet) {
                Map<String, List<Schedule>> map = new HashMap<>();
                for (String classroomNumber : classes) {
                    map.put(classroomNumber, schedules.stream().filter(schedule -> schedule.day().equals(dlr.day())
                                    && schedule.lessonNumber().equals(dlr.lessonNumber()) && schedule.roomNumber().equals(classroomNumber))
                            .collect(Collectors.toList()));
                }
                try {
                    responseMap.put(objectMapper.writeValueAsString(dlr), map);
                } catch (Exception ignored) {
                    return responseMap;
                }
            }
        }

        return responseMap;
    }
}
