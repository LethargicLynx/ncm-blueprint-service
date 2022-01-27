package com.databake.ncmblueprint.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class FtpService {

    @Value("${ftp.server.host}")
    private String ftpHost;

    @Value("${ftp.server.port}")
    private Integer ftpPort;

    @Value("${ftp.server.username}")
    private String ftpUsername;

    @Value("${ftp.server.password}")
    private String ftpPassword;

    public byte[] downloadFtpFile(String filePath) throws IOException {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(ftpHost, ftpPort);
            if(!ftpClient.login(ftpUsername, ftpPassword)) {
                throw new Exception();
            }

            ftpClient.enterLocalPassiveMode();
            try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ftpClient.retrieveFile(filePath, outputStream);
                return outputStream.toByteArray();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }

        return null;
    }

    public byte[] downloadImgFile(String filePath) throws IOException {
        File imgFile = new File(filePath);
        byte[] imgByte = Files.readAllBytes(imgFile.toPath());

        return imgByte;
    }


}
