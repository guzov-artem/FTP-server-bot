package telegramBot.commands;

import telegramBot.FileService.FileServiceImpl;
import telegramBot.FileSystemInterface.FileSystemInterface;
import telegramBot.Utils.Utils;
import it.sauronsoftware.ftp4j.*;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;


import java.io.IOException;

public class HelpCommand extends ServiceCommand{
    public HelpCommand(String identifier, String description, FileSystemInterface fileSystemInterface) {
        super(identifier, description, fileSystemInterface);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        try {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                    "Привет) Я бот FTP сервера!" +
                        "\nЯ помогу сохранить твои файлы, для этого выбери нужную папку и отправь файлы, как документ" +
                            "\n Чтобы скачать нужный файл, просто тапни на него" +
                            "\nУдачи)))");
        } catch (FTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPDataTransferException e) {
            e.printStackTrace();
        } catch (FTPListParseException e) {
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
        } catch (FTPAbortedException e) {
            e.printStackTrace();
        }
    }
}
