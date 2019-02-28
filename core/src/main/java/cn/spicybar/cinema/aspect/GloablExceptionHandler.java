package cn.spicybar.cinema.aspect;

import cn.spicybar.cinema.util.ParameterException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 全局异常处理
 *
 * @author hackyo
 * @version V1.0.0
 */
@ControllerAdvice
public class GloablExceptionHandler {

    /**
     * 处理参数异常
     *
     * @param e 异常信息
     * @return 异常说明
     */
    @ResponseBody
    @ExceptionHandler(ParameterException.class)
    public JSONObject parameterException(ParameterException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        JSONObject errorMap = new JSONObject();
        for (FieldError fieldError : fieldErrors) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        errorMap.put("status", "error");
        return errorMap;
    }

}
