package by.vstu.schedule.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record NewClassRoom219LoadInfo(
        @NotNull(message = "{schedule_monitor.new_class_room_219_load_info.create.errors.date_is_null}")
        LocalDate date,
        @NotNull(message = "{schedule_monitor.new_class_room_219_load_info.create.errors.time_is_null}")
        LocalTime time,
        @NotBlank(message = "{schedule_monitor.new_class_room_219_load_info.create.errors.type_is_blank}")
        @Size(max = 60, message = "{schedule_monitor.new_class_room_219_load_info.create.errors.type_size_is_invalid}")
        String type,
        @NotBlank(message = "{schedule_monitor.new_class_room_219_load_info.create.errors.responsible_is_blank}")
        @Size(max = 40, message = "{schedule_monitor.new_class_room_219_load_info.create.errors.responsible_size_is_invalid}")
        String responsible,
        @NotBlank(message = "{schedule_monitor.new_class_room_219_load_info.create.errors.description_is_blank}")
        @Size(max = 1000, message = "{schedule_monitor.new_class_room_219_load_info.create.errors.description_size_is_invalid}")
        String description) {
}
