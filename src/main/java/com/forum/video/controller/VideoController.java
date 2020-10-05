package com.forum.video.controller;

import com.forum.video.bo.Video;
import com.forum.video.result.ResultCodeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.forum.video.result.Result;

import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/storage")
public class VideoController {

    private Logger logger = LoggerFactory.getLogger(VideoController.class);




    public Result<Integer> updateVideo(MultipartFile multipartFile){
        try{

            return Result.success(1);
        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.failure(ResultCodeMessage.SERVER_ERROR);
        }

    }


}
