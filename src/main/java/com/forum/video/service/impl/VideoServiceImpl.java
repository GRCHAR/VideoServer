package com.forum.video.service.impl;

import com.forum.video.bo.Video;
import com.forum.video.config.ConfigParam;
import com.forum.video.dao.VideoDao;
import com.forum.video.ffmpegUtil.CmdResult;
import com.forum.video.ffmpegUtil.FFmpegTool;
import com.forum.video.service.ITranscodeQueue;
import com.forum.video.service.IVideoService;
import com.forum.video.service.rabbit.RabbitMqSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
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

    @Autowired
    private ITranscodeQueue transcodeQueueService;


    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5), r -> new Thread(r, "upload-video"));


    @Override
    public Video uploadVideo(String title, int userId, MultipartFile multipartFile){
        Video video = new Video(title, userId);
        videoDao.insert(video);
        log.info("videoId:" + video.getId());
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
                    JSONObject jsonObject;
                    jsonObject = new JSONObject();
                    jsonObject.put("code", 2);
                    jsonObject.put("videoId", videoId);
                    jsonObject.put("title", title);
                    jsonObject.put("userId", userId);
                    jsonObject.put("state", "upload");
                    jsonObject.put("message", "开始上传");
                    multipartFile.transferTo(file);
                    rabbitMqSender.send(jsonObject.toString());
                    transcodeQueueService.addVideoToTranscodeQueue(videoId);
                    jsonObject.put("state", "transcode");
                    jsonObject.put("message", "开始转码");
                    rabbitMqSender.send(jsonObject.toString());

                } catch (Exception e) {
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

    @Override
    public Video updateVideo(Video video){
        videoDao.updateById(video);
        return videoDao.selectById(video.getId());
    }

    @Override
    public void getVideoFile(HttpServletResponse response, int videoId, String fileName) {
        byte[] buffer = new byte[1024];
        String dashPath = configParam.getDashPath();
        try {
            File file;
            if("getDash".equals(fileName)){
                file = new File(dashPath + "/" + videoId + "/" + videoDao.getVideo(videoId).getTitle() + ".mpd");
            } else {
                file = new File(dashPath + "/" + videoId + "/" + fileName);

            }
            BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            int readNumber = bufferedInputStream.read(buffer, 0 , buffer.length);
            while (readNumber != -1){
                outputStream.write(buffer, 0, readNumber);
                readNumber = bufferedInputStream.read(buffer, 0, buffer.length);
            }
            outputStream.flush();
            outputStream.close();
            bufferedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
