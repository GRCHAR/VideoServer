package com.forum.video.ffmpegUtil;

/**
 * @author genghaoran
 */
public class FFmpegParameter {

    private long bitrate;

    private int fps;

    private String type;

    public FFmpegParameter(){

    }

    public FFmpegParameter(long bitrate, int fps, String type){

    }

    public int getFps() {
        return fps;
    }

    public long getBitrate() {
        return bitrate;
    }

    public String getType() {
        return type;
    }

    public void setBitrate(long bitrate) {
        this.bitrate = bitrate;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setType(String type) {
        this.type = type;
    }
}
