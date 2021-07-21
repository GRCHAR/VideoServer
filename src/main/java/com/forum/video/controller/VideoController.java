package com.forum.video.controller;

import com.forum.video.bo.Video;
import com.forum.video.result.Result;
import com.forum.video.result.ResultCodeMessage;
import com.forum.video.service.IVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;

/**
 * @author genghaoran
 */
@RestController
@RequestMapping(value = "/video")
public class VideoController {

    Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private IVideoService videoService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<Video> createVideo(@RequestParam String title, @RequestParam int userId, @RequestParam("video") MultipartFile multipartFile){
        Video video = new Video();
        int videoId = 0;
        try{
            boolean hasVideo = videoService.hasVideo(title, userId);
            if(hasVideo){
                return Result.failure(ResultCodeMessage.ALREADY_HAVE_VIDEO);
            }
            video = videoService.updateVideo(title, userId, multipartFile);
        }catch (Exception e){
            logger.error("createVideo " + title + " " +userId);
            logger.error(e.getMessage());
            return Result.failure(ResultCodeMessage.SERVER_ERROR);
        }

        return Result.success(video);
    }




}
