//package com.yl.auth.config;
//
//import cn.hutool.json.JSONUtil;
//import com.yl.common.annotation.Limit;
//import com.yl.common.config.BusinessException;
//import com.yl.common.config.LimitType;
//import com.yl.common.config.ResultCode;
//import com.yl.common.util.RedisLimitUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Slf4j
//@Aspect
//@Component
//public class LimitAspect {
//
//    /**
//     * 前置通知，判断是否超出限流次数
//     *
//     * @param point
//     */
//    @Before("@annotation(limit)")
//    public void doBefore(JoinPoint point, Limit limit) {
//        try {
//            // 拼接key
//            String key = getCombineKey(limit, point);
//            // 判断是否超出限流次数
//            if (!RedisLimitUtil.limit(key, limit.count(), limit.time())) {
//                throw new BusinessException(ResultCode.ERROR.getCode(), "访问过于频繁，请稍候再试");
//            }
//        } catch (BusinessException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new BusinessException(ResultCode.ERROR.getCode(), "接口限流异常，请稍候再试");
//        }
//    }
//
//
//    /**
//     * 根据限流类型拼接key
//     */
//    public String getCombineKey(Limit limit, JoinPoint joinPoint) {
//        StringBuilder sb = new StringBuilder(limit.prefix());
//
//        //记录开始时间
//        long beginTime = System.currentTimeMillis();
//        // 从请求头中获取所需要的信息
//        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        String loginId = request.getHeader("loginId");
//        // 获取请求地址
//        Object requestPath = request.getRequestURI();
//        //格式换开始时间
//        String optTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        //参数
//        Object paramObj = joinPoint.getArgs();
//        String param = null;
//        if (request.getMethod().equals("POST")) {
//            param = JSONUtil.toJsonStr(paramObj);
//        }
//        //返回结果
////        Object resultObj = joinPoint.proceed();
////        String result = JSONUtil.toJsonStr(resultObj);
//        //获取切点方法对象
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        //获取类名
//        String className = joinPoint.getTarget().getClass().getName();
//        //获取方法名
//        String methodName = method.getName();
//
//        // 按照IP限流
//        if (limit.type() == LimitType.IP) {
//            sb.append(getIpAddr(request)).append("-");
//        }
//        sb.append(className).append("-").append(method.getName());
//        return sb.toString();
//    }
//
//    public String getIpAddr(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
//    }
//
//}
