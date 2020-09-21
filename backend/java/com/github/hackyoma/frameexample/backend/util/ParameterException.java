package com.github.hackyoma.frameexample.backend.util;

import org.springframework.validation.BindingResult;

/**
 * 自定义参数异常
 *
 * @author hackyo
 * @version V1.0.0
 */
public class ParameterException extends RuntimeException {

    private BindingResult bindingResult;

    public ParameterException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

}
