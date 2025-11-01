package com.my.myaicodemother.core;

import com.my.myaicodemother.mode.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;
    @Test
    void generateCode() {
//        File file = aiCodeGeneratorFacade.generateCode("生成一个HTML代码", CodeGenTypeEnum.HTML);

        File file = aiCodeGeneratorFacade.generateCode("生成一个404页面 不超过50行代码", CodeGenTypeEnum.MULTI_FILE);
        assertNotNull(file);
    }

    @Test
    void generateCodeStream() {
        Flux<String> flux = aiCodeGeneratorFacade.generateCodeStream("", CodeGenTypeEnum.HTML);
        // 阻塞等待所有数据收集完毕
        List<String> result = flux.collectList().block();
        Assertions.assertNotNull(result);
        String join = String.join("", result);
        Assertions.assertNotNull( join);
    }
}