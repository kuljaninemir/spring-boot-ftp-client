package com.github.kuljaninemir.springbootstarterftpclient;

import java.io.InputStream;
import java.io.OutputStream;

public interface FTPFileWriter {
    boolean open();

    void close();

    boolean retrieveFile(String remotePath, OutputStream outputStream);

    boolean storeFile(InputStream inputStream, String destPath);

    boolean storeFile(String sourcePath, String destPath);

    boolean isConnected();
}
