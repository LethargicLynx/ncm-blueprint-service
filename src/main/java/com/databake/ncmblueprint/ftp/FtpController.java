package com.databake.ncmblueprint.ftp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping(value = "${general.root_uri}/file")
public class FtpController {

    @Autowired
    private FtpService ftpService;

    @Value("${general.file.root}")
    private String fileRootPath;

//    @RequestMapping(value = "/image_ncm_blueprint/**", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
//    @ResponseBody byte[] getImageFtp(HttpServletRequest request) {
//        try {
//            String requestURL = request.getRequestURL().toString();
//            String pathFile = requestURL.split("/image_ncm_blueprint/")[1];
//            byte[] img = ftpService.downloadFtpFile(pathFile);
//            System.out.println(img.length);
//            return img;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return  null;
//        }
//    }

    @RequestMapping(value = "/image_ncm_blueprint/**", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(HttpServletRequest request) throws IOException {
        String requestURL = request.getRequestURL().toString();
        String pathFile = requestURL.split("/image_ncm_blueprint/")[1];

        byte[] img = ftpService.downloadImgFile(fileRootPath + pathFile);
        System.out.println(img.length);
        return img;
    }


}
