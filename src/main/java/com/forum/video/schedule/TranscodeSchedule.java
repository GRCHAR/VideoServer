package com.forum.video.schedule;

import com.forum.video.bo.DashQueue;
import com.forum.video.bo.TranscodeQueue;
import com.forum.video.bo.Video;
import com.forum.video.config.ConfigParam;
import com.forum.video.ffmpegUtil.CmdResult;
import com.forum.video.ffmpegUtil.FFmpegTool;
import com.forum.video.service.IDashQueue;
import com.forum.video.service.ITranscodeQueue;
import com.forum.video.service.IVideoService;
import com.forum.video.service.rabbit.RabbitMqSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author genghaoran
 */
@Component
@Slf4j
public class TranscodeSchedule {

    @Autowired
    private FFmpegTool fFmpegTool;

    @Autowired
    private ConfigParam configParam;

    @Autowired
    private ITranscodeQueue transcodeQueueService;

    @Autowired
    private IVideoService videoService;

    @Autowired
    private IDashQueue dashQueueService;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    private static void isChartPathExist(String dirPath) throws Exception {
        File file = new File(dirPath);
        if (!file.exists()) {
            if(!file.mkdirs()){
                throw new Exception("mkdir transcodeVideo fail");
            }
        }
    }


    @Scheduled(cron = "0/10 * * * * ?")
    private void transcodeVideoQueue(){

        String path = configParam.getTranscodePath();
        List<TranscodeQueue> transcodeQueues = transcodeQueueService.getVideosUnTranscode();
        log.info("transcodeQueues:" + transcodeQueues);
        if(transcodeQueues != null){
            try {
                for(TranscodeQueue transcodeQueue : transcodeQueues){
                    int videoId = transcodeQueue.getVideoId();
                    Video video = videoService.getVideoMessage(videoId);
                    video.setState("transcoding");
                    Video updatedVideo = videoService.updateVideo(video);
                    File file = new File(updatedVideo.getUrl());
                    isChartPathExist(path + videoId);
                    CompletableFuture<CmdResult> result = fFmpegTool.transcodeVideoDefault(file, path + videoId);
                    CmdResult cmdResult = result.get();
                    JSONObject jsonObject;
                    if (cmdResult.getExitValue() == 1) {

                        video.setState("fail");
                        videoService.updateVideo(video);
                        transcodeQueue.setState(2);
                        transcodeQueueService.updateTranscode(transcodeQueue);
                        jsonObject = new JSONObject();
                        jsonObject.put("code", 1);
                        jsonObject.put("videoId", videoId);
                        jsonObject.put("userId", video.getUserId());
                        jsonObject.put("title", video.getTitle());
                        jsonObject.put("state", "fail");
                        jsonObject.put("message", "cmdResult.getExitValue() == 1");
                        rabbitMqSender.send(jsonObject.toString());
                    } else {
                        video.setState("transcoded");
                        video.setTranscodeUrl(path + videoId + "/" + file.getName() + ".mp4");
                        videoService.updateVideo(video);
                        transcodeQueue.setState(0);
                        transcodeQueueService.updateTranscode(transcodeQueue);
                        dashQueueService.addVideoToDashQueue();
                        jsonObject = new JSONObject();
                        jsonObject.put("code", 0);
                        jsonObject.put("videoId", videoId);
                        jsonObject.put("userId", video.getUserId());
                        jsonObject.put("title", video.getTitle());
                        jsonObject.put("state", "transcoded");
                        rabbitMqSender.send(jsonObject.toString());
                    }
                }
            } catch (Exception e) {
                log.error("transcodeQueues schedule error" + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    @Scheduled(cron = "0/10 * * * * ?")
    private void dashQueue(){
        String path = configParam.getDashPath();
        List<DashQueue> dashQueues = dashQueueService.getUnDashVideoFromQueue();
        log.info("dashQueues:" + dashQueues);
        if(dashQueues != null){
            try {
                for(DashQueue dashQueue : dashQueues){
                    JSONObject jsonObject;
                    int videoId = dashQueue.getVideoId();
                    Video video = videoService.getVideoMessage(videoId);
                    video.setState("dashing");
                    jsonObject = new JSONObject();
                    jsonObject.put("code", 0);
                    jsonObject.put("videoId", videoId);
                    jsonObject.put("userId", video.getUserId());
                    jsonObject.put("title", video.getTitle());
                    jsonObject.put("state", "dashing");
                    rabbitMqSender.send(jsonObject.toString());
                    Video updatedVideo = videoService.updateVideo(video);
                    File file = new File(updatedVideo.getTranscodeUrl());
                    isChartPathExist(path + videoId);
                    CompletableFuture<CmdResult> result = fFmpegTool.transcodeDashVideo(file, path + videoId);
                    CmdResult cmdResult = result.get();

                    if (cmdResult.getExitValue() == 1) {
                        video.setState("dashFail");
                        videoService.updateVideo(video);
                        dashQueue.setState(2);
                        dashQueueService.updateDashQueue(dashQueue);
                        jsonObject = new JSONObject();
                        jsonObject.put("code", 1);
                        jsonObject.put("videoId", videoId);
                        jsonObject.put("userId", video.getUserId());
                        jsonObject.put("title", video.getTitle());
                        jsonObject.put("state", "fail");
                        jsonObject.put("message", "cmdResult.getExitValue() == 1");
                        rabbitMqSender.send(jsonObject.toString());
                        videoService.updateVideo(updatedVideo);

                    } else {
                        video.setState("success");
                        videoService.updateVideo(video);
                        dashQueue.setState(0);
                        dashQueueService.updateDashQueue(dashQueue);
                        jsonObject = new JSONObject();
                        jsonObject.put("code", 0);
                        jsonObject.put("videoId", videoId);
                        jsonObject.put("userId", video.getUserId());
                        jsonObject.put("title", video.getTitle());
                        jsonObject.put("state", "success");
                        rabbitMqSender.send(jsonObject.toString());
                    }
                }
            } catch (Exception e) {
                log.error("transcodeQueues schedule error" + e.getMessage());
                e.printStackTrace();
            }
        }
    }


}
