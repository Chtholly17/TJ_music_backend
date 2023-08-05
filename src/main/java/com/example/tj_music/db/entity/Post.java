package com.example.tj_music.db.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer postId;
    private Date createTime;
    private User postOwner;
    private String postName;
    private String postContent;
    private Integer postLike;

    public Post() {
    }

    public Post(Integer postId, Date createTime, User postOwner, String postName, String postContent, Integer postLike) {
        this.postId = postId;
        this.createTime = createTime;
        this.postOwner = postOwner;
        this.postName = postName;
        this.postContent = postContent;
        this.postLike = postLike;
    }
}