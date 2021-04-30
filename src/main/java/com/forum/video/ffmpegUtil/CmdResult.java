package com.forum.video.ffmpegUtil;

public class CmdResult {

    private int exitValue;

    private String outputMessage;

    private String errorMessage;

    public CmdResult(){

    }

    public CmdResult(int exitValue, String errorMessage, String outputMessage){
        this.exitValue = exitValue;
        this.errorMessage = errorMessage;
        this.outputMessage = outputMessage;
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
}
