package com.github.kuljaninemir.springbootftpclient;

import java.io.InputStream;
import java.io.OutputStream;

public interface FTPFileWriter {

    /**
     * Connects to a server and tries to log in the user.
     *
     * @return boolean True if successful, False otherwise.
     */
    boolean open();

    /**
     * Logouts the current user and disconnects from the server.
     */
    void close();

    /**
     * Retrieve a file from the ftp server.
     *
     * @param remotePath   Remote path for the file to retrieve.
     * @param outputStream Stream the file is read into.
     * @return boolean True if successful, False otherwise.
     */

    boolean loadFile(String remotePath, OutputStream outputStream);

    /**
     * Store a file on the ftp server.
     *
     * @param inputStream Stream the new file is read from.
     * @param destPath    Remote path the file should be placed at.
     * @param append      Append to an existing file or write as a new file.
     * @return boolean True if successful, False otherwise.
     */

    boolean saveFile(InputStream inputStream, String destPath, boolean append);

    /**
     * Store a file on the ftp server.
     *
     * @param sourcePath Local path the file is read from.
     * @param destPath   Remote path the file should be placed at.
     * @param append      Append to an existing file or write as a new file.
     * @return boolean True if successful, False otherwise.
     */

    boolean saveFile(String sourcePath, String destPath, boolean append);

    /**
     * Does a NOOP to see if the connection is valid.
     *
     * @return boolean True if connected, False otherwise.
     */

    boolean isConnected();
}
