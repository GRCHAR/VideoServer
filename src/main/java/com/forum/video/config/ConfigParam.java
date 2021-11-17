package com.forum.video.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author genghaoran
 */
@ConfigurationProperties(prefix = "video")
@Component
public class ConfigParam {
    private String path;

    private String transcodePath;

    private String dashPath;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTranscodePath() {
        return transcodePath;
    }

    public void setTranscodePath(String transcodePath) {
        this.transcodePath = transcodePath;
    }

    public void setDashPath(String dashPath) {
        this.dashPath = dashPath;
    }

    public String getDashPath() {
        return dashPath;
    }
}
