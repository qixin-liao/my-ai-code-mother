package com.my.myaicodemother.exception;

import com.my.myaicodemother.common.BaseResponse;
import com.my.myaicodemother.common.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        /**
     * 业务异常处理器
     * 处理 BusinessException 类型的异常，记录错误日志并返回统一的错误响应
     *
     * @param e 业务异常对象，包含错误码和错误信息
     * @return BaseResponse<?> 统一的错误响应对象，包含错误码和错误信息
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        // 记录业务异常日志
        log.error("BusinessException", e);
        // 返回统一的错误响应
        return ResultUtils.error(e.getCode(), e.getMessage());
    }


        /**
     * 运行时异常处理器
     *
     * @param e 运行时异常对象
     * @return 包含系统错误信息的统一响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        // 记录运行时异常日志
        log.error("RuntimeException", e);
        // 返回系统错误响应
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }

}
