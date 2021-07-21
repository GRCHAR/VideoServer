package com.forum.video.result;

/**
 * @author genghaoran
 */
public enum ResultCodeMessage {
    //成功
    SUCCESS(200, "successful"),
    // 重定向
    REDIRECT(301, "redirect"),
    // 资源未找到
    NOT_FOUND(404, "not found"),
    // 服务器错误
    SERVER_ERROR(500,"server error"),

    ALREADY_HAVE_VIDEO(500, "视频标题重复");



    private int code;
    private String message;

    ResultCodeMessage(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
