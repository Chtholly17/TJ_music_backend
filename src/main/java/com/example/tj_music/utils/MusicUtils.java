package com.example.tj_music.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

@Component
@PropertySource(value = {"classpath:application.properties"})
public class MusicUtils {


    private static MusicUtils musicUtils = new MusicUtils();

    private MusicUtils() {
    }

    public static MusicUtils getInstance() {
        return musicUtils;
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
     * @param userStudentNumber
     * @param fileName
     * @return
     * @throws IOException
     */
    public EnumMap<MusicUtils.UploadResult,Object> upload(MultipartFile file, String userStudentNumber, String fileName) throws IOException {
        EnumMap<UploadResult,Object> en = new EnumMap<UploadResult,Object>(UploadResult.class);
        String saveName = savePath + userStudentNumber +"/music/"+ fileName + ".wav";
        en.put(UploadResult.PATH,saveName);
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
            String url = "http://120.46.60.40:"+ port + "/" + userStudentNumber +"/music/" + fileName + ".wav";
            en.put(UploadResult.URL,url);
            return en;
        }
        catch (IllegalStateException e) {
            throw e;
        }
    }

    /**
     * 获取mp3语音文件播放时长(秒) mp3
     * @param filePath
     * @return
     */
    public Float getMp3Duration(String filePath) {

        try {
            File mp3File = new File(filePath);
            MP3File f = (MP3File) AudioFileIO.read(mp3File);
            MP3AudioHeader audioHeader = (MP3AudioHeader) f.getAudioHeader();
            return Float.parseFloat(audioHeader.getTrackLength() + "");
        } catch (Exception e) {
            e.printStackTrace();
            return 0f;
        }
    }
}
