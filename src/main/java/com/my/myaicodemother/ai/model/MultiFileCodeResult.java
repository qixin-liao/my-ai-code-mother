package com.my.myaicodemother.ai.model;

import jdk.jfr.Description;
import lombok.Data;

/**
 * 多文件代码结果
 *
 */
@Description("生成多个代码文件的结果")
@Data
public class MultiFileCodeResult {

    private String htmlCode;

    private String cssCode;

    private String jsCode;

    private String description;
}
