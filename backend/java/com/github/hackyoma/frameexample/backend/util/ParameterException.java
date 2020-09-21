package com.github.hackyoma.frameexample.backend.util;

import org.springframework.validation.BindingResult;

/**
 * 自定义参数异常
 *
 * @author hackyo
 * @date 2018/8/22
 */
public class ParameterException extends RuntimeException {

    private final BindingResult bindingResult;

    public ParameterException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

}
