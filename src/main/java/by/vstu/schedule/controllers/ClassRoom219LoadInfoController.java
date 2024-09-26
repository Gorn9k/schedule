package by.vstu.schedule.controllers;

import by.vstu.schedule.models.DTO.Schedule219;
import by.vstu.schedule.models.DTO.UpdateClassRoom219LoadInfo;
import by.vstu.schedule.models.entities.ClassRoom219LoadInfo;
import by.vstu.schedule.services.ClassRoom219LoadInfoService;
import by.vstu.schedule.services.DateService;
import by.vstu.schedule.services.NotificationService;
import by.vstu.schedule.services.TelegramBot;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static by.vstu.schedule.services.DateService.FRONT_TIME_FORMAT;

@RestController
@RequestMapping("/class-room/number/219/load-info/{classRoom219LoadInfoId:\\d+}")
@RequiredArgsConstructor
public class ClassRoom219LoadInfoController {

    private final ClassRoom219LoadInfoService classRoom219LoadInfoService;

    private final TelegramBot telegramBot;

    private final NotificationService notificationService;

    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<Schedule219> getClassRoom219LoadInfo(@PathVariable("classRoom219LoadInfoId") Integer classRoom219LoadInfoId) {
        ClassRoom219LoadInfo classRoom219LoadInfo = classRoom219LoadInfoService.getClassRoom219LoadInfoById(classRoom219LoadInfoId);
        return ResponseEntity.ok(new Schedule219(classRoom219LoadInfo.getId(),
                classRoom219LoadInfo.getLocalDate(),
                classRoom219LoadInfo.getLocalTime().format(FRONT_TIME_FORMAT),
                classRoom219LoadInfo.getType(),
                classRoom219LoadInfo.getResponsible(),
                classRoom219LoadInfo.getDescription()));
    }

    @PutMapping("edit")
    public ResponseEntity<?> updateClassRoom219LoadInfoAndRedirectToClassRooms219LoadInfoPage(@Valid @RequestBody UpdateClassRoom219LoadInfo payload,
                                                                                              BindingResult bindingResult,
                                                                                              @Value("${chat.id}") String chatId,
                                                                                              Locale locale) {
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
        ClassRoom219LoadInfo classRoom219LoadInfo = classRoom219LoadInfoService.getClassRoom219LoadInfoById(payload.id());
        String message = String.format("""
                        Только что была обновлена нагрузка на 219 аудиторию.
                        Старая нагрузка:
                            Дата: %s
                            Время: %s
                            Тип: %s
                            Ответственный: %s
                            Комментарий: %s""",
                classRoom219LoadInfo.getLocalDate(),
                classRoom219LoadInfo.getLocalTime(),
                classRoom219LoadInfo.getType(),
                classRoom219LoadInfo.getResponsible(),
                classRoom219LoadInfo.getDescription());
        ClassRoom219LoadInfo updatedClassRoom219LoadInfo = classRoom219LoadInfoService.updateClassRoom219LoadInfo(new ClassRoom219LoadInfo(
                classRoom219LoadInfo.getId(), payload.date(), payload.time(), payload.type(), payload.responsible(), payload.description()));
//        telegramBot.sendMessage(Long.valueOf(chatId), message.concat(String.format("""
//                        \nНовая нагрузка:
//                            Дата: %s
//                            Время: %s
//                            Тип: %s
//                            Ответственный: %s
//                            Комментарий: %s""",
//                updatedClassRoom219LoadInfo.getLocalDate(),
//                updatedClassRoom219LoadInfo.getLocalTime(),
//                updatedClassRoom219LoadInfo.getType(),
//                updatedClassRoom219LoadInfo.getResponsible(),
//                updatedClassRoom219LoadInfo.getDescription())));
        notificationService.updateTask(updatedClassRoom219LoadInfo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteClassRoom219LoadInfoAndRedirectToClassRooms219LoadInfoPage(@PathVariable("classRoom219LoadInfoId") Integer id, @Value("${chat.id}") String chatId) {
        ClassRoom219LoadInfo classRoom219LoadInfo = classRoom219LoadInfoService.getClassRoom219LoadInfoById(id);
        classRoom219LoadInfoService.deleteClassRoom219LoadInfo(classRoom219LoadInfo);
//        telegramBot.sendMessage(Long.valueOf(chatId), String.format("""
//                        Только что была удалена нагрузка на 219 аудиторию.
//                        Удаленная нагрузка:
//                            Дата: %s
//                            Время: %s
//                            Тип: %s
//                            Ответственный: %s
//                            Комментарий: %s""",
//                classRoom219LoadInfo.getLocalDate(),
//                classRoom219LoadInfo.getLocalTime(),
//                classRoom219LoadInfo.getType(),
//                classRoom219LoadInfo.getResponsible(),
//                classRoom219LoadInfo.getDescription()));
        notificationService.deleteTask(classRoom219LoadInfo.getId());
        return ResponseEntity.noContent().build();
    }
}
