package com.github.hackyoma.frameexample.backend.aspect;

import com.github.hackyoma.frameexample.backend.util.GetIp;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一日志切面
 *
 * @author hackyo
 * @version V1.0.0
 */
@Aspect
@Component
public class HttpAspect {

    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    /**
     * 定义日志切面规则
     */
    @Pointcut("execution(public * com.github.hackyoma.frameexample.backend.*.controller.*.*(..))")
    public void log() {
    }

    /**
     * 输出访问日志
     *
     * @param joinPoint 操作信息
     */
    @Before("log()")
    public void doBeforeLog(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            logger.info("url={}, method={}, ip={}, class_method={}, args={}", request.getRequestURI(), request.getMethod(), GetIp.getUserIp(request), joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), joinPoint.getArgs());
        }
    }

    /**
     * 输出返回结果日志
     *
     * @param object 操作信息
     */
    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterLog(Object object) {
        logger.info("response={}", object);
    }

}
