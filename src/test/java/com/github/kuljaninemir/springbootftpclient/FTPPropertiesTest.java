package com.github.kuljaninemir.springbootftpclient;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class FTPPropertiesTest {

    @Test
    public void initShouldSetPortIfNotSet() {
        FTPProperties ftp = new FTPProperties();
        ftp.init();
        assertTrue(ftp.getPort() == 21);
    }

    @Test
    public void initShouldNotSetPortIfSet() {
        FTPProperties ftp = new FTPProperties();
        ftp.setPort(10);
        ftp.init();
        assertTrue(ftp.getPort() == 10);
    }
}
