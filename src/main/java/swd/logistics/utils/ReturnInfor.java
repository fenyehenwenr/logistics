package swd.logistics.utils;


import swd.logistics.utils.EnumUtils.ReturnCode;

public class ReturnInfor<T> {


    private int code;

    private String message;

    private T Data;

    public ReturnInfor(T data){
        this(ReturnCode.SUCCESS,data);
    }

    public ReturnInfor(ReturnCode returnCode, T data) {
        this.code = returnCode.getCode();
        this.message = returnCode.getMessage();
        Data = data;
    }

    public ReturnInfor(int code, String message, T data) {
        this.code = code;
        this.message = message;
        Data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return Data;
    }

    @Override
    public String toString() {
        return "ReturnInfor{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", Data=" + Data +
                '}';
    }
}
