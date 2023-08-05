package com.example.tj_music.db.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Appeal {
    @Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer appealId;
    private Date createTime;
    private String appealContent;
    private Integer appealOwner;

    private String appealStatus;

    private String appealRejectReason;

}

