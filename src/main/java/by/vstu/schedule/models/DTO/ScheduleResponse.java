package by.vstu.schedule.models.DTO;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public record ScheduleResponse(Set<Schedule> schedules, SortedSet<String> classesNumbers, SortedSet<Integer> days, SortedSet<Integer> lessonsNumbers) {
}
