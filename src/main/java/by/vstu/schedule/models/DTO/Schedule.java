package by.vstu.schedule.models.DTO;

public record Schedule(Long id, String roomNumber, Integer day, Integer lessonNumber, String frame, String disciplineName,
                       String teacherFullName, String group) {
}
