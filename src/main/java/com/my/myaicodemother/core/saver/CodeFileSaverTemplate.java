package com.my.myaicodemother.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.my.myaicodemother.exception.BusinessException;
import com.my.myaicodemother.exception.ErrorCode;
import com.my.myaicodemother.mode.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 抽象代码文件保存器 - 模板方法模式
 *
 * @param <T>
 */
public abstract class CodeFileSaverTemplate<T> {

    // 文件保存根目录
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 模板 方法： 保存代码的标准流程
     *
     * @param result
     * @return 文件目录对象
     */
    public final File saveCode(T result) {
        // 1. 验证输入
        validateInput(result);
        // 2. 构建唯一目录路径
        String baseDirPath = buildUniqueDir();
        // 3. 保存文件 （具体实现交给子类）
        saveFiles(result, baseDirPath);
        // 4. 返回文件目录对象
        return new File(baseDirPath);
    }

    /**
     * 写入单个文件
     * @param dirPath 目录路径
     * @param filename 文件名
     * @param content 文件内容
     */
    public final void writeToFile(String dirPath, String filename, String content) {
        if (StrUtil.isNotBlank(content)) {
            String filePath = dirPath + File.separator + filename;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }

    /**
     * 验证输入参数（可由子类覆盖）
     *
     * @param result
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码对象不能为空");
        }
    }

    /**
     * 构建唯一目录路径：tmp/code_output/bizType_雪花ID
     *
     * @return 目录路径
     */
    protected String buildUniqueDir() {
        String codeType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", codeType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 保存文件（由子类实现）
     *
     * @param result
     * @param baseDirPath
     */
    protected abstract void saveFiles(T result, String baseDirPath);

    /**
     * 获取代码生成类型 (由子类实现)
     *
     * @return 代码生成类型枚举
     */
    protected abstract CodeGenTypeEnum getCodeType();
}
