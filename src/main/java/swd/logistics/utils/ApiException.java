package swd.logistics.utils;


import swd.logistics.utils.EnumUtils.ReturnCode;

public class ApiException extends RuntimeException {

    private int code;

    private String message;

    public ApiException(){
        this(4001,"接口错误");
    }

    public ApiException(String message){
        this(4001,message);
    }
    public ApiException(ReturnCode returnCode, String message){
        this(returnCode.getCode(),message);
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
