package telegramBot.FileService;

import it.sauronsoftware.ftp4j.*;

import java.io.File;
import java.io.IOException;

public interface FileService {
    String[] getListNames(String path) throws FTPException, IOException, FTPDataTransferException, FTPListParseException, FTPIllegalReplyException, FTPAbortedException;

    FTPFile[] getList(String path) throws FTPException, IOException, FTPDataTransferException,
            FTPListParseException, FTPIllegalReplyException, FTPAbortedException;

    String[] getListFiles(String path) throws FTPException, IOException, FTPIllegalReplyException, FTPAbortedException, FTPDataTransferException, FTPListParseException;

    String[] getListDirectories(String path) throws FTPException, IOException, FTPIllegalReplyException, FTPAbortedException, FTPDataTransferException, FTPListParseException;

    void deleteFile(String path) throws FTPException, IOException, FTPIllegalReplyException;

    void deleteFiles(String[] paths);

    void deleteDirectoryWithFiles(String path);

    File downloadFile(String path) throws IOException, FTPAbortedException, FTPDataTransferException, FTPException, FTPIllegalReplyException;

    File[] downloadFiles(String[] paths) throws FTPAbortedException, FTPDataTransferException, FTPException, FTPIllegalReplyException, IOException;

    void storeFile(File file, String path) throws IOException, FTPAbortedException, FTPDataTransferException, FTPException, FTPIllegalReplyException;

    void storeFiles(File[] files, String path) throws FTPAbortedException, FTPDataTransferException, FTPException, FTPIllegalReplyException, IOException;

}
