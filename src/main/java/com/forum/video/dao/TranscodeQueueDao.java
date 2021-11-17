package com.forum.video.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forum.video.bo.TranscodeQueue;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author genghaoran
 */
@Mapper
public interface TranscodeQueueDao extends BaseMapper<TranscodeQueue> {

}
