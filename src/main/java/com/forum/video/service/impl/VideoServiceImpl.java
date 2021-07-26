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
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
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


    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ffmpeg");
        }
    });


    @Override
    public Video updateVideo(String title, int userId, MultipartFile multipartFile) throws IOException, InterruptedException {
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
                    CmdResult cmdResult = null;
                    cmdResult = fFmpegTool.transcodeVideoDefault(file, configParam.getPath() + videoId);
                    if (cmdResult.getExitValue() == 1) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 1);
                        jsonObject.put("videoId", videoId);
                        jsonObject.put("userId", userId);
                        jsonObject.put("message", "cmdResult.getExitValue() == 1");
                        rabbitMqSender.send(jsonObject.toString());
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", 0);
                        jsonObject.put("videoId", videoId);
                        jsonObject.put("userId", userId);
                        rabbitMqSender.send(jsonObject.toString());
                    }

                } catch (IOException | InterruptedException e) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 1);
                    jsonObject.put("videoId", videoId);
                    jsonObject.put("userId", userId);
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
        if (exist != null) {
            return true;
        }
        return false;
    }


}
