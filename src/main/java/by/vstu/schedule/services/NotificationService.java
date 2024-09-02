package by.vstu.schedule.services;

import by.vstu.schedule.models.entities.ClassRoom219LoadInfo;
import by.vstu.schedule.repositories.ClassRoom219LoadInfoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class NotificationService {

    private final ClassRoom219LoadInfoRepository classRoom219LoadInfoRepository;
    private final TelegramBot telegramBot;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<Integer, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final Long chatId;

    public NotificationService(ClassRoom219LoadInfoRepository classRoom219LoadInfoRepository,
                               TelegramBot telegramBot,
                               @Value("${chat.id}") Long chatId) {
        this.classRoom219LoadInfoRepository = classRoom219LoadInfoRepository;
        this.telegramBot = telegramBot;

        this.chatId = chatId;
    }

    @PostConstruct
    public void init() {
        loadAndScheduleEvents();
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void scheduleDailyTask() {
        loadAndScheduleEvents();
    }

    private void loadAndScheduleEvents() {
        LocalDate today = LocalDate.now();
        List<ClassRoom219LoadInfo> todaysLoads = classRoom219LoadInfoRepository.findClassRoom219LoadsInfoByLocalDate(today);

        for (ClassRoom219LoadInfo load : todaysLoads) {
            scheduleTask(load);
        }
    }

    public void scheduleTask(ClassRoom219LoadInfo load) {
        LocalDateTime eventTime = load.getLocalDate().atTime(load.getLocalTime());
        LocalDateTime notifyTime = eventTime.minusMinutes(30);
        long delay = LocalDateTime.now().until(notifyTime, TimeUnit.SECONDS.toChronoUnit());

        if (delay > 0) {
            ScheduledFuture<?> scheduledFuture = scheduler.schedule(() -> sendNotification(load), delay, TimeUnit.SECONDS);
            scheduledTasks.put(load.getId(), scheduledFuture);
        }
    }

    public void updateTask(ClassRoom219LoadInfo load) {
        cancelTask(load.getId());
        addTaskIfToday(load);
    }

    public void deleteTask(Integer loadId) {
        cancelTask(loadId);
    }

    private void cancelTask(Integer loadId) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(loadId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledTasks.remove(loadId);
        }
    }

    public void addTaskIfToday(ClassRoom219LoadInfo load) {
        if (load.getLocalDate().equals(LocalDate.now())) {
            scheduleTask(load);
        }
    }

    private void sendNotification(ClassRoom219LoadInfo load) {
        String message = String.format("""
                Через 30 минут должно начаться мероприятие в 219 аудитории.
                Подробная информация о нагрузке:
                    Дата: %s
                    Время: %s
                    Тип: %s
                    Ответственный: %s
                    Комментарий: %s""", load.getLocalDate(), load.getLocalTime(), load.getType(), load.getResponsible(), load.getDescription());
        telegramBot.sendMessage(this.chatId, message);
    }
}

