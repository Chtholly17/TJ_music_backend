package com.example.tj_music.service;

import com.example.tj_music.db.entity.Appeal;
import com.example.tj_music.db.entity.AppealContent;
import com.example.tj_music.db.mapper.AppealMapper;
import com.example.tj_music.db.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdministerService {
    @Autowired
    private AppealMapper appealMapper;
    @Autowired
    private UserMapper  userMapper;

    // get all the information of appeals
    public List<List<Object>> selectAllAppeals() {
        List<List<Object>> appealInfos = new ArrayList<>();
        // get all appeals and store them in a list
        List<Appeal> appeals = appealMapper.selectAllAppeal();
        // for all the appeals in the list, get the information of the user who made the appeal
        for (Appeal appeal : appeals) {
            // get the user's student number using the appeal's owner
            String studentNumber = userMapper.selectUserById(appeal.getAppealOwner()).getUserStudentNumber();

            // get the user's id using the appeal's owner
            int userId = userMapper.selectUserById(appeal.getAppealOwner()).getUserId();
            // get appeal's id
            int appealId = appeal.getAppealId();
            // get appeal's content
            String appealContent = appeal.getAppealContent();
            // store the appeal's id,student number and content in a list,keep the id type as int
            // and the student number and content type as string
            // init a empty list
            List<Object> appealInfo = new ArrayList<>();
            appealInfo.add(appealId);
            appealInfo.add(studentNumber);
            appealInfo.add(userId);
            appealInfo.add(appealContent);
            // store the appealInfo list in a new list
            appealInfos.add(appealInfo);
        }
        return appealInfos;
    }

    // accept the appeal,take appealId and userId as parameters
    public void acceptAppeal(int appealId, String userStudentNumber) {
        // get the user's id using the user's student number
        int userId = userMapper.selectUserByStudentNumber(userStudentNumber).getUserId();
        // update the user's status to normal
        appealMapper.deleteAppealByAppealOwner(userId);
        userMapper.updateUserStatusById("normal",userId);
        // delete the appeal
        appealMapper.deleteAppealById(appealId);

    }

    // reject the appeal,take appealId and reject reason parameter
    public void rejectAppeal(int appealId, String rejectReason) {
        // change the appeal's status to rejected
        appealMapper.updateAppealStatusById("rejected",appealId);
        // change the appeal's reject reason
        appealMapper.updateAppealRejectReasonById(rejectReason,appealId);
    }


    /**
     * get all appeal content, appeal status and student numbers
     * @return
     */
    public List<AppealContent> getAllAppealContentAndStatusAndStudentNumber() {
        return appealMapper.selectAllAppealContentAndStatusAndStudentNumber();
    }
}
