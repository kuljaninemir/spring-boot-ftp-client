[![Build Status](https://travis-ci.org/kuljaninemir/spring-boot-ftp-client.svg?branch=master)](https://travis-ci.org/kuljaninemir/spring-boot-ftp-client) [![Coverage Status](https://codecov.io/gh/kuljaninemir/spring-boot-ftp-client/branch/master/graph/badge.svg)](https://codecov.io/gh/kuljaninemir/spring-boot-ftp-client) [![HitCount](http://hits.dwyl.com/kuljaninemir/spring-boot-ftp-client.svg)](http://hits.dwyl.com/kuljaninemir/spring-boot-ftp-client) [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues)

# Spring boot ftp client

Spring boot project for using Apache Commons FTPClient through a simplified interface.

## Usage

Add the following dependency:

```
<dependency>
  <groupId>com.github.kuljaninemir</groupId>
  <artifactId>spring-boot-ftp-client</artifactId>
  <version>1.0.07</version>
</dependency>
```

Add the following to your application.yml:

```
ftp:
  username: username
  password: password
  server: server
```

Inject a FTPFileWriter like so:

```
@Inject
FTPFileWriter ftpFileWriter;
```

Connect and start using the FTPFileWriter

```
ftpFileWriter.open();
if(ftpFileWriter.isConnected()){
    ftpFileWriter.retrieveFile(path, outputstream);
    ftpFileWriter.storeFile(inputstream, remotepath);
    ftpFileWriter.storeFile(sourcepath, destpath);
}
ftpFileWriter.close();
```


## Configuration

By default port 21 is used and keepAliveTimeout is not set. You can change both these things by supplying these additional properties:

```
ftp:
  port: 21
  keepAliveTimout: 10
```

If you want the FTPFileWriter to connect as soon as it's injected, you can set this:

```
ftp:
  autoStart: true
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

I use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/kuljaninemir/spring-boot-ftp-client/tags). 

## Authors

* **Emir Kuljanin** - *Initial work* - [kuljaninemir](https://github.com/kuljaninemir)

See also the list of [contributors](https://github.com/kuljaninemir/spring-boot-ftp-client/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details