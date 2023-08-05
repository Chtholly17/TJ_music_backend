package com.example.tj_music.utils;

import com.example.tj_music.object.Result;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource(value = {"classpath:application.properties"})
public class PythonUtils {
    

    private static PythonUtils pythonUtils = new PythonUtils();
    
    public static PythonUtils getInstance() {
        return pythonUtils;
    }

    private PythonUtils() {
    }
    
    /**
     * 获取分数
     * @param originPath
     * @param singPath
     * @return
     */
    public Map<String, String> getScore(String originPath, String singPath) {
        try {
            File workingDirectory = new File("/root/gitcode/music_score");
            ProcessBuilder pb = new ProcessBuilder("/root/miniconda3/envs/music/bin/python3", "main.py", originPath, singPath);
//            File workingDirectory = new File("/root/TJ_music");
//            ProcessBuilder pb = new ProcessBuilder("/opt/homebrew/bin/python3", "test.py", s, "arg2");
            pb.directory(workingDirectory);
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();

            String currentPath = System.getProperty("user.dir");
            System.out.println("Current Path: " + currentPath);

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder preciseScore = new StringBuilder();
            StringBuilder qualityScore = new StringBuilder();
            StringBuilder pitchScore = new StringBuilder();

            while ((line = in.readLine()) != null) {
                switch (line) {
                    case "precise_score":
                        preciseScore.append(in.readLine());
                        break;
                    case "quality_score":
                        qualityScore.append(in.readLine());
                        break;
                    case "pitch_score":
                        pitchScore.append(in.readLine());
                        break;
                }
            }
            in.close();

            // 等待脚本执行完毕并获取退出代码
            int exitCode = process.waitFor();
            String scriptOutput = preciseScore.toString() + " " + qualityScore.toString() + " " + pitchScore.toString();

//            System.out.println("Python script exited with code: " + exitCode);
            System.out.println("Script output: " + scriptOutput);
            Map<String, String> scoreList = new HashMap<>();
            scoreList.put("preciseScore", preciseScore.toString());
            scoreList.put("qualityScore", qualityScore.toString());
            scoreList.put("pitchScore", pitchScore.toString());
            System.out.println(preciseScore.toString());
            System.out.println(qualityScore.toString());
            System.out.println(pitchScore.toString());
            return scoreList;
//            return Result.success(scoreList);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
//        return Result.fail("fail");
    }

    /**
     * get comments by scores
     * @param preciseScore
     * @param qualityScore
     * @param pitchScore
     * @return
     */
    public Result getComments(String preciseScore, String qualityScore, String pitchScore) {
        try {
            File workingDirectory = new File("/root/gpt_call/");
            ProcessBuilder pb = new ProcessBuilder("/root/miniconda3/envs/music/bin/python3", "main.py", preciseScore, qualityScore, pitchScore);
            pb.directory(workingDirectory);
            pb.environment().put("PYTHONIOENCODING", "UTF-8");
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder comments = new StringBuilder();
            while ((line = in.readLine()) != null) {
                comments.append(line);
            }
            in.close();
            int exitCode = process.waitFor();
            String scriptOutput = comments.toString();
            System.out.println("Script output: " + scriptOutput);
            return Result.success(comments.toString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * merge mp3 from bgm and vocal
     * @param bgmPath
     * @param vocalPath
     * @param outputPath
     * @return
     */
    public Result merge(String bgmPath, String vocalPath, String outputPath) {
        try {
            File workingDirectory = new File("/root/gitcode/music_score/");
            ProcessBuilder pb = new ProcessBuilder("/root/miniconda3/envs/music/bin/python3",
                    "merge.py", bgmPath, vocalPath, outputPath);
            pb.directory(workingDirectory);
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder outputs = new StringBuilder();
            while ((line = in.readLine()) != null) {
                outputs.append(line);
            }
            in.close();
            int exitCode = process.waitFor();
            String scriptOutput = outputs.toString();
            System.out.println("Script output: " + scriptOutput);
            return Result.success();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
