package com.example.spring.boot.demo.aspect;

import com.alibaba.fastjson.JSONObject;
import com.example.spring.boot.demo.helper.HttpHelper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;


/**
 * @author: 胡文良
 * @ClassName RequestAspect.java
 * @description:
 * @create: 2021-05-28 14:20
 **/
@Aspect
@Component
public class RequestAspect {

    private static Logger logger = LoggerFactory.getLogger(RequestAspect.class);


    @Pointcut("execution(public * com.example.spring.boot.demo.controller..*.*(..))")//切入点描述 这个是controller包的切入点
    public void controllerLog(){}//签名，可以理解成这个切入点的一个名称

    @Pointcut(value = "execution(public * com.example.spring.boot.demo.controller..*.*(..))")//切入点描述，这个是uiController包的切入点
    public void uiControllerLog(){}

    @Before("controllerLog() || uiControllerLog()") //在切入点的方法run之前要干的
    public void logBeforeController(JoinPoint joinPoint) {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();//这个RequestContextHolder是Springmvc提供来获得请求的东西
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 记录下请求内容
        logger.info("################URL : " + request.getRequestURL().toString());
        logger.info("################HTTP_METHOD : " + request.getMethod());
        logger.info("################IP : " + request.getRemoteAddr());
        logger.info("################THE ARGS OF THE CONTROLLER : " + Arrays.toString(joinPoint.getArgs()));

        JSONObject params = new JSONObject();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        sb.append("Started ").append(request.getMethod()).append(" \"").append(String.format("%s%s", request.getRequestURI(), StringUtils.isNotEmpty(request.getQueryString()) ? String.format("?%s", request.getQueryString()) : "")).append("\" for ").append(request.getRemoteHost()).append(" at ").append(sdf.format(new Date()));
        if (!CollectionUtils.isEmpty(params)) {
            sb.append("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\tParameters: ").append(params.toJSONString());
        }
        logger.info(sb.toString());
        logger.info("body :" + HttpHelper.getBodyString(request).replaceAll(" ", ""));

        //下面这个getSignature().getDeclaringTypeName()是获取包+类名的   然后后面的joinPoint.getSignature.getName()获取了方法名
        logger.info("################CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //logger.info("################TARGET: " + joinPoint.getTarget());//返回的是需要加强的目标类的对象
        //logger.info("################THIS: " + joinPoint.getThis());//返回的是经过加强后的代理类的对象

    }

    /**
     * @AfterReturning 后置通知
     * 属性1. value切入点位置
     *     2. returning 自定义的变量，标识目标方法的返回值
     *     自定义变量名必须和通知方法的形参一样
     *     在方法定义的上面
     * 特点：在目标方法之后执行的
     *      能够获取到目标方法的返回值，可以根据这个返回值做不同的处理功能
     */
    @AfterReturning(returning = "ret", pointcut = "controllerLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("response : " + ret);
    }
}
