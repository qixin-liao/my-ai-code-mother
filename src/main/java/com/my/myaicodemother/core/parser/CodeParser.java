package com.my.myaicodemother.core.parser;

/**
 * 代码解析器接口
 */
public interface CodeParser<T> {
    /**
     * 解析代码
     *
     * @param codeContent 代码内容
     * @return 解析结果
     */
    T parseCode(String codeContent); // 解析  ParserCode
}
