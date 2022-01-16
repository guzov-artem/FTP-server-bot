package telegramBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class SpringTelegramBotApplication {
    public static void main(String[] args) {

        start();
    }
    static void start() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot("FTP-Server", "2126198780:AAHu0tIOW8UkDa2504vJ0xstGqNPPMKcbwQ"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
