package com.my.myaicodemother.core.saver;

import com.my.myaicodemother.ai.model.HtmlCodeResult;
import com.my.myaicodemother.exception.BusinessException;
import com.my.myaicodemother.exception.ErrorCode;
import com.my.myaicodemother.mode.enums.CodeGenTypeEnum;

/**
 * HTML代码文件保存器模板
 *
 * @author 廖祁新
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {

    @Override
    public CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        // 验证 HTML 代码
        if (result.getHtmlCode() == null || result.getHtmlCode().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码不能为空");
        }
    }
}
