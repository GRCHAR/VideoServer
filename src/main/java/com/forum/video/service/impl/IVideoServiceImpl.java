package com.forum.video.service.impl;

import com.forum.video.result.Result;
import com.forum.video.result.ResultCodeMessage;
import com.forum.video.service.IVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 */
public class IVideoServiceImpl implements IVideoService {

    private final String path = "I:\\storage\\";
    private final Logger logger = LoggerFactory.getLogger(IVideoServiceImpl.class);

    @Override
    public int updateVideo(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            logger.error("file is empty");
            return 0;
        }
        String filename = multipartFile.getOriginalFilename();
        if(filename == null){
            return 0;
        }
        File file = new File(path, filename);
        multipartFile.transferTo(file);
        return 1;
    }
}
