package com.forum.video.dao;

import com.forum.video.bo.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * @author Administrator
 */
public interface VideoDao {

    /**
     * 添加视频信息
     * @param video 视频对象
     * @return 返回新增视频对象ID
     */
    @Insert("INSERT INTO video(id, title, userId, url) VALUES(#{id}, #{title}, #{userId}, #{url})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int createVideo(Video video);

    /**
     * 根据ID获取视频对象信息
     * @param id 视频ID
     * @return 视频对象
     */
    @Select("SELECT * FROM video WHERE id=#{id}")
    public Video getVideo(int id);


}
