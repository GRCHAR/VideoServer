package com.forum.video.ffmpegUtil;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author genghaoran
 */
@Component
public class FFmpegTool {

    public CmdResult executeCmd(String cmd) throws IOException, InterruptedException {
        CmdResult cmdResult = new CmdResult();
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        if(process.exitValue() == 0){
            cmdResult.setExitValue(0);
            byte[] outputByte = new byte[0];
            process.getOutputStream().write(outputByte);
            String outputString = new String(outputByte, StandardCharsets.UTF_8);
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
        String cmd = "ffmpeg -i" + file.getAbsolutePath() + "-c:v "+fFmpegParameter.getType()+" -r " +
                fFmpegParameter.getFps() + " " + outputDir + "/" + file.getName();
        return executeCmd(cmd);
    }

    public CmdResult transcodeVideoDefault(File file, String outputDir) throws IOException, InterruptedException {
        FFmpegParameter ffmpegParameter = new FFmpegParameter(0, 25 ,"libx264");
        return transcodeVideo(file, outputDir, ffmpegParameter);
    }




}
