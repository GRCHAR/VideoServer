package com.forum.video.dao;

import com.forum.video.bo.Video;
import org.apache.ibatis.annotations.*;

/**
 * @author Administrator
 */
@Mapper
public interface VideoDao {

    /**
     * 添加视频信息
     * @param video 视频对象
     * @return 返回新增视频对象ID
     */
    @Insert("INSERT INTO video(id, title, user_id, url) VALUES(#{id}, #{title}, #{userId}, #{url})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int createVideo(Video video);

    /**
     * 更新视频url信息
     * @param video 视频对象
     */
    @Update("UPDATE video SET url=#{url} WHERE id=#{id}")
    void updateVideo(Video video);

    /**
     * 查找是否存在video
     * @param title 视频标题
     * @param userId 用户ID
     * @return 返回查找结果
     */
    @Select("SELECT 1 FROM video WHERE title=#{title} AND user_id=#{userId} LIMIT 1")
    Integer hasVideo(String title, int userId);

    /**
     * 根据ID获取视频对象信息
     * @param id 视频ID
     * @return 视频对象
     */
    @Select("SELECT * FROM video WHERE id=#{id}")
    Video getVideo(int id);

    /**
     * 根据视频ID删除视频信息
     * @param id 视频ID
     * @return 视频对象
     */
    @Delete("DELETE FROM video WHERE id=#{id}")
    int deleteVideo(int id);







}
