package com.forum.video.service.impl;

import com.forum.video.bo.Video;
import com.forum.video.dao.VideoDao;
import com.forum.video.service.IVideoService;
import org.elasticsearch.common.recycler.Recycler;
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
        File file = new File("/VideoStorage",title);
        multipartFile.transferTo(file);
        Video video = new Video(title, userId);
        return videoDao.createVideo(video);
    }

    @Override
    public Video getVideoMessage(int id) {
        return videoDao.getVideo(id);
    }


}
