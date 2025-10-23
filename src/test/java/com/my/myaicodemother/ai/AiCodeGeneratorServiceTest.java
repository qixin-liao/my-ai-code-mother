package com.my.myaicodemother.ai;

import com.my.myaicodemother.ai.model.HtmlCodeResult;
import com.my.myaicodemother.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
         HtmlCodeResult res = aiCodeGeneratorService.generateHtmlCode("生成一个HTML代码");
        Assertions.assertNotNull(res);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult res = aiCodeGeneratorService.generateMultiFileCode("做一个时钟，不超过50行");
        Assertions.assertNotNull(res);
    }
}