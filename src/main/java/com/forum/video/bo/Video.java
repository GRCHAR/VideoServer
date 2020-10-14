package com.forum.video.bo;

import org.elasticsearch.common.recycler.Recycler;

/**
 * @author genghaoran
 */
public class Video {
    private int id;
    private String title;
    private int userId;
    private String url;

    public Video(int id, String title, int userId, String url) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.url = url;
    }

    public Video(String title, int userId){
        this.title = title;
        this.userId = userId;
        this.url = null;
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

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", userId=" + userId +
                ", url='" + url + '\'' +
                '}';
    }
}
