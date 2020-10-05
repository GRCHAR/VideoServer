package com.forum.video.dao;

import com.forum.video.bo.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * @author Administrator
 */
public interface VideoDao {

    /**
     * 上传视频信息添加
     * @param video 视频信息对象
     * @return 视频ID
     */
    @Insert("INSERT INTO storage(#{id}, #{path}) VALUES(#{id}, #{path})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int addVideo(Video video);

    /**
     * 根据ID获取视频信息
     * @param id 视频ID
     * @return 视频对象信息
     */
    @Select("SELECT * FROM storage WHERE id=#{id}")
    public Video getVideo(int id);

}
