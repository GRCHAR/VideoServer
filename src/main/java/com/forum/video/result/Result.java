package com.forum.video.result;

import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author genghaoran
 */
@Service
public class Result<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public Result(int code, String message){
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(T data){
        this.code = 200;
        this.message = "成功";
        this.data = data;
    }

    public Result(){
        this.code = 200;
        this.message = "成功";
    }

    public Result(ResultCodeMessage resultCodeMessage){
        this.code = resultCodeMessage.getCode();
        this.message = resultCodeMessage.getMessage();
    }

    public Result(ResultCodeMessage resultCodeMessage, T data){
        this.code = resultCodeMessage.getCode();
        this.message = resultCodeMessage.getMessage();
        this.data = data;
    }

    public static<T> Result<T> success(){
        return new Result<T>();
    }

    public static<T> Result<T> success(T data){
        return new Result<T>(data);
    }


    public static<T> Result<T> failure(ResultCodeMessage resultCodeMessage){
        return new Result<T>(resultCodeMessage);
    }

    public static<T> Result<T> success(ResultCodeMessage resultCodeMessage, T data){
        return new Result<T>(resultCodeMessage, data);
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
