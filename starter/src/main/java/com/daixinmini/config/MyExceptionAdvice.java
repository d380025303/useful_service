package com.daixinmini.config;

import com.daixinmini.base.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p>Project: his-require-starter</p>
 * <p>Description: 全局的异常处理.</p>
 * <p>Copyright (c) 2017 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:linguo@karrytech.com">Your Name</a>
 */
@ControllerAdvice
public class MyExceptionAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(BizException e) {
        logger.error("e: ", e);
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String bizExceptionHandler(Exception e) {
        logger.error("e: ", e);
        return "出错啦~";
    }

}
