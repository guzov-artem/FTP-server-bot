package telegramBot;

import com.google.gson.Gson;
import it.sauronsoftware.ftp4j.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import telegramBot.FileService.FileService;
import telegramBot.FileService.FileServiceImpl;
import telegramBot.FileSystemInterface.FileSystemInterface;
import telegramBot.Utils.Utils;
import telegramBot.commands.*;
import org.json.JSONObject;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Собственно, бот
 */
@Component
public final class Bot extends TelegramLongPollingCommandBot {

    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private static FileSystemInterface fileSystemInterface;

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();


    public Bot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        this.fileSystemInterface = new FileSystemInterface();

        register(new StartCommand("start", "Старт", fileSystemInterface));
        register(new HelpCommand("help","Помощь", fileSystemInterface));
    }


    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            Map<String, String> callBackMap = new HashMap<>();
            String call_data = update.getCallbackQuery().getData();
            Message mes = update.getCallbackQuery().getMessage();
            callBackMap = Utils.parseJsonToArrayMaps().get(Integer.parseInt(call_data));
            long chat_id = update.getCallbackQuery().getMessage().getChatId();



            if (callBackMap.get("bT").equals("f")) {
                if (callBackMap.get("fT").equals("f")) {
                    SendDocument sendDocument = new SendDocument();
                    sendDocument.setChatId(String.valueOf(chat_id));
                    try {
                        sendDocument.setDocument(
                                new InputFile(fileSystemInterface.downloadFile(callBackMap.get("path"))));
                        execute(sendDocument);
                    } catch (FTPAbortedException e) {
                        e.printStackTrace();
                    } catch (FTPDataTransferException e) {
                        e.printStackTrace();
                    } catch (FTPException e) {
                        e.printStackTrace();
                    } catch (FTPIllegalReplyException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                else if (callBackMap.get("fT").equals("d")) {
                    try {
                        FTPFile[] files = fileSystemInterface.getList(callBackMap.get("path"));
                        fileSystemInterface.goDirectory(callBackMap.get("path"));
                        SendMessage message = setKeyBoard(String.valueOf(chat_id), files);

                        if (files.length == 0) {
                            message.setText("directory is empty" + "\ncurrent directory: " + callBackMap.get("path"));
                        }
                        else {

                            message.setText("current directory:" + callBackMap.get("path"));
                            message.enableMarkdown(true);
                        }


                        execute(message);
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
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (callBackMap.get("bT").equals("back")) {

                fileSystemInterface.goParentDirectory();
                FTPFile[] files = new FTPFile[0];
                try {
                    files = fileSystemInterface.getList(fileSystemInterface.getCurrentDirectory());
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
                SendMessage message = setKeyBoard(String.valueOf(chat_id), files);

                if (files.length == 0) {
                    message.setText("directory is empty" + "\ncurrent directory: "
                            + fileSystemInterface.getCurrentDirectory());
                }
                else {

                    message.setText("current directory:" + fileSystemInterface.getCurrentDirectory());
                    message.enableMarkdown(true);
                }
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }
        if (update.hasMessage()) {
            if (update.getMessage().hasDocument()) {
                Document document = update.getMessage().getDocument();
                try {
                    File file = downloadFromTelegram(document);
                    fileSystemInterface.uploadFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FTPAbortedException e) {
                    e.printStackTrace();
                } catch (FTPException e) {
                    e.printStackTrace();
                } catch (FTPDataTransferException e) {
                    e.printStackTrace();
                } catch (FTPIllegalReplyException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static SendMessage setKeyBoard(String chatId, FTPFile names[]) {
        ArrayList<Map<String, String>> mapArrayList = Utils.parseJsonToArrayMaps();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        ArrayList<InlineKeyboardButton> list1 =  new ArrayList<InlineKeyboardButton>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Назад");

        Map<String, String> map1 = new HashMap<>();
        map1.put("bT", "back");
        mapArrayList.add(map1);
        button1.setCallbackData(String.valueOf(mapArrayList.size() - 1));
        list1.add(button1);
        rowList.add(list1);


        for (int i = 0; i < names.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(names[i].getName());
            Map<String, String> map = new HashMap<>();
            map.put("bT", "f");
            map.put("fT", Utils.isFile(names[i].getName())?"f":"d");
            map.put("path", fileSystemInterface.getCurrentDirectory() + "/" + names[i].getName());


            mapArrayList.add(map);

            String callBackData = new Gson().toJson(map);
            button.setCallbackData(String.valueOf(mapArrayList.size() - 1));

            ArrayList<InlineKeyboardButton> list =  new ArrayList<InlineKeyboardButton>();
            list.add(button);
            rowList.add(list);
        }
        Utils.writeArrayMapToJson(mapArrayList);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public File downloadFromTelegram(Document document) throws IOException {
        URL url = new URL("https://api.telegram.org/bot"+getBotToken()+"/getFile?file_id="+document.getFileId());
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + file_path);
        File file = File.createTempFile(Utils.getFileNamePreff(document.getFileName()),
                "." + Utils.getFileNameSuff(document.getFileName()));
        FileOutputStream fos = new FileOutputStream(file);
        System.out.println("Start upload");
        ReadableByteChannel rbc = Channels.newChannel(downoload.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        return file;
    }


    /**
     * Получение настроек по id чата. Если ранее для этого чата в ходе сеанса работы бота настройки не были установлены,
     * используются настройки по умолчанию

    public static Settings getUserSettings(Long chatId) {
        Map<Long, Settings> userSettings = ru.taksebe.telegram.mentalCalculation.telegram.telegram.TelegramBot.getUserSettings();
        Settings settings = userSettings.get(chatId);
        if (settings == null) {
            return defaultSettings;
        }
        return settings;
    }*/

    /**
     * Отправка ответа
     * @param chatId id чата
     * @param userName имя пользователя
     * @param text текст ответа

    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            logger.error(String.format("Ошибка %s. Сообщение, не являющееся командой. Пользователь: %s", e.getMessage(),
                    userName));
            e.printStackTrace();
        }
    }*/
}