package com.example.tj_music.controller;

import com.example.tj_music.db.entity.Music;
import com.example.tj_music.db.mapper.UserMapper;
import com.example.tj_music.object.CommentInfo;
import com.example.tj_music.service.AccountService;
import com.example.tj_music.service.OriginService;
import com.example.tj_music.object.Result;
import com.example.tj_music.service.ScoringService;
import com.example.tj_music.service.WorkService;
import com.example.tj_music.db.entity.Origin;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.tj_music.utils.PythonUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/Scoring")
public class ScoringController {
    // please use the logger to print the log
    private static Logger log = Logger.getLogger("UserController.class");
    @Autowired // auto-inject
    private ScoringService scoringService;
    @Autowired
    private OriginService originService;

    /**
     * get comments by scores
     * @param commentInfo
     * @return
     */
    @PostMapping("/getComments")
    public Result getComments(@RequestBody CommentInfo commentInfo) {
        String preciseScore = commentInfo.getPreciseScore();
        String qualityScore = commentInfo.getQualityScore();
        String pitchScore = commentInfo.getPitchScore();
        return PythonUtils.getInstance().getComments(preciseScore, qualityScore, pitchScore);
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

        String[] parts = origin.getOriginVoiceFilename().split("/");
        String origin_name = parts[parts.length - 1];

        parts = origin.getoriginBgMusicFilename().split("/");
        String origin_bgm_name = parts[parts.length - 1];

        scoringService.saveTmpMp3(file, userStudentNumber);
        String url = "http://120.46.60.40:8888" + "/" + userStudentNumber +"/music/" + origin_name;


        String work_voice_path = "/root/TJ_music/static/" + userStudentNumber + "/music/vocal.wav";
        String origin_bgm_path = "/root/TJ_music/static/admin/music/" + origin_bgm_name;
        String outputPath = scoringService.mergeMp3(origin_bgm_path, work_voice_path, userStudentNumber,
                origin_name);
        System.out.println("origin_bgm_path: " + origin_bgm_path);
        System.out.println("work_voice_path: " + work_voice_path);
        System.out.println("outputPath: " + outputPath);
        Map<String, Object> map = new HashMap<>();
        map.put("scores", PythonUtils.getInstance().getScore("/root/TJ_music/static/admin/music/" + origin_name, outputPath));
        map.put("url", url);
        return Result.success(map);
    }

}