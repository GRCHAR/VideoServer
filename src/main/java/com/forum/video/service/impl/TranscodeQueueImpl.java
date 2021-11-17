package com.forum.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.video.bo.TranscodeQueue;
import com.forum.video.dao.TranscodeQueueDao;
import com.forum.video.service.ITranscodeQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author genghaoran
 */
@Service
@Slf4j
public class TranscodeQueueImpl implements ITranscodeQueue {

    @Autowired
    private TranscodeQueueDao transcodeQueueDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addVideoToTranscodeQueue(int videoId){
        log.info("addVideoToTranscodeQueue videoId:" + videoId);
        TranscodeQueue transcodeQueue = new TranscodeQueue();
        transcodeQueue.setVideoId(videoId);
        transcodeQueue.setTimestamp(new Timestamp(System.currentTimeMillis()));
        transcodeQueue.setState(1);
        transcodeQueueDao.insert(transcodeQueue);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteVideoTranscodeQueue(int videoId){
        QueryWrapper<TranscodeQueue> wrapper = new QueryWrapper<>();
        wrapper.eq("video_id", videoId);
        transcodeQueueDao.delete(wrapper);
        return true;
    }


    @Override
    public List<TranscodeQueue> getVideosUnTranscode(){
        List<TranscodeQueue> transcodeQueues;
        try{
            Map<String, Object> transcodeQueryMap = new HashMap<>(1);
            transcodeQueryMap.put("state", 1);
            transcodeQueues = transcodeQueueDao.selectByMap(transcodeQueryMap);
        } catch (Exception e){
            log.error("getVideoUnTranscode error " + e.getMessage());
            return null;
        }
        return transcodeQueues;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSuccessTranscode(){
        Map<String, Object> transcodeQueryMap = new HashMap<>(1);
        transcodeQueryMap.put("state", 0);
        transcodeQueueDao.deleteByMap(transcodeQueryMap);
    }

    @Override
    public int updateTranscode(TranscodeQueue transcodeQueue){
        return transcodeQueueDao.updateById(transcodeQueue);
    }
}
