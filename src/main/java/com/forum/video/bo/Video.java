package com.forum.video.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.elasticsearch.common.recycler.Recycler;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author genghaoran
 */

@Entity(name = "video")
public class Video {
    @TableId(type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private int userId;
    private String url;
    private String transcodeUrl;
    private String dashUrl;
    private String state;

    public Video(int id, String title, int userId, String url, String state) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.url = url;
    }

    public Video(){

    }

    public Video(String title, int userId){
        this.title = title;
        this.userId = userId;
        this.url = null;
        this.state = "update";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDashUrl() {
        return dashUrl;
    }

    public void setDashUrl(String dashUrl) {
        this.dashUrl = dashUrl;
    }

    public void setTranscodeUrl(String transcodeUrl) {
        this.transcodeUrl = transcodeUrl;
    }

    public String getTranscodeUrl() {
        return transcodeUrl;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", userId=" + userId +
                ", url='" + url + '\'' +
                ", transcodeUrl='" + transcodeUrl + '\'' +
                ", dashUrl='" + dashUrl + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
