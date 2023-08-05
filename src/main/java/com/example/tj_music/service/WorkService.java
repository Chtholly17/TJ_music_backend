package com.example.tj_music.service;

import com.example.tj_music.object.vo.GetWorkListVO;
import com.example.tj_music.db.entity.Origin;
import com.example.tj_music.db.entity.User;
import com.example.tj_music.db.mapper.OriginMapper;
import com.example.tj_music.db.mapper.UserMapper;
import com.example.tj_music.db.mapper.WorkCommentMapper;
import com.example.tj_music.object.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tj_music.db.mapper.WorkMapper;
import com.example.tj_music.db.entity.Work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkService {
    @Autowired
    private WorkMapper workMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkCommentMapper workCommentMapper;

    @Autowired
    private OriginMapper originMapper;


    public Result selectWorkByOriginId(int originId) {
        List<Work> works = workMapper.selectWorkByOriginId(originId);
        // init a list of map
        List<Map<String, Object>> list = new ArrayList<>();
        // traverse works
        for(Work work : works) {
            // get user id
            int userId = work.getWorkOwner();
            // get user nickname
            String nickname = userMapper.selectUserById(userId).getUserNickname();
            String userStudentNumber = userMapper.selectUserById(userId).getUserStudentNumber();
            Map<String, Object> map = new HashMap<>();
            map.put("userNickname", nickname);
            map.put("userStudentNumber", userStudentNumber);
            map.put("work", work);

            list.add(map);
        }
        if(works.size() == 0) {
            return Result.fail("no work with origin id " + originId);
        }
        return Result.success(list);
    }

    public Result getNWorks(int n) {
        List<Work> works = workMapper.selectNWorksWithHighestNWorkLike(n);
        if(works == null) {
            return Result.fail("there are no n works");
        }
        return Result.success(works);
    }

    public Result getWorkList(String tag, String order) {
        // print out the tag and order
//        System.out.println("tag: " + tag + " order: " + order);

        // update the data of work with given tag
        List<Work> works_to_update = workMapper.getWorksByTag(tag);
        for(Work work : works_to_update) {
            int workId = work.getWorkId();
            int workLike = work.getWorkLike();
            int workCommentCnt = workMapper.getWorkCommentCntById(workId);
            User user = userMapper.selectUserById(work.getWorkOwner());
            int userFansCnt = user.getUserFansCnt();
            workMapper.updateWorkCommentCnt(workId, workCommentCnt);
            workMapper.updateWorkOwnerFans(workId, userFansCnt);
        }


        List<Work> works = null;
        List<GetWorkListVO> worksVO = new ArrayList<>();
        if (order.equals("like")) {
            works = workMapper.selectWorkByTagWorkLikeDesc(tag);
        } else if (order.equals("comment")) {
            works = workMapper.selectWorkByTagCommentNumberDesc(tag);
        } else {
            works = workMapper.selectWorkByTagUserFansDesc(tag);
        }
        if(works == null) {
            return Result.fail("no work with tag " + tag);
        }

        // print out the works
//        System.out.println("works: " + works);

        // iterate works
        for(Work work : works) {
            GetWorkListVO vo = new GetWorkListVO();
            User owner = userMapper.selectUserById(work.getWorkOwner());
            Origin origin = originMapper.selectOriginByOriginId(work.getWorkOriginVersion());
            vo.setWorkId(work.getWorkId());
            vo.setWorkName(work.getWorkName());
            vo.setWorkAuthorNickname(owner.getUserNickname());
            vo.setScore(work.getWorkPreciseScore());
            vo.setLike(work.getWorkLike());
            vo.setWorkAuthorFans(owner.getUserFansCnt());
            vo.setWorkCommentCnt(workMapper.getWorkCommentCntById(work.getWorkId()));
            vo.setWorkPrefaceFilename(work.getWorkPrefaceFilename());

            worksVO.add(vo);
        }

        return Result.success(worksVO);
    }

    // delete the work and corresponding comments
    public boolean deleteWorkAndCommentById(int workId) {
        // check if the work exists

        if(workCommentMapper.selectWorkCommentByTarget(workId) == null) {
            return false;
        }
        // delete the work
        workCommentMapper.deleteWorkCommentById(workId);
        // delete the comments
        workMapper.deleteWorkAndCommentById(workId);

        return true;
    }

    public Result getWorkById(int workId) {
        Work work = workMapper.selectWorkByWorkId(workId);
        if(work == null) {
            return Result.fail("no work with id " + workId);
        }
        return Result.success(work);
    }

    /**
     * insert work
     * @return
     */
    public Result insertWork(String workName, String workComment, String userStudentNumber, 
                             Integer workOriginVersion, Integer workLike, String workVoiceFilename, 
                             String workTag, String workPrefaceFilename, Integer workQualityScore, 
                             Integer workPreciseScore, Integer workPitchScore) {
        // construct a temp work
        Work work = new Work();
        // use the student number to find the user id
        User user = userMapper.selectUserByStudentNumber(userStudentNumber);
        if(user == null) {
            return Result.fail("user not found with student number " + userStudentNumber);
        }

        Origin origin=originMapper.selectOriginByOriginId(workOriginVersion);
        if(origin==null){
            return Result.fail("origin not found with origin id " + workOriginVersion);
        }


        // fill the work
        work.setWorkName(workName);
        work.setWorkComment(workComment);
        work.setWorkOwner(user.getUserId());
        work.setWorkOriginVersion(workOriginVersion);
        work.setWorkLike(workLike);
        work.setWorkVoiceFilename(workVoiceFilename);
        work.setWorkTag(workTag);
        work.setWorkPrefaceFilename(workPrefaceFilename);
        work.setWorkQualityScore(workQualityScore);
        work.setWorkPreciseScore(workPreciseScore);
        work.setWorkPitchScore(workPitchScore);
        work.setWorkTag(origin.getOriginTag());
        work.setWorkCommentCnt(0);
        work.setWorkOwnerFansCnt(user.getUserFansCnt());


        // print out all the attributes
        System.out.println(work.toString());

        workMapper.insertWork(work.getWorkName(), work.getWorkComment(), work.getWorkOwner(), work.getWorkOriginVersion(), work.getWorkLike(), work.getWorkVoiceFilename(), work.getWorkTag(), work.getWorkPrefaceFilename(), work.getWorkQualityScore(), work.getWorkPreciseScore(), work.getWorkPitchScore());

        return Result.success(work);
    }

    // add like to work
    public Result addLikeToWork(int workId) {
        Work work = workMapper.selectWorkByWorkId(workId);
        if(work == null) {
            return Result.fail("no work with id " + workId);
        }
        workMapper.addLikeToWork(workId);
        return Result.success();
    }
}
