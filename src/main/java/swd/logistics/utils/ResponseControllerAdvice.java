package swd.logistics.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import swd.logistics.utils.EnumUtils.ReturnCode;

@ControllerAdvice(basePackages = {"swd.logistics.controller"})//返回消息前会走一遍这个注解
public class ResponseControllerAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {//如果这里判断为true，也就是Controller层没用ReturnInfo封装
        return !methodParameter.getGenericParameterType().equals(ReturnInfor.class);
    }

    //当supports方法返回true，才会走下面这个方法，将返回对象包装一下
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        if (Empty.isEmpty(o)){
            return new ReturnInfor<>(ReturnCode.NOT_DATA,null);
        }
        if (methodParameter.getGenericParameterType().equals(String.class)){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(new ReturnInfor<>(o));
            } catch (JsonProcessingException e) {
                throw new ApiException("返回类型错误");
            }
        }
        return new ReturnInfor<>(o);
    }
}
