package com.forum.video.controller;

import com.forum.video.bo.Video;
import com.forum.video.result.Result;
import com.forum.video.result.ResultCodeMessage;
import com.forum.video.service.IVideoService;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    public Result<Video> createVideo(@RequestParam String title, @RequestParam int userId, @RequestParam("file") MultipartFile multipartFile){
        Video video = new Video();
        int videoId = 0;
        try{
            boolean hasVideo = videoService.hasVideo(title, userId);
            if(hasVideo){
                return Result.failure(ResultCodeMessage.ALREADY_HAVE_VIDEO);
            }
            video = videoService.uploadVideo(title, userId, multipartFile);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("createVideo " + title + " " +userId);
            logger.error(e.getMessage());
            return Result.failure(ResultCodeMessage.SERVER_ERROR);
        }
        return Result.success(video);
    }

    @GetMapping(value = "/{videoId}/{fileName}")
    public void getDashMpd(HttpServletResponse response, @PathVariable String videoId, @PathVariable String fileName){
//        response.setContentType("application/force-download");
//        response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());
        try{
            if(Objects.equals(fileName, "getDash")){
                videoService.getVideoFile(response, Integer.parseInt(videoId), "getDash");
            } else {
                videoService.getVideoFile(response, Integer.parseInt(videoId), fileName);
            }
        }catch (Exception e){
            logger.error("getDashMpd error:" + e.getMessage());
            e.printStackTrace();
        }

    }

    @GetMapping(value = "/getImage")
    public void getVideoImage(HttpServletResponse response,@RequestParam int videoId, @RequestParam int imageId){
        try{
            videoService.getVideoImage(response, videoId, imageId);
        } catch (Exception e){
            logger.error("getImage error:" + e.getMessage());
            e.printStackTrace();
        }
    }


    @GetMapping(value = "/getVideoPage")
    public Result<List<Video>> getPageVideo(@RequestParam int pageNumber, @RequestParam String searchName){
        List<Video> videos = new ArrayList<>();
        try {
            videos = videoService.getVideoPageList(pageNumber, searchName);
        } catch (Exception e){
            logger.error("getPageVideo error, message:" + e.getMessage());
            return Result.failure(ResultCodeMessage.SERVER_ERROR);
        }
        return Result.success(videos);
    }

    @GetMapping(value = "/getVideoTotal")
    public Result<Long> getPageTotal(@RequestParam String searchName){
        try{
            Long pageTotal = videoService.getVideoPageTotal(searchName);
            return Result.success(pageTotal);
        } catch (Exception e){
            logger.error("getPageTotal error, message:" + e.getMessage());
            e.printStackTrace();
        }
        return Result.failure(ResultCodeMessage.SERVER_ERROR);
    }


//    @GetMapping(value = "/{videoId}/{m4sName}")
//    public void getM4s(HttpServletResponse response, @PathVariable String m4sName){
////        response.setContentType("application/force-download");
////        response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());
//        try{
//            videoService.getVideoFile(response, -1, m4sName);
//        } catch (Exception e){
//            logger.error("getM4s error:" + e.getMessage());
//            e.printStackTrace();
//        }
//
//    }




    @RequestMapping(method = RequestMethod.GET, value = "/testQueue")
    public Result testQueue(){
        videoService.testQueue();
        return Result.success();
    }







}
