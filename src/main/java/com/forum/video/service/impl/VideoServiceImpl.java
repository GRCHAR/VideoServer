package com.forum.video.service.impl;

import com.forum.video.bo.Video;
import com.forum.video.dao.VideoDao;
import com.forum.video.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author genghaoran
 */
@Service
public class VideoServiceImpl implements IVideoService {

    @Autowired
    private VideoDao videoDao;



    @Override
    public int updateVideo(String title, int userId, MultipartFile multipartFile) throws IOException {
        Video video = new Video(title, userId);
        int videoId = videoDao.createVideo(video);
        File file = new File("/VideoStorage/" + videoId,title);
        multipartFile.transferTo(file);
        return videoId;
    }

    @Override
    public Video getVideoMessage(int id) {
        return videoDao.getVideo(id);
    }

    @Override
    public int deleteVideo(int id){
        Video video = videoDao.getVideo(id);
        File file = new File("/VideoStorage/" + video.getId(), video.getTitle());
        videoDao.deleteVideo(id);
        boolean result = file.delete();
        if(result) {
            return 0;
        }
        return 1;
    }


}
