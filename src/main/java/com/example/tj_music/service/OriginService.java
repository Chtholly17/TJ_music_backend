package com.example.tj_music.service;

import com.example.tj_music.db.entity.Origin;
import com.example.tj_music.db.entity.OriginFrontEnd;
import com.example.tj_music.db.entity.Work;
import com.example.tj_music.db.mapper.OriginMapper;

import com.example.tj_music.db.mapper.WorkCommentMapper;
import com.example.tj_music.db.mapper.WorkMapper;
import com.example.tj_music.utils.ImageUtils;
import com.example.tj_music.utils.LrcUtils;
import com.example.tj_music.utils.MusicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.tj_music.object.Result;

import java.util.EnumMap;
import java.util.List;

import static java.lang.Math.round;

@Service
public class OriginService {
    @Autowired
    private OriginMapper originMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private WorkCommentMapper workCommentMapper;

    /**
     * search origin by key word.
     * code:0 represents origin does not exist.
     * code:1 represents search succeeded.
     * @param originTag
     * @return Result
     */
    public Result searchOriginByTag(String originTag) {
        List<Origin> originList =  originMapper.selectOriginByTag(originTag);
        if (originList.isEmpty()) {
            return Result.fail("origin does not exist.");
        }
        return Result.success(originList);
    }

    /**
     * search origin by work id.
     * code:0 represents origin does not exist.
     * code:1 represents search succeeded.
     * @param workId
     * @return Result
     */
    public Result searchOriginByWorkId(Integer workId) {
        Origin origin = originMapper.selectOriginByWorkId(workId);
        if (origin == null) {
            return Result.fail("origin does not exist.");
        }
        return Result.success(origin);
    }

    /**
     * get origin by origin id.
     * code:0 represents origin does not exist.
     * code:1 represents search succeeded.
     * @param originId
     * @return Origin
     */
    public Origin getOriginByOriginId(Integer originId) {
        return originMapper.selectOriginByOriginId(originId);
    }

    public Result getOriginList() {
        List<Origin> originList = originMapper.getAllOrigin();
        if (originList.isEmpty()) {
            return Result.fail("origin does not exist.");
        }
        return Result.success(originList);
    }
    public void insertOrigin(OriginFrontEnd originFrontEnd, Integer originId) {
        //  copy non-file attributes from originFrontEnd to origin
        Origin origin = new Origin();
        origin.setOriginName(originFrontEnd.getOriginName());
        origin.setOriginAuthor(originFrontEnd.getOriginAuthor());
        origin.setOriginDuration(10);
        origin.setOriginIntroduction(originFrontEnd.getOriginIntroduction());
        origin.setOriginTag(originFrontEnd.getOriginMusicTag());

        // insert origin
        EnumMap<MusicUtils.UploadResult,Object> ret;

        // get the time as the filename
        long time = System.currentTimeMillis();
        String filename_time = String.valueOf(time);

        // insert bgm
        try {
             ret = MusicUtils.getInstance().upload(originFrontEnd.getOriginBgMusicFile(), "admin", filename_time+"_bgmusic");
             origin.setOriginBgMusicFilename(ret.get(MusicUtils.UploadResult.URL).toString());
         } catch (Exception e) {
             e.printStackTrace();
             return;
         }
        // insert voice
         try {
             ret = MusicUtils.getInstance().upload(originFrontEnd.getOriginVoiceFile(), "admin", filename_time+"_voice");
             origin.setOriginVoiceFilename(ret.get(MusicUtils.UploadResult.URL).toString());
         } catch (Exception e) {
             e.printStackTrace();
             return;
         }
//         ImageUtils imageUtils = new ImageUtils();
         try {
             String prefaceFilename = ImageUtils.getInstance().upload(originFrontEnd.getOriginPrefaceFile(), "admin", filename_time+"_preface");
             origin.setOriginPrefaceFilename(prefaceFilename);
         } catch (Exception e) {
             e.printStackTrace();

             return;
         }
         // insert lrc
        try {
            EnumMap<LrcUtils.UploadResult,Object> lrc_ret;
            lrc_ret = LrcUtils.getInstance().upload(originFrontEnd.getOriginLrcFile(), "admin", filename_time+"_lrc");
            origin.setOriginLrcFilename(lrc_ret.get(LrcUtils.UploadResult.URL).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // search whether the given origin id exist
        boolean exist = originMapper.selectOriginByOriginId(originId) != null;

        if(!exist) {
            originMapper.insertOrigin(origin.getOriginName(), origin.getOriginAuthor(), origin.getOriginBgMusicFilename(), origin.getOriginVoiceFilename(), origin.getOriginDuration(), origin.getOriginPrefaceFilename(), origin.getOriginIntroduction(),origin.getOriginLrcFilename(), origin.getOriginTag());
            return;
        }
        originMapper.updateOriginById(originId, origin.getOriginName(), origin.getOriginAuthor(), origin.getOriginBgMusicFilename(), origin.getOriginVoiceFilename(), origin.getOriginDuration(), origin.getOriginPrefaceFilename(), origin.getOriginIntroduction(), origin.getOriginTag());
    }

    public Result deleteOrigin(Integer originId) {
        Origin origin = originMapper.selectOriginByOriginId(originId);
        if (origin == null) {
            return Result.fail("origin does not exist.");
        }
        // select all the works of the origin
        List<Work> workIdList = workMapper.selectWorkByOriginId(originId);
        // traverse the work list
        for (Work work : workIdList) {
            // delete the work_comment of the work
            workCommentMapper.deleteWorkCommentByTarget(work.getWorkId());
            // delete the work
            workMapper.deleteWorkById(work.getWorkId());
        }
        // delete the origin
        originMapper.deleteOriginById(originId);
        return Result.success("delete origin successfully.");
    }

    /**
     * update origin tag by origin id
     * @param originId
     * @param originTag
     */
    public void updateOriginTag(Integer originId, String originTag) {
        originMapper.updateOriginTagById(originId, originTag);
    }
}
