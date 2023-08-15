//package com.yl.auth.config;
//
//import cn.hutool.json.JSONUtil;
//import com.yl.common.config.Result;
//import com.yl.common.config.ResultCode;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//@ControllerAdvice
//public class ResponseHandler implements ResponseBodyAdvice {
//
//    @Override
//    public boolean supports(MethodParameter returnType, Class converterType) {
//        return true;
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        if (body instanceof Result) {
//            return body;
//        }
//        if (body instanceof String) {
//            return JSONUtil.toJsonStr(Result.success(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), body));
//        }
//        return Result.success(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), body);
//    }
//
//}
