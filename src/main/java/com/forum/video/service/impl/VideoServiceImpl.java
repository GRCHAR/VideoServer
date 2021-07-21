package com.forum.video.service.impl;

import com.forum.video.bo.Video;
import com.forum.video.config.ConfigParam;
import com.forum.video.dao.VideoDao;
import com.forum.video.ffmpegUtil.FFmpegTool;
import com.forum.video.service.IVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author genghaoran
 */
@Service
@Slf4j
public class VideoServiceImpl implements IVideoService {

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private FFmpegTool fFmpegTool;

    @Autowired
    private ConfigParam configParam;




    @Override
    public Video updateVideo(String title, int userId, MultipartFile multipartFile) throws IOException, InterruptedException {
        Video video = new Video(title, userId);
        int videoId = videoDao.createVideo(video);
        File fileDir = new File(configParam.getPath() + videoId);
        log.info(multipartFile.getOriginalFilename());
        if(!fileDir.exists()){
            if(!fileDir.mkdirs()){
                log.error("not successes mkdir!");
                return null;
            }
        }
        File file = new File(configParam.getPath() + videoId, title);
        log.info("filePath:" + file.getAbsolutePath());
//        CmdResult cmdResult = fFmpegTool.transcodeVideoDefault(file, "/VideoStorage/transcode");
//        if(cmdResult.getExitValue() == 1){
//           return null;
//        }
        multipartFile.transferTo(file);
        video.setUrl(file.getAbsolutePath());
        videoDao.updateVideo(video);
        return video;
    }

    @Override
    public Video getVideoMessage(int id) {
        return videoDao.getVideo(id);
    }

    @Override
    public int deleteVideo(int id){
        Video video = videoDao.getVideo(id);
        File file = new File("/VideoStorage/" + video.getId(), video.getTitle());
        videoDao.deleteVideo(id);
        boolean result = file.delete();
        if(result) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean hasVideo(String title, int userId){
        Integer exist = videoDao.hasVideo(title, userId);
        if(exist != null){
            return true;
        }
        return  false;
    }





}
