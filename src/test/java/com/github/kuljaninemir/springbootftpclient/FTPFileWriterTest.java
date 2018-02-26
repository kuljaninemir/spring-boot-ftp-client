package com.github.kuljaninemir.springbootftpclient;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FTPFileWriterTest {

    private static final String FILE_1_CONTENTS = "abcdef 1234567890";
    private static FakeFtpServer fakeFtpServer;
    private FTPFileWriter ftpFileWriter;

    @BeforeClass
    public static void setupFakeFTPServer() {
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.addUserAccount(new UserAccount("user", "password", "c:\\data"));

        FileSystem fileSystem = new WindowsFakeFileSystem();
        fileSystem.add(new DirectoryEntry("c:\\data"));
        fileSystem.add(new FileEntry("c:\\data\\file1.txt", FILE_1_CONTENTS));
        fileSystem.add(new FileEntry("c:\\data\\run.exe"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.setServerControlPort(2101);

        fakeFtpServer.start();
    }

    @AfterClass
    public static void teardownFakeFTPServer() {
        fakeFtpServer.stop();
    }

    @Before
    public void setupFtpFileWriter() {
        ftpFileWriter = new FTPFileWriterImpl(getStandardFTPProperties());
    }

    @Test
    public void open() {
        assertTrue(ftpFileWriter.open());
    }

    @Test
    public void openWithKeepAliveTimout() {
        FTPProperties standardFTPProperties = getStandardFTPProperties();
        standardFTPProperties.setKeepAliveTimeout(5);
        ftpFileWriter = new FTPFileWriterImpl(standardFTPProperties);
        assertTrue(ftpFileWriter.open());
    }

    @Test
    public void openWrongCredentialsShouldReturnFalse() {
        FTPProperties standardFTPProperties = getStandardFTPProperties();
        standardFTPProperties.setPassword("wrongpassword");
        ftpFileWriter = new FTPFileWriterImpl(standardFTPProperties);
        assertFalse(ftpFileWriter.open());
    }

    @Test
    public void openWrongPortShouldReturnFalse() {
        FTPProperties standardFTPProperties = getStandardFTPProperties();
        standardFTPProperties.setPort(50);
        ftpFileWriter = new FTPFileWriterImpl(standardFTPProperties);
        assertFalse(ftpFileWriter.open());
    }

    @Test
    public void opensAutomaticallyWhenAutoStartIsTrue() {
        FTPProperties standardFTPProperties = getStandardFTPProperties();
        standardFTPProperties.setAutoStart(true);
        FTPFileWriterImpl ftpFileWriterMock = Mockito.spy(new FTPFileWriterImpl(standardFTPProperties));
        ftpFileWriterMock.init();
        verify(ftpFileWriterMock, times(1)).open();
    }

    @Test
    public void close() {
        ftpFileWriter.open();
        ftpFileWriter.close();
    }

    @Test
    public void closeWhenNotOpen() {
        ftpFileWriter.open();
        ftpFileWriter.close();
        ftpFileWriter.close();
    }

    @Test
    public void isConnectedShouldReturnTrueWhenConnected() {
        ftpFileWriter.open();
        assertTrue(ftpFileWriter.isConnected());
    }

    @Test
    public void isConnectedShouldReturnFalseWhenNotConnected() {
        assertFalse(ftpFileWriter.isConnected());
    }

    @Test
    public void isConnectedShouldReturnFalseWhenConnectionIsInvalid() {
        FTPProperties standardFTPProperties = getStandardFTPProperties();
        standardFTPProperties.setPort(50);
        ftpFileWriter = new FTPFileWriterImpl(standardFTPProperties);
        assertFalse(ftpFileWriter.open());
        assertFalse(ftpFileWriter.isConnected());
    }

    @Test
    public void retrieveFileContentsShouldMatch() {
        ftpFileWriter.open();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean success = ftpFileWriter.retrieveFile("file1.txt", outputStream);
        assertTrue(success);
        assertEquals(outputStream.toString(), FILE_1_CONTENTS);
    }

    @Test
    public void retrieveFileDoesNotExistShouldReturnFalse() {
        ftpFileWriter.open();
        boolean success = ftpFileWriter.retrieveFile("doesNotExist.txt", new ByteArrayOutputStream());
        assertFalse(success);
    }

    @Test
    public void retrieveFileWrongConnectionShouldReturnFalse() {
        FTPProperties standardFTPProperties = getStandardFTPProperties();
        standardFTPProperties.setPort(50);
        ftpFileWriter = new FTPFileWriterImpl(standardFTPProperties);
        assertFalse(ftpFileWriter.open());
        boolean success = ftpFileWriter.retrieveFile("doesNotExist.txt", new ByteArrayOutputStream());
        assertFalse(success);
    }

    @Test(expected = NullPointerException.class)
    public void retrieveFileNotConnectedShouldThrowNullpointer() {
        boolean success = ftpFileWriter.retrieveFile("doesNotExist.txt", new ByteArrayOutputStream());
        assertFalse(success);
    }

    public FTPProperties getStandardFTPProperties() {
        FTPProperties ftpProperties = new FTPProperties();
        ftpProperties.setAutoStart(false);
        ftpProperties.setServer("localhost");
        ftpProperties.setUsername("user");
        ftpProperties.setPassword("password");
        ftpProperties.setPort(2101);
        ftpProperties.setAutoStart(false);
        return ftpProperties;
    }
}
