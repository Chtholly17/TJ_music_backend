package com.example.tj_music.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@PropertySource(value = {"classpath:application.properties"})
public class MusicUtils {

    @Value("${savePath}")
    private String savePath;

    @Value("${server.port}")
    private String port;

    /**
     * @description: 通过url获取输入流
     * @param file
     * @return String urlPath
     */
    public String upload(MultipartFile file, String userName, String fileName) throws IOException {
        String saveName = savePath + userName +"/"+ fileName + ".mp3";
        File dest = new File(saveName);
        System.out.println(saveName);
        if(!dest.exists()){
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
            String url = "http://49.4.115.48:"+ port + "/" + userName +"/musics/" + fileName + ".mp3";
            return url;
        }
        catch (IllegalStateException e) {
            throw e;
        }
    }
}