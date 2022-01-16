package telegramBot.Utils;

import com.google.gson.Gson;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    /**
     * Формирование имени пользователя
     * @param msg сообщение
     */
    public static String getUserName(Message msg) {
        return getUserName(msg.getFrom());
    }

    /**
     * Формирование имени пользователя. Если заполнен никнейм, используем его. Если нет - используем фамилию и имя
     * @param user пользователь
     */
    public static String getUserName(User user) {
        return (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    public static String getFileName(String path) {
        StringBuilder fileName = new StringBuilder();
        for (int i = path.length() - 1; i > 0 ; i--) {
          if (path.charAt(i) == '/') {
              break;
          }
          fileName.append(path.charAt(i));
        }
        return fileName.reverse().toString();
    }

    public static String getFileNameSuff(String fileName) {
        StringBuilder suff = new StringBuilder();
        for (int i = fileName.length() - 1; i > 0 ; i--) {
            if (fileName.charAt(i) == '.') {
                break;
            }
            suff.append(fileName.charAt(i));
        }
        return suff.reverse().toString();
    }

    public static String getFileNamePreff(String fileName) {
        StringBuilder preff = new StringBuilder(fileName);
        for (int i = fileName.length() - 1; i > 0 ; i--) {
            if (fileName.charAt(i) == '.') {
                preff.deleteCharAt(i);
                break;
            }
            preff.deleteCharAt(i);
        }
        return preff.toString();
    }

    public static String getDirectoryName(String path) {
        StringBuilder fileName = new StringBuilder(path);
        for (int i = path.length() - 1; i > 0 ; i--) {
            if (path.charAt(i) == '/') {
                fileName.deleteCharAt(i);
                break;
            }
            fileName.deleteCharAt(i);
        }
        return fileName.toString();
    }

    public static String getParentDir(String directory) {
        StringBuilder parentDir = new StringBuilder(directory);
        for (int i = directory.length() - 1; i > 0 ; i--) {
            if (directory.charAt(i) == '/') {
                parentDir.deleteCharAt(i);
                break;
            }
            parentDir.deleteCharAt(i);
        }
        return parentDir.toString();
    }

    public static   boolean isDirectrory(String name) {
        for (int i = name.length() - 1; i > 0 ; i--) {
            if (name.charAt(i) == '/') {
                return true;
            }
            if (name.charAt(i) == '.') {
                return false;
            }
        }
        return false;
    }

    public static ArrayList<Map<String, String>> parseJsonToArrayMaps() {
        File file = new File("buttomsInfo.json");
        ArrayList<Map<String, String>> map = new ArrayList<>();
        try (FileReader fileReader = new FileReader(file)) {
            map = new Gson().fromJson(fileReader, map.getClass());
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void writeArrayMapToJson(ArrayList<Map<String, String>> mapArrayListmap) {
        File file = new File("buttomsInfo.json");
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(new Gson().toJson(mapArrayListmap, mapArrayListmap.getClass()));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static   boolean isFile(String name) {
        return name.matches("^.*\\.(.*)$");
    }
}
