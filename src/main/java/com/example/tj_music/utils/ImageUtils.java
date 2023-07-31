package com.example.tj_music.utils;


import com.example.tj_music.db.entity.Image;
import com.example.tj_music.service.informationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@PropertySource(value = {"classpath:application.properties"})
public class ImageUtils {

    private static ImageUtils imageUtils = new ImageUtils();

    private ImageUtils() {
    }

    public static ImageUtils getInstance() {
        return imageUtils;
    }

    @Value("${savePath}")
    private String savePath="/root/TJ_music/static/";

    @Value("${server.port}")
    private String port;

    /**
     * @description: 通过url获取输入流
     * @param file
     * @param userName
     * @param fileName
     * @return String urlPath
     */
    public String upload(MultipartFile file, String userName, String fileName) throws IOException {
        String saveName = savePath + userName + "/images/" + fileName + ".jpg";
        File dest = new File(saveName);
//        System.out.println(saveName);
        if (!dest.exists()) {
            //先得到文件的上级目录，并创建上级目录，在创建文件
            dest.getParentFile().mkdirs();
            try {
                //创建文件
                dest.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            file.transferTo(dest);
            String url = "http://120.46.60.40:" + port + "/" + userName + "/images/" + fileName + ".jpg";
            return url;
        } catch (IllegalStateException e) {
            throw e;
        }
    }

}
