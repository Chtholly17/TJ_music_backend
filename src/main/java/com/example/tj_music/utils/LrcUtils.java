package com.example.tj_music.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

@Component
@PropertySource(value = {"classpath:application.properties"})
public class LrcUtils {

    private static LrcUtils lrcUtils = new LrcUtils();

    public static LrcUtils getInstance() {
        return lrcUtils;
    }

    private LrcUtils() {
    }

    @Value("${savePath}")
    private String savePath;

    @Value("${server.port}")
    private String port;

    public enum UploadResult {
        URL,PATH
    }

    /**
     * upload 保存MultiPartFile
     * @param file
     * @param userName
     * @param fileName
     * @return
     * @throws IOException
     */
    public EnumMap<LrcUtils.UploadResult,Object> upload(MultipartFile file, String userName, String fileName) throws IOException {
        EnumMap<LrcUtils.UploadResult,Object> en = new EnumMap<LrcUtils.UploadResult,Object>(LrcUtils.UploadResult.class);
        String saveName = savePath + userName +"/lrc/"+ fileName + ".lrc";
        en.put(LrcUtils.UploadResult.PATH,saveName);
//        System.out.println(saveName);
//        String saveName = "/root/TJ_music/static/" + userName +"/"+ fileName + ".mp3";
        // display the saveName
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
            String url = "http://120.46.60.40:"+ port + "/" + userName +"/lrc/" + fileName + ".lrc";
            en.put(LrcUtils.UploadResult.URL,url);
            return en;
        }
        catch (IllegalStateException e) {
            throw e;
        }
    }
}
