package com.forum.video.service;

import com.forum.video.bo.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author genghaoran
 */
public interface IVideoService {

    public Video updateVideo(String title, int userId, MultipartFile multipartFile) throws IOException, InterruptedException;

    public Video getVideoMessage(int id);

    public int deleteVideo(int id);

    public boolean hasVideo(String title, int userId);

    public void testQueue();


}
