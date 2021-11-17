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
@Entity(name = "TranscodeQueue")
@TableName("transcode_queue")
public class TranscodeQueue {

    @TableId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int videoId;

    private int state;

    private Timestamp timestamp;

    public TranscodeQueue(){

    }

    public TranscodeQueue(int id, int videoId, Timestamp timestamp, int state){
        this.id = id;
        this.videoId = videoId;
        this.timestamp = timestamp;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public int getVideoId() {
        return videoId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getState() {
        return state;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public void setState(int state) {
        this.state = state;
    }
}
