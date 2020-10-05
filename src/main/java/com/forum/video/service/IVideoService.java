package com.forum.video.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Administrator
 */
public interface IVideoService {

    /**
     * 处理视频上传
     * @param multipartFile 视频上传文件
     * @return 视频上传状态
     * @exception IOException 上传时异常
     */
    public int updateVideo(MultipartFile multipartFile) throws IOException;
}
