package com.forum.video.service.impl;

import com.forum.video.bo.Video;
import com.forum.video.config.ConfigParam;
import com.forum.video.dao.VideoDao;
import com.forum.video.ffmpegUtil.CmdResult;
import com.forum.video.ffmpegUtil.FFmpegTool;
import com.forum.video.service.IVideoService;
import com.forum.video.service.rabbit.RabbitMqSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RabbitMqSender rabbitMqSender;


    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5), r -> new Thread(r, "upload-transcode-video"));


    @Override
    public Video updateVideo(String title, int userId, MultipartFile multipartFile){
        Video video = new Video(title, userId);
        videoDao.createVideo(video);
        int videoId = video.getId();
        File fileDir = new File(configParam.getPath() + videoId);
        log.info(multipartFile.getOriginalFilename());
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                log.error("not successes mkdir!");
                return null;
            }
        }
        File file = new File(configParam.getPath() + videoId, title);
        log.info("filePath:" + file.getAbsolutePath());
        executor.execute(() -> {
            try {
                try {
                    multipartFile.transferTo(file);
                    JSONObject jsonObject;
                    jsonObject = new JSONObject();
                    jsonObject.put("code", 2);
                    jsonObject.put("videoId", videoId);
                    jsonObject.put("title", title);
                    jsonObject.put("userId", userId);
                    jsonObject.put("state", "upload");
                    jsonObject.put("message", "开始上传");
                    rabbitMqSender.send(jsonObject.toString());
                    CmdResult cmdResult;
                    cmdResult = fFmpegTool.transcodeVideoDefault(file, configParam.getPath() + videoId);
                    if (cmdResult.getExitValue() == 1) {
                        video.setState("fail");
                        videoDao.updateVideo(video);
                        jsonObject = new JSONObject();
                        jsonObject.put("code", 1);
                        jsonObject.put("videoId", videoId);
                        jsonObject.put("userId", userId);
                        jsonObject.put("title", title);
                        jsonObject.put("state", "fail");
                        jsonObject.put("message", "cmdResult.getExitValue() == 1");
                    } else {
                        video.setState("success");
                        videoDao.updateVideo(video);
                        jsonObject = new JSONObject();
                        jsonObject.put("code", 0);
                        jsonObject.put("videoId", videoId);
                        jsonObject.put("userId", userId);
                        jsonObject.put("title", title);
                        jsonObject.put("state", "success");
                    }
                    rabbitMqSender.send(jsonObject.toString());

                } catch (IOException | InterruptedException e) {
                    video.setState("fail");
                    videoDao.updateVideo(video);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("videoId", videoId);
                    jsonObject.put("userId", userId);
                    jsonObject.put("title", title);
                    jsonObject.put("state", "fail");
                    jsonObject.put("message", e.getMessage());
                    rabbitMqSender.send(jsonObject.toString());
                    log.error(e.getMessage());
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        video.setUrl(file.getAbsolutePath());
        videoDao.updateVideo(video);
        return video;
    }

    @Override
    public void testQueue(){
        rabbitMqSender.sendTest();
    }

    @Override
    public Video getVideoMessage(int id) {
        return videoDao.getVideo(id);
    }

    @Override
    public int deleteVideo(int id) {
        Video video = videoDao.getVideo(id);
        File file = new File("/VideoStorage/" + video.getId(), video.getTitle());
        videoDao.deleteVideo(id);
        boolean result = file.delete();
        if (result) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean hasVideo(String title, int userId) {
        Integer exist = videoDao.hasVideo(title, userId);
        return exist != null;
    }


}
