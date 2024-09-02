package by.vstu.schedule.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botToken;

//    private final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);


    public TelegramBot(@Value("${bot.token}") String botToken) throws TelegramApiException {
        super(botToken);
        this.botToken = botToken;
    }

//    @PostConstruct
//    private void init() throws TelegramApiException {
//        telegramBotsApi.registerBot(this); // Регистрируем бота
//    }

    @Override
    public String getBotUsername() {
        return "ClassRoom219LoadInfoBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        sendMessage(chatId, answer);
    }

    public void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException telegramApiException) {
            System.out.println(telegramApiException.getMessage());
        }
    }
}
