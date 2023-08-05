package com.example.tj_music.object.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMessageBriefListVO {
    String StudentNumber;
    String nickname;
    String profileImageFilename;
    String lastMessageContent;

}
