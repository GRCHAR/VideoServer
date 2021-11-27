package com.forum.video.ffmpegUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author genghaoran
 */
@Component
@Slf4j
public class ProcessReadStream {

    @Async("readInputStream")
    public void readInputStream(Process process){
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        try {
            while ((line = in.readLine()) != null) {
                log.info("line");
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

    @Async("readErrorStream")
    public void readErrorStream(Process process){
        BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = null;
        try {
            while ((line = err.readLine()) != null) {
                log.info(line);
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


}
