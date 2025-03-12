package org.icbt.hasitha.megacity.dto;

public class ResultDTO<T> {


    private String message;
    private String code;
    private  T data;

    public ResultDTO(String message, String code, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public ResultDTO(String message, String code) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
