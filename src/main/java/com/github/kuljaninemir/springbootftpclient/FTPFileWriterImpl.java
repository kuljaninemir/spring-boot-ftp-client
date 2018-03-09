package com.github.kuljaninemir.springbootftpclient;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class FTPFileWriterImpl implements FTPFileWriter {

    private static final Logger logger = LoggerFactory.getLogger(FTPFileWriterImpl.class);
    private FTPProperties FTPProperties;
    private FTPClient ftpClient;

    @Autowired
    public FTPFileWriterImpl(@Autowired FTPProperties FTPProperties) {
        this.FTPProperties = FTPProperties;
    }

    @PostConstruct
    public void init() {
        if (this.FTPProperties.isAutoStart()) {
            logger.debug("Autostarting connection to FTP server.");
            this.open();
        }
    }

    public boolean open() {
        close();
        logger.debug("Connecting and logging in to FTP server.");
        ftpClient = new FTPClient();
        boolean loggedIn = false;
        try {
            ftpClient.connect(FTPProperties.getServer(), FTPProperties.getPort());
            loggedIn = ftpClient.login(FTPProperties.getUsername(), FTPProperties.getPassword());
            if (FTPProperties.getKeepAliveTimeout() > 0)
                ftpClient.setControlKeepAliveTimeout(FTPProperties.getKeepAliveTimeout());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return loggedIn;
    }

    public void close() {
        if (ftpClient != null) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public boolean loadFile(String remotePath, OutputStream outputStream) {
        try {
            logger.debug("Trying to retrieve a file from remote path " + remotePath);
            return ftpClient.retrieveFile(remotePath, outputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean saveFile(InputStream inputStream, String destPath, boolean append) {
        try {
            logger.debug("Trying to store a file to destination path " + destPath);
            if(append)
                return ftpClient.appendFile(destPath, inputStream);
            else
                return ftpClient.storeFile(destPath, inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean saveFile(String sourcePath, String destPath, boolean append) {
        InputStream inputStream = null;
        try {
            inputStream = new ClassPathResource(sourcePath).getInputStream();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return this.saveFile(inputStream, destPath, append);
    }

    public boolean isConnected() {
        boolean connected = false;
        if (ftpClient != null) {
            try {
                connected = ftpClient.sendNoOp();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        logger.debug("Checking for connection to FTP server. Is connected: " + connected);
        return connected;
    }
}