package swd.logistics.utils;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import swd.logistics.utils.EnumUtils.ReturnCode;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NullPointerException.class)
    public ReturnInfor nullPoint(NullPointerException e){
        System.out.println("空指针啦:"+e.getMessage());
        return new ReturnInfor<String>(ReturnCode.ERROR,"数据异常");
    }

    @ExceptionHandler(ApiException.class)
    public ReturnInfor<String> apiError(ApiException e){
        System.out.println(e.getMessage());
       return new ReturnInfor<String>(e.getCode(),e.getMessage(),null);
    }

    @ExceptionHandler(Exception.class)
    public ReturnInfor<String> Exception(Exception e){
        return new ReturnInfor<String>(ReturnCode.ERROR,e.getMessage());
    }
}
