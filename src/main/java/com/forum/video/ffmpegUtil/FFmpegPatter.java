package com.forum.video.ffmpegUtil;

/**
 * @author genghaoran
 */

public enum FFmpegPatter {

    /**
     * ffmpeg转码模版
     */
    VIDEO_30FPS_2000BIT_1080P_H264("1920x1080", "2000k", 30, "libx264"),;

    private String scale;

    private String bitrate;

    private int fps;

    private String type;

    FFmpegPatter(String scale, String bitrate, int fps, String type){
        this.scale = scale;
        this.bitrate = bitrate;
        this.fps = fps;
        this.type = type;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }


    public void setScale(String scale) {
        this.scale = scale;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFps() {
        return fps;
    }

    public String getBitrate() {
        return bitrate;
    }

    public String getScale() {
        return scale;
    }

    public String getType() {
        return type;
    }
}
