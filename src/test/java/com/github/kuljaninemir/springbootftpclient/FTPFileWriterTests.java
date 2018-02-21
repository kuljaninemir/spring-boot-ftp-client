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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FTPFileWriterTests {

	private static FakeFtpServer fakeFtpServer;
	private FTPFileWriter ftpFileWriter;

	@BeforeClass
	public static void setupFakeFTPServer() {
		fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.addUserAccount(new UserAccount("user", "password", "c:\\data"));

		FileSystem fileSystem = new WindowsFakeFileSystem();
		fileSystem.add(new DirectoryEntry("c:\\data"));
		fileSystem.add(new FileEntry("c:\\data\\file1.txt", "abcdef 1234567890"));
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
	public void openWrongCredentialsShouldReturnFalse() {
		FTPProperties standardFTPProperties = getStandardFTPProperties();
		standardFTPProperties.setPassword("wrongpassword");
		ftpFileWriter = new FTPFileWriterImpl(standardFTPProperties);
		assertFalse(ftpFileWriter.open());
	}

	public FTPProperties getStandardFTPProperties() {
		FTPProperties ftpProperties = new FTPProperties();
		ftpProperties.setAutoStart(false);
		ftpProperties.setServer("localhost");
		ftpProperties.setUsername("user");
		ftpProperties.setPassword("password");
		ftpProperties.setPort(2101);
		return ftpProperties;
	}
}
