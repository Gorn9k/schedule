package by.vstu.schedule.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateClassRoom219LoadInfo(
        @NotNull
        Integer id,
        @NotNull(message = "{schedule_monitor.update_class_room_219_load_info.update.errors.date_is_null}")
        LocalDate localDate,
        @NotNull(message = "{schedule_monitor.update_class_room_219_load_info.update.errors.time_is_null}")
        LocalTime localTime,
        @NotBlank(message = "{schedule_monitor.update_class_room_219_load_info.update.errors.type_is_blank}")
        @Size(max = 60, message = "{schedule_monitor.update_class_room_219_load_info.update.errors.type_size_is_invalid}")
        String type,
        @NotBlank(message = "{schedule_monitor.update_class_room_219_load_info.update.errors.responsible_is_blank}")
        @Size(max = 40, message = "{schedule_monitor.update_class_room_219_load_info.update.errors.responsible_size_is_invalid}")
        String responsible,
        @NotBlank(message = "{schedule_monitor.update_class_room_219_load_info.update.errors.description_is_blank}")
        @Size(max = 1000, message = "{schedule_monitor.update_class_room_219_load_info.update.errors.description_size_is_invalid}")
        String description) {
}
