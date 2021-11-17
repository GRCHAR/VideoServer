package com.forum.video.bo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * @author genghaoran
 */
@TableName("dash_queue")
@Entity
public class DashQueue {

    @TableId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int videoId;

    private int state;

    private Timestamp timestamp;

    public DashQueue(){

    }

    public DashQueue(int id, int videoId, Timestamp timestamp, int state){
        this.id = id;
        this.videoId = videoId;
        this.state = state;
        this.timestamp = timestamp;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getState() {
        return state;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getVideoId() {
        return videoId;
    }

    public int getId() {
        return id;
    }
}

