package com.forum.video.service;

import com.forum.video.bo.Video;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiInternalFrameUI;
import java.io.IOException;

/**
 * @author genghaoran
 */
public interface IVideoService {

    public int updateVideo(String title, int userId, MultipartFile multipartFile) throws IOException;

    public Video getVideoMessage(int id);

    public int deleteVideo(int id);


}
