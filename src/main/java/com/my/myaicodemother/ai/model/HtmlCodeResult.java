package com.my.myaicodemother.ai.model;

import jdk.jfr.Description;
import lombok.Data;

/**
 * HTML代码结果
 */
@Description("HTML代码文件的结果")
@Data
public class HtmlCodeResult {

    private String htmlCode;

    private String description;
}
