package com.forum.video.ffmpegUtil;

/**
 * @author genghaoran
 */
public class CmdResult {

    private int exitValue;

    private String outputMessage;

    private String errorMessage;

    private String outputPath;

    public CmdResult(){

    }

    public CmdResult(int exitValue, String errorMessage, String outputMessage, String outputPath){
        this.exitValue = exitValue;
        this.errorMessage = errorMessage;
        this.outputMessage = outputMessage;
        this.outputPath = outputPath;
    }


    public void setExitValue(int exitValue) {
        this.exitValue = exitValue;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }

    public int getExitValue() {
        return exitValue;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
