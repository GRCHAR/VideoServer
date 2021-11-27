package com.forum.video.controller;

import com.forum.video.bo.Live;
import com.forum.video.result.Result;
import com.forum.video.result.ResultCodeMessage;
import com.forum.video.service.ILiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.xml.ws.soap.Addressing;
import java.util.Map;

/**
 * @author genghaoran
 */
@RestController
@RequestMapping(value = "/live")
@Slf4j
public class LiveController {

    @Autowired
    private ILiveService liveService;

    @PostMapping(value = "/create")
    public Result<Live> createLive(@RequestBody Map<String, Object> map){
        try{
            int userId = (int) map.get("userId");
            String title = String.valueOf(map.get("title"));
            Live live = liveService.createLive(userId, title);
            return Result.success(live);
        } catch (Exception e){
            e.printStackTrace();
            log.error("createLive error, message" + e.getMessage());
        }
        return Result.failure(ResultCodeMessage.SERVER_ERROR);
    }

    @PostMapping(value = "/updateTitle")
    public Result<Live> updateLiveTitle(@RequestParam int userId, @RequestParam String title){
        try{
           Live live = liveService.updateLiveTitle(title, userId);
           return Result.success(live);
        } catch (Exception e){
            e.printStackTrace();
            log.error("updateLive error, message" + e.getMessage());
        }
        return Result.failure(ResultCodeMessage.SERVER_ERROR);
    }

    @PostMapping(value = "/startLive")
    public Result<Live> startLive(@RequestParam int userId){
        try{
            Live live = liveService.startLive(userId);
            return Result.success(live);
        } catch (Exception e){
            log.error("startLive error, message:" + e.getMessage());
        }
        return Result.failure(ResultCodeMessage.SERVER_ERROR);
    }

}

