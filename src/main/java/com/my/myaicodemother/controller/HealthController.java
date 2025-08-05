package com.my.myaicodemother.controller;

import com.my.myaicodemother.common.BaseResponse;
import com.my.myaicodemother.common.ResultUtils;
import com.my.myaicodemother.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "健康检查")
public class HealthController {

    @GetMapping("/")
    @Operation(summary = "健康检查", description = "健康检查")
    public BaseResponse<String> healthCheck() {

        return ResultUtils.success(null);
    }
}
