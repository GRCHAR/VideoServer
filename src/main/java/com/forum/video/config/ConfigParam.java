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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
