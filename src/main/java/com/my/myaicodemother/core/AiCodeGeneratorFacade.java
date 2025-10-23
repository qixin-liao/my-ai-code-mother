package com.my.myaicodemother.core;

import com.my.myaicodemother.ai.AiCodeGeneratorService;
import com.my.myaicodemother.exception.BusinessException;
import com.my.myaicodemother.exception.ErrorCode;
import com.my.myaicodemother.mode.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    public File generateCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成类型为空");
        }
//        return switch (codeGenTypeEnum) {
//            case HTML -> aiCodeGeneratorService.generateHtmlCode(userMessage);
//            case MULTI_FILE -> aiCodeGeneratorService.generateMultiFileCode(userMessage);
//            default -> {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成类型错误");
//            }
//        };
        return null;
    }
}
