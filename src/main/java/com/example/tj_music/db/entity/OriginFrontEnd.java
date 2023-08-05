/*
 * @Description: 
 * @Date: 2023-05-26 20:35:50
 * @LastEditTime: 2023-05-26 21:33:20
 */
package com.example.tj_music.db.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OriginFrontEnd {
    String originName;
    String originAuthor;
    MultipartFile originBgMusicFile;
    MultipartFile originVoiceFile;
    MultipartFile originPrefaceFile;
    MultipartFile originLrcFile;
    String originIntroduction;
    String originMusicTag;
//    MultipartFile[] file;
}
