package com.example.tj_music.service;

import com.example.tj_music.db.entity.Origin;
import com.example.tj_music.db.entity.OriginFrontEnd;
import com.example.tj_music.db.entity.Work;
import com.example.tj_music.db.mapper.OriginMapper;

import com.example.tj_music.db.mapper.WorkCommentMapper;
import com.example.tj_music.db.mapper.WorkMapper;
import com.example.tj_music.utils.PythonUtils;
import com.example.tj_music.utils.MusicUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.tj_music.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;

import static java.lang.Math.round;

@Service
public class scoringService {
    // @Autowired
    // private MusicUtils musicUtils;
    @Autowired
    private PythonUtils pythonUtils;

    /**
     * save temporary mp3 for scoring
     * @param file
     * @param userStudentNumber
     * @return
     */
    public EnumMap<MusicUtils.UploadResult,Object> saveTmpMp3(MultipartFile file, String userStudentNumber) throws IOException {
        return MusicUtils.getInstance().upload(file, userStudentNumber, "vocal");
    }

    /**
     * merge mp3 from bgm and vocal
     * @param bgmPath
     * @param vocalPath
     * @param userStudentNumber
     * @param originName
     * @return
     */
    public String mergeMp3(String bgmPath, String vocalPath, String userStudentNumber, String originName) {
        String outputPath = "/root/TJ_music/static/" + userStudentNumber + "/music/" + originName;
        System.out.println("origin_bgm_path: " + outputPath);
        PythonUtils.getInstance().merge(bgmPath, vocalPath, outputPath);
        return outputPath;
    }

}
