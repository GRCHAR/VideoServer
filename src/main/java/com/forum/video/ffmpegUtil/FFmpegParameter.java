package com.forum.video.ffmpegUtil;

/**
 * @author genghaoran
 */
public class FFmpegParameter {

    private String bitrate;

    private int fps;

    private String type;

    private String scale;

    public FFmpegParameter(){

    }

    public FFmpegParameter(String bitrate, int fps, String type, String scale){
        this.bitrate = bitrate;
        this.fps = fps;
        this.type = type;
        this.scale = scale;
    }

    public FFmpegParameter(FFmpegPatter fFmpegPatter){
        this.fps = fFmpegPatter.getFps();
        this.bitrate = fFmpegPatter.getBitrate();
        this.type = fFmpegPatter.getType();
        this.scale = fFmpegPatter.getScale();
    }

    public int getFps() {
        return fps;
    }

    public String getBitrate() {
        return bitrate;
    }

    public String getType() {
        return type;
    }

    public String getScale() {
        return scale;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
