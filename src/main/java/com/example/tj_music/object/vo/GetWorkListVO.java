package com.example.tj_music.object.vo;

import lombok.Data;


@Data

public class GetWorkListVO {
    int workId;
    String workName;
    String workAuthorNickname;
    int score;
    int like;
    int workAuthorFans;
    int workCommentCnt;
    String workPrefaceFilename;
}
