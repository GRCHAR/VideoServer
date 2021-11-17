package com.forum.video.service;

import com.forum.video.bo.TranscodeQueue;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author genghaoran
 */
public interface ITranscodeQueue {
    /**
     * 将上传后的视频添加到转码队列
     * @param videoId 视频ID
     */
    @Transactional(rollbackFor = Exception.class)
    void addVideoToTranscodeQueue(int videoId);

    /**
     * 从转码队列删除指定videoId的视频
     * @param videoId 视频ID
     * @return true
     */
    @Transactional(rollbackFor = Exception.class)
    boolean deleteVideoTranscodeQueue(int videoId);

    /**
     * 获取未转码的视频
     * @return 未转码的视频List
     */
    List<TranscodeQueue> getVideosUnTranscode();

    /**
     * 删除转码成功的视频
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteSuccessTranscode();

    int updateTranscode(TranscodeQueue transcodeQueue);
}
