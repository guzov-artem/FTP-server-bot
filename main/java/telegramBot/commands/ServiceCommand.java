package telegramBot.commands;

import it.sauronsoftware.ftp4j.*;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegramBot.FileSystemInterface.FileSystemInterface;

import java.io.IOException;

/**
 * Суперкласс для сервисных команд
 */
abstract class ServiceCommand extends BotCommand {

    ServiceCommand(String identifier, String description, FileSystemInterface fileSystemInterface) {
        super(identifier, description);
        this.fileSystemInterface = fileSystemInterface;
    }

    /**
     * Отправка ответа пользователю
     */
    void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text) throws FTPException, IOException, FTPDataTransferException, FTPListParseException, FTPIllegalReplyException, FTPAbortedException {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    protected FileSystemInterface fileSystemInterface;
}