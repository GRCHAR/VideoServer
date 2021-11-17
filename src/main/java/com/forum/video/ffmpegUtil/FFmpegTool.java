package com.forum.video.ffmpegUtil;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author genghaoran
 */
@Component
@Slf4j
public class FFmpegTool {

    @Autowired
    ThreadPoolTaskExecutor clearStream;

    public CmdResult executeCmd(String cmd) throws IOException, InterruptedException {
        log.info("start cmd:" + cmd);
        CmdResult cmdResult = new CmdResult();
        Process process = Runtime.getRuntime().exec(cmd);
        log.info("process start!");
        new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while ((line = in.readLine()) != null) {
//                        log.info("output: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        // 处理ErrorStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = null;
                try {
                    while ((line = err.readLine()) != null) {
//                        log.info("err: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        err.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        process.waitFor();
        if(process.exitValue() == 0){
            cmdResult.setExitValue(0);
            byte[] outputByte = new byte[0];
            process.getOutputStream().write(outputByte);
            String outputString = new String(outputByte, StandardCharsets.UTF_8);
            log.error(outputString);
            cmdResult.setOutputMessage(outputString);
        } else {
            cmdResult.setExitValue(1);
            byte[] errorByte = new byte[0];
            process.getOutputStream().write(errorByte);
            String errorString = new String(errorByte, StandardCharsets.UTF_8);
            cmdResult.setErrorMessage(errorString);
        }
        return cmdResult;
    }


    public CmdResult transcodeVideo(File file, String outputDir, FFmpegParameter fFmpegParameter) throws IOException, InterruptedException {
        String cmd = "ffmpeg -i " + file.getAbsolutePath() + " -c:v "+fFmpegParameter.getType()+" -r " +
                fFmpegParameter.getFps() + " -b:v " + fFmpegParameter.getBitrate() + " -s " + fFmpegParameter.getScale() + " " + outputDir + "/" + file.getName() + ".mp4" + " -y";
        CmdResult cmdResult = executeCmd(cmd);
        cmdResult.setOutputPath(outputDir + "/" + file.getName() + ".mp4");
        return cmdResult;
    }

    @Async("transcodeVideoExecutor")
    public CompletableFuture<CmdResult> transcodeVideoDefault(File file, String outputDir) throws IOException, InterruptedException {
        FFmpegParameter ffmpegParameter = new FFmpegParameter(FFmpegPatter.VIDEO_30FPS_2000BIT_1080P_H264);
        return CompletableFuture.completedFuture(transcodeVideo(file, outputDir, ffmpegParameter));
    }

    @Async("transcodeDashVideo")
    public CompletableFuture<CmdResult> transcodeDashVideo(File file, String outputDir){
        String cmd = "ffmpeg -i " + file.getAbsolutePath() + " -c copy -f dash " + outputDir  + "/" + file.getName().split("\\.")[0] + ".mpd" + " -y";
        CmdResult cmdResult = new CmdResult();
        try {
            cmdResult = executeCmd(cmd);
        } catch (IOException | InterruptedException e) {
            log.error("executeCmd " + cmd + " error: " + e.getMessage());
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(cmdResult);
    }

    void clearStream(Process process){
        log.info("clear start!");
        byte[] outputByte = new byte[1];
        int readNumber = 0;
        try {
            readNumber = process.getInputStream().read(outputByte);
            log.info("first read:" + readNumber);

            while(readNumber != -1){
                log.info("readNumber:" + readNumber);
                readNumber = process.getInputStream().read(outputByte);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
