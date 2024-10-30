package by.vstu.schedule.controllers;

import by.vstu.schedule.models.DTO.NewClassRoom219LoadInfo;
import by.vstu.schedule.models.DTO.Schedule219;
import by.vstu.schedule.models.entities.ClassRoom219LoadInfo;
import by.vstu.schedule.services.ClassRoom219LoadInfoService;
import by.vstu.schedule.services.DateService;
import by.vstu.schedule.services.NotificationService;
import by.vstu.schedule.services.TelegramBot;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static by.vstu.schedule.services.DateService.FRONT_TIME_FORMAT;

@RestController
@RequestMapping("/class-room/number/219/loads-info")
@RequiredArgsConstructor
public class ClassRoom219LoadsInfoController {

    private final ClassRoom219LoadInfoService classRoom219LoadInfoService;

    private final TelegramBot telegramBot;

    private final NotificationService notificationService;

    private final MessageSource messageSource;

    @GetMapping("list")
    public ResponseEntity<List<Schedule219>> getClassRooms219LoadInfoPage(@RequestParam String startDate,
                                                                          @RequestParam String endDate) {
        return ResponseEntity.ok(classRoom219LoadInfoService.getClassRoom219LoadsInfoByStartDateAndEndDate(LocalDate.parse(startDate),
                LocalDate.parse(endDate)).stream().map(classRoom219LoadInfo ->
                new Schedule219(classRoom219LoadInfo.getId(),
                        classRoom219LoadInfo.getLocalDate(),
                        classRoom219LoadInfo.getLocalTime().format(FRONT_TIME_FORMAT),
                        classRoom219LoadInfo.getType(),
                        classRoom219LoadInfo.getResponsible(),
                        classRoom219LoadInfo.getDescription())).collect(Collectors.toList()));
    }

    @PostMapping("create")
    public ResponseEntity<?> createClassRoom219LoadInfoAndRedirectToClassRooms219LoadInfoPage(@RequestBody @Valid NewClassRoom219LoadInfo payload,
                                                                                              BindingResult bindingResult,
                                                                                              @Value("${chat.id}") String chatId,
                                                                                              Locale locale,
                                                                                              HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> field_error_map = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError ->
                    field_error_map.put(fieldError.getField(), messageSource.getMessage(
                            fieldError.getDefaultMessage(),
                            new Object[0],
                            fieldError.getDefaultMessage(),
                            locale)));
            return ResponseEntity.badRequest().body(field_error_map);
        }
        ClassRoom219LoadInfo createdClassRoom219LoadInfo = classRoom219LoadInfoService.saveClassRoom219LoadInfo(new ClassRoom219LoadInfo(null,
                payload.date(), payload.time(), payload.type(), payload.responsible(), payload.description()));
//        telegramBot.sendMessage(Long.valueOf(chatId), String.format("""
//                        Только что была создана новая нагрузка на 219 аудиторию.
//                        Новая нагрузка:
//                            Дата: %s
//                            Время: %s
//                            Тип: %s
//                            Ответственный: %s
//                            Комментарий: %s""",
//                createdClassRoom219LoadInfo.getLocalDate(),
//                createdClassRoom219LoadInfo.getLocalTime(),
//                createdClassRoom219LoadInfo.getType(),
//                createdClassRoom219LoadInfo.getResponsible(),
//                createdClassRoom219LoadInfo.getDescription()));
        notificationService.addTaskIfToday(createdClassRoom219LoadInfo);
        return ResponseEntity.created(URI.create("/class-room/number/219/load-info/"
                .concat(String.valueOf(createdClassRoom219LoadInfo.getId())))).build();
    }
}
