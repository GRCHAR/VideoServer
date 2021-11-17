package com.forum.video.service;


import com.forum.video.bo.DashQueue;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author genghaoran
 */
public interface IDashQueue {

    /**
     * 添加转码后的视频到dash队列
     */
    @Transactional(rollbackFor = Exception.class)
    void addVideoToDashQueue();

    /**
     * 获取dash队列中所有未进行dash转码的视频
     * @return 未转码视频list
     */
    @Transactional(rollbackFor = Exception.class)
    List<DashQueue> getUnDashVideoFromQueue();

    /**
     * 从dash队列中删除已经完成dash转码的视频
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteSuccessVideoFromQueue();

    int updateDashQueue(DashQueue dashQueue);
}
