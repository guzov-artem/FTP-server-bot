package telegramBot.FileSystemInterface;

import telegramBot.FileService.*;
import telegramBot.Utils.*;

import it.sauronsoftware.ftp4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class FileSystemInterface {
    private String currentDirectory;
    private String defaultDirectory;

    @Autowired
    private FileService fileSystem;

    public FileSystemInterface() {

        this.defaultDirectory = "/FTP";
        this.currentDirectory = defaultDirectory;
        fileSystem = new FileServiceImpl();
    }


    public String[] getListNames() throws FTPException, IOException, FTPDataTransferException, FTPListParseException,
            FTPIllegalReplyException, FTPAbortedException {
        return fileSystem.getListNames(currentDirectory);
    }

    public ArrayList<String> getListNames(String path) {
        return new ArrayList<String>();
    }

    public FTPFile[] getList() throws FTPException, IOException, FTPDataTransferException, FTPListParseException, FTPIllegalReplyException, FTPAbortedException {
        return fileSystem.getList(currentDirectory);
    }

    public FTPFile[] getList(String path) throws FTPException, IOException, FTPDataTransferException, FTPListParseException, FTPIllegalReplyException, FTPAbortedException {
        return fileSystem.getList(path);
    }

    public void goDefaultDirectory() {
        currentDirectory = defaultDirectory;
    }

    public void goParentDirectory() {
        currentDirectory = Utils.getParentDir(currentDirectory);
    }

    public void goDirectory(String path) {
        currentDirectory = path;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public File downloadFile(String path) throws FTPAbortedException, FTPDataTransferException, FTPException, FTPIllegalReplyException, IOException {
        return fileSystem.downloadFile(path);
    }
    public void uploadFile(File file) throws FTPAbortedException, FTPDataTransferException, FTPException, FTPIllegalReplyException, IOException {
        fileSystem.storeFile(file, currentDirectory);
    }
}
