package by.vstu.schedule.models.DTO;

import java.util.Objects;

public record Schedule(Long id, String roomNumber, Integer day, Integer lessonNumber, String frame, String disciplineName,
                       String teacherFullName, String group) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Schedule person = (Schedule) obj;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
