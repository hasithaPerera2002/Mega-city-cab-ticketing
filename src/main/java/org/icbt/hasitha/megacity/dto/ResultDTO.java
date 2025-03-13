package org.icbt.hasitha.megacity.dto;

public class ResultDTO<T> {


    private String message;
    private int code;
    private  T data;

    public ResultDTO(String message, int code, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public ResultDTO(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public ResultDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
