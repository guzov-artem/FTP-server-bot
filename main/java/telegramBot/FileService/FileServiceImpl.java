package telegramBot.FileService;

import telegramBot.Utils.*;
import it.sauronsoftware.ftp4j.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;


public class FileServiceImpl implements FileService{

    private String ftpUrl = "192.168.1.71";
    private String login = "root";
    private String password = "orangepi";

    private FTPClient ftpClient;

    public FileServiceImpl() {
        this.ftpClient = new FTPClient();


        try {
            ftpClient.connect(ftpUrl);
            this.ftpClient.login(login, password);
            ftpClient.setType(FTPClient.TYPE_BINARY);
            ftpClient.changeDirectory("/FTP");

        }
        catch (Exception e) {
e.printStackTrace();
        }
    }


    @Override
    public String[] getListNames(String path) throws FTPException, IOException, FTPDataTransferException,
            FTPListParseException, FTPIllegalReplyException, FTPAbortedException {
        ftpClient.changeDirectory(path);
        return ftpClient.listNames();
    }

    @Override
    public FTPFile[] getList(String path) throws FTPException, IOException, FTPDataTransferException,
            FTPListParseException, FTPIllegalReplyException, FTPAbortedException {
        ftpClient.changeDirectory(path);
        return ftpClient.list();
    }

    @Override
    public String[] getListFiles(String path) throws FTPException, IOException, FTPIllegalReplyException,
            FTPAbortedException, FTPDataTransferException, FTPListParseException {
        ftpClient.changeDirectory(path);
        ArrayList<String> files = new ArrayList<>();
        for (FTPFile ftpFile: ftpClient.list()) {
            if (ftpFile.getType() == FTPFile.TYPE_FILE) {
                files.add(ftpFile.getName());
            }
        }
        return (String[]) files.toArray();
    }

    @Override
    public String[] getListDirectories(String path) throws FTPException, IOException, FTPIllegalReplyException,
            FTPAbortedException, FTPDataTransferException, FTPListParseException {
        ftpClient.changeDirectory(path);
        ArrayList<String> files = new ArrayList<>();
        for (FTPFile ftpFile: ftpClient.list()) {
            if (ftpFile.getType() == FTPFile.TYPE_DIRECTORY) {
                files.add(ftpFile.getName());
            }
        }
        return (String[]) files.toArray();
    }

    @Override
    public void deleteFile(String path) throws FTPException, IOException, FTPIllegalReplyException {
        ftpClient.changeDirectory(path);

    }

    @Override
    public void deleteFiles(String[] paths) {

    }

    @Override
    public void deleteDirectoryWithFiles(String path) {

    }

    @Override
    public File downloadFile(String path) throws IOException, FTPAbortedException, FTPDataTransferException,
            FTPException, FTPIllegalReplyException {
        ftpClient.changeDirectory(Utils.getDirectoryName(path));
        File file = File.createTempFile(Utils.getFileNamePreff(Utils.getFileName(path)), "." + Utils.getFileNameSuff(Utils.getFileName(path)));
        ftpClient.download(path, file);
        return file;
    }

    @Override
    public File[] downloadFiles(String[] paths) throws FTPAbortedException, FTPDataTransferException, FTPException,
            FTPIllegalReplyException, IOException {
        ArrayList<File> files = new ArrayList<>();
        for (String path: paths) {
            files.add(downloadFile(path));
        }
        return (File[]) files.toArray();
    }

    @Override
    public void storeFile(File file, String directory) throws IOException, FTPAbortedException, FTPDataTransferException,
            FTPException, FTPIllegalReplyException {
        ftpClient.changeDirectory(directory);
        ftpClient.upload(file);
    }

    @Override
    public void storeFiles(File[] files, String directory) throws FTPAbortedException, FTPDataTransferException,
            FTPException, FTPIllegalReplyException, IOException {
        for (File file: files) {
            storeFile(file, directory);
        }
    }
}
