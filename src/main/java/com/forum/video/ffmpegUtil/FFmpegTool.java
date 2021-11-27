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
    ProcessReadStream processReadStream;

    public CmdResult executeCmd(String cmd) throws IOException, InterruptedException {
        log.info("start cmd:" + cmd);
        CmdResult cmdResult = new CmdResult();
        Process process = Runtime.getRuntime().exec(cmd);
        log.info("process start!");
        processReadStream.readErrorStream(process);
        processReadStream.readInputStream(process);
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

    public CmdResult transcodeVideoFrames(File file, String outputDir) throws IOException, InterruptedException {
        String cmd = "ffmpeg -i " + file.getAbsolutePath() + " -vf fps=1/60 -q:v 1 "+ outputDir +"/image_%d.png -y";
        CmdResult cmdResult = executeCmd(cmd);
        cmdResult.setOutputPath(outputDir);
        return cmdResult;
    }

    @Async("transcodeVideoExecutor")
    public CompletableFuture<CmdResult> transcodeVideoDefault(File file, String outputDir) throws IOException, InterruptedException {
        FFmpegParameter ffmpegParameter = new FFmpegParameter(FFmpegPatter.VIDEO_30FPS_2000BIT_1080P_H264);
        CmdResult cmdTranscodeResult = transcodeVideo(file, outputDir, ffmpegParameter);
        if(cmdTranscodeResult.getExitValue() == 0){
            CmdResult imgTranscodeResult = transcodeVideoFrames(file, outputDir);
            return CompletableFuture.completedFuture(imgTranscodeResult);
        }
        CmdResult result = new CmdResult();
        result.setExitValue(1);
        return CompletableFuture.completedFuture(result);
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






}
