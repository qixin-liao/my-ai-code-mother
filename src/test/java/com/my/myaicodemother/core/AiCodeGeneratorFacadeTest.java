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

        File file = aiCodeGeneratorFacade.generateCode("生成一个404页面 不超过50行代码", CodeGenTypeEnum.MULTI_FILE,1l);
        assertNotNull(file);
    }

    @Test
    void generateCodeStream() {
        Flux<String> flux = aiCodeGeneratorFacade.generateCodeStream("第二部分：目录\n" +
                "\n" +
                "序幕：四环的诞生与起源 (1899-1932)\n" +
                "\n" +
                "重生：奥迪品牌的复兴 (1965-1980)\n" +
                "\n" +
                "传奇：quattro与技术的辉煌 (1980-1990)\n" +
                "\n" +
                "新时代：全球豪华品牌的确立 (1990-2010)\n" +
                "\n" +
                "新纪元：电动化与未来出行 (2010-至今)\n" +
                "\n" +
                "总结：奥迪的精神与展望", CodeGenTypeEnum.HTML,1l);
        // 阻塞等待所有数据收集完毕
        List<String> result = flux.collectList().block();
        Assertions.assertNotNull(result);
        String join = String.join("", result);
        Assertions.assertNotNull( join);
    }
}