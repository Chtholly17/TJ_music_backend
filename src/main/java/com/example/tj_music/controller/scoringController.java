package com.example.tj_music.controller;

import com.example.tj_music.db.entity.Music;
import com.example.tj_music.service.originService;
import com.example.tj_music.db.mapper.OriginMapper;
import com.example.tj_music.utils.Result;
import com.example.tj_music.utils.MusicUtils;
import com.example.tj_music.service.scoringService;
import com.example.tj_music.db.entity.Origin;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.tj_music.utils.PythonUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController // @RestController = @Controller + @ResponseBody (return json)
public class scoringController {
    // please use the logger to print the log
    private static Logger log = Logger.getLogger("UserController.class");
    @Autowired // auto-inject
    private PythonUtils pythonUtils;
    // user service
    @Autowired // auto-inject
    private scoringService scoringService;
    @Autowired
    private originService originService;

    /**
     * get comments by scores
     * @param preciseScore
     * @param qualityScore
     * @param pitchScore
     * @return
     */
    @PostMapping("/getComments")
    public Result getComments(@RequestParam("preciseScore") String preciseScore, @RequestParam("qualityScore") String qualityScore, @RequestParam("pitchScore") String pitchScore) {
        return pythonUtils.getComments(preciseScore, qualityScore, pitchScore);
    }

    /**
     * get scores by origin id and user id
     * @param music
     * @return
     */
    @PostMapping("/getScores")
    public Result getScores(Music music) throws IOException {
        MultipartFile file = music.getFile();
        Integer originId = music.getOriginId();
        String userStudentNumber = music.getUserStudentNumber();
        System.out.println("originId: " + originId);
        System.out.println("userStudentNumber: " + userStudentNumber);
        System.out.println("file: " + file);
        Origin origin = originService.getOriginByOriginId(originId);
        String origin_name = origin.getOriginName();
        byte[] utf8Bytes = origin_name.getBytes(StandardCharsets.UTF_8);
        origin_name = new String(utf8Bytes, StandardCharsets.UTF_8);
        scoringService.saveTmpMp3(file, userStudentNumber);
        String url = "http://49.4.115.48:8888" + "/" + userStudentNumber +"/music/" + origin_name + ".wav";
        String work_voice_path = "/root/TJ_music/static/" + userStudentNumber + "/music/vocal.wav";
        String origin_bgm_path = "/root/TJ_music/static/admin/" + origin_name + "_bgmusic.wav";
        String outputPath = scoringService.mergeMp3(origin_bgm_path, work_voice_path, userStudentNumber,
                origin_name);
        System.out.println("origin_bgm_path: " + origin_bgm_path);
        System.out.println("work_voice_path: " + work_voice_path);
        System.out.println("outputPath: " + outputPath);
        Map<String, String> map = pythonUtils.getScore("/root/TJ_music/static/admin/" + origin_name + ".wav", outputPath);
        map.put("url", url);
        return Result.success(map);
    }

}