package swd.logistics.utils.EnumUtils;

public enum ReturnCode {

    SUCCESS(1000,"操作成功"),
    FAILED(1001,"操作失败"),
    PARAMETER_ERROR(1002,"参数校验错误"),
    NOT_DATA(1003,"数据为空"),
    API_ERROR(4001,"接口错误"),
    ERROR(5000,"未知错误")
    ;

    private int code;

   private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ReturnCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
