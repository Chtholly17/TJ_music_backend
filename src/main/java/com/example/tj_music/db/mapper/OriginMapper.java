/*
 * @Description: 
 * @Date: 2023-05-07 14:45:36
 * @LastEditTime: 2023-05-26 20:36:20
 */
package com.example.tj_music.db.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.example.tj_music.db.entity.Origin;

import java.util.List;

@Mapper
public interface OriginMapper {
    // Select all origins
    @Select("select * from origin")
    public List<Origin> getAllOrigin();

    // Select origin by tag
    @Select("select * from origin where origin_name like CONCAT('%', #{originTag, jdbcType=VARCHAR}, '%')")
    public List<Origin> selectOriginByTag(String originTag);

    // Select origin by work id
    @Select("select * from origin inner join work on origin.origin_id = work.work_origin_version" +
            " where work.work_id = #{workId}")
    public Origin selectOriginByWorkId(Integer workId);

    // select origin by origin id
    @Select("select * from origin where origin_id = #{originId}")
    public Origin selectOriginByOriginId(Integer originId);


    // insert origin
    @Insert("insert into origin(origin_name, origin_author, origin_bgmusic_filename, origin_voice_filename" +
            ", origin_duration, origin_preface_filename, origin_introduction, origin_lrc_filename, origin_tag) " +
            "values(#{originName}, #{originAuthor}, #{originBgMusicFilename}, #{originVoiceFilename}, #{originDuration}," +
            " #{originPrefaceFilename}, #{originIntroduction},#{originLrcFilename},#{originTag})")
    void insertOrigin(String originName, String originAuthor, String originBgMusicFilename, String originVoiceFilename,
                      Integer originDuration, String originPrefaceFilename, String originIntroduction,String originLrcFilename, String originTag);

    // delete origin by origin id
    @Delete("delete from origin where origin_id = #{originId}")
    public void deleteOriginById(Integer originId);

    // update origin by origin id
    @Select("update origin set origin_name = #{originName}, origin_author = #{originAuthor}, origin_bgmusic_filename = #{originBgMusicFilename}, origin_voice_filename = #{originVoiceFilename}, origin_duration = #{originDuration}, origin_preface_filename = #{originPrefaceFilename}, origin_introduction = #{originIntroduction}, origin_tag = #{originTag} where origin_id = #{originId}")
    public void updateOriginById(Integer originId, String originName, String originAuthor, String originBgMusicFilename, String originVoiceFilename, Integer originDuration, String originPrefaceFilename, String originIntroduction, String originTag);

    // update origin tag by origin id
    @Select("update origin set origin_tag = #{originTag} where origin_id = #{originId}")
    public void updateOriginTagById(Integer originId, String originTag);
}
