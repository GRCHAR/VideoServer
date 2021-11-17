package com.forum.video.service.impl;

import com.forum.video.bo.DashQueue;
import com.forum.video.bo.Video;
import com.forum.video.dao.DashQueueDao;
import com.forum.video.dao.TranscodeQueueDao;
import com.forum.video.dao.VideoDao;
import com.forum.video.service.IDashQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PseudoColumnUsage;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author genghaoran
 */
@Service
public class DashQueueImpl implements IDashQueue {

    @Autowired
    private DashQueueDao dashQueueDao;

    @Autowired
    private TranscodeQueueDao transcodeQueueDao;

    @Autowired
    private VideoDao videoDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addVideoToDashQueue(){
        Map<String, Object> searchMap = new HashMap<>(1);
        searchMap.put("state", "transcoded");
        videoDao.selectByMap(searchMap).forEach(video -> {
            DashQueue dashQueue = new DashQueue();
            dashQueue.setVideoId(video.getId());
            dashQueue.setTimestamp(new Timestamp(System.currentTimeMillis()));
            dashQueue.setState(1);
            dashQueueDao.insert(dashQueue);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DashQueue> getUnDashVideoFromQueue(){
        Map<String,Object> searchMap = new HashMap<>(1);
        searchMap.put("state", 1);
        return dashQueueDao.selectByMap(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSuccessVideoFromQueue(){
        Map<String, Object> searchMap = new HashMap<>(1);
        searchMap.put("state", 0);
        dashQueueDao.deleteByMap(searchMap);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDashQueue(DashQueue dashQueue){
        return dashQueueDao.updateById(dashQueue);
    }


}
