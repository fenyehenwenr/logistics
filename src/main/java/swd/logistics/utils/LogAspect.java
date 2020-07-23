package swd.logistics.utils;


import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class LogAspect {

    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * swd.logistics.service..*.*(..))")
    public void  Aspect(){

    }

    //@Around("Aspect()")
    public void logHandler(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();//开始调用的时间
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();//调用的方法名
        String className = method.getDeclaringClass().getName();//调用的类名
        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder();
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();//获取http请求
        HttpServletRequest request = attributes.getRequest();
        for (int i = 0; i <args.length ; i++) {
            params.append(args[i].toString());
            params.append(";");
        }
        Object result = null;
        try {
            result = joinPoint.proceed();
        }catch (Throwable throwable){
            String exception = throwable.getClass()+";"+throwable.getMessage();
            long costTime = System.currentTimeMillis();
            logger.error("请求时间：{}，请求耗时：{}，请求类名：{}，请求方法：{}，请求参数:{}，请求结果：{}",startTime,costTime,className,methodName,params.toString(),exception);
        }
        long costTime=System.currentTimeMillis()-startTime;
        logger.info("请求时间：{}，请求耗时：{}，请求类名：{}，请求方法：{}，请求参数:{}，请求结果：{}",startTime,costTime,className,methodName,params.toString(), result.toString());

    }

    @Before("Aspect()")
    public void beforeHandler(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();//获取http请求
        HttpServletRequest request = attributes.getRequest();
        logger.info("====================请求开始==================");
        logger.info("请求接口："+request.getRequestURI().toString());
        logger.info("请求方式："+request.getMethod());
        logger.info("请求方法："+joinPoint.getSignature());
        logger.info("请求方法参数："+ Arrays.toString(joinPoint.getArgs()));
        logger.info("====================请求结束==================");
        logger.info("");
    }

    @AfterReturning(pointcut = "Aspect()",returning = "o")
    public void afterHandler(Object o){
        logger.info("====================返回开始==================");
        logger.info("返回内容："+ JSONObject.toJSONString(o));
        logger.info("====================返回结束==================");
        logger.info("");
    }
}
