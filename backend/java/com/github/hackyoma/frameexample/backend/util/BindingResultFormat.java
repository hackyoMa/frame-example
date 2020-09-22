package com.github.hackyoma.frameexample.backend.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 格式化BindingResult
 *
 * @author hackyo
 * @date 2018/8/22
 */
public final class BindingResultFormat {

    /**
     * 格式化参数校验结果
     *
     * @param bindingResult bindingResult
     * @return 参数校验结果
     */
    public static Map<String, String> format(BindingResult bindingResult) {
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        Map<String, String> fieldErrorMap = new HashMap<>(fieldErrorList.size());
        for (FieldError fieldError : fieldErrorList) {
            fieldErrorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return fieldErrorMap;
    }

}
