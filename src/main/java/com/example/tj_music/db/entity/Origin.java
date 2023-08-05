package com.example.tj_music.db.entity;

import java.sql.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class Origin {

    // Declare the fields of the entity class
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private int originId;
    private String originName;
    private String originAuthor;

    private String originBgMusicFilename;

    private String originVoiceFilename;

    private Integer originDuration;

    private String originPrefaceFilename;

    private String originLrcFilename;

    private String originIntroduction;

    private String originTag;

}