package com.forum.video.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.forum.video.bo.Live;
import com.forum.video.dao.LiveDao;
import com.forum.video.service.ILiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.nio.file.Watchable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author genghaoran
 */
@Service
@Slf4j
public class LiveServiceImpl implements ILiveService {

    @Autowired
    private LiveDao liveDao;


    @Override
    public Live createLive(int userId, String title){
        try{
            Live live = new Live(userId, title);
            liveDao.insert(live);
            return live;
        } catch (Exception e){
            e.printStackTrace();
            log.error("createLive error, message:" + e.getMessage());
        }
        return null;
    }

    @Override
    public Live getLive(int userId){
        try{
            Map<String, Object> searchMap = new HashMap<>(1);
            searchMap.put("user_id", userId);
            List<Live> liveList = liveDao.selectByMap(searchMap);
            return liveList.get(0);
        } catch (Exception e){
            e.printStackTrace();
            log.error("getLive error, message:" + e.getMessage());
        }
        return null;
    }

    @Override
    public Live updateLiveTitle(String title, int userId){
        try{
            Live live = new Live(userId, title);
            UpdateWrapper<Live> wrapper = new UpdateWrapper<Live>();
            wrapper.eq("user_id", userId);
            liveDao.update(live, wrapper);
        } catch (Exception e){
            e.printStackTrace();
            log.error("updateLiveTitle error, message:" + e.getMessage());
        }
        return null;
    }

    @Override
    public Live startLive(int userId){
        try{
            Map<String, Object> searchMap = new HashMap<>(1);
            searchMap.put("user_id", userId);
            List<Live> liveList = liveDao.selectByMap(searchMap);
            Live live = liveList.get(0);
            live.setState(1);
            UpdateWrapper<Live> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", userId);
            liveDao.update(live, updateWrapper);
            return live;
        } catch (Exception e){
            e.printStackTrace();
            log.error("startLive error, message:" + e.getMessage());
        }
        return null;
    }
}
