package com.my.myaicodemother.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.my.myaicodemother.exception.BusinessException;
import com.my.myaicodemother.exception.ErrorCode;
import com.my.myaicodemother.exception.ThrowUtils;
import com.my.myaicodemother.mode.dto.AppQueryRequest;
import com.my.myaicodemother.mode.entity.User;
import com.my.myaicodemother.mode.enums.UserRoleEnum;
import com.my.myaicodemother.mode.vo.AppVO;
import com.my.myaicodemother.service.AppService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.my.myaicodemother.mode.entity.App;
import com.my.myaicodemother.mapper.AppMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用 服务层实现。
 *
 * @author 廖祁新
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Override
    public void validApp(App app, boolean add) {
        ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR);
        String appName = app.getAppName();
        String initPrompt = app.getInitPrompt();

        // 创建时必须填写 appName 和 initPrompt
        if (add) {
            ThrowUtils.throwIf(StrUtil.isBlank(appName), ErrorCode.PARAMS_ERROR, "应用名称不能为空");
            ThrowUtils.throwIf(StrUtil.isBlank(initPrompt), ErrorCode.PARAMS_ERROR, "应用初始化 prompt 不能为空");
        }
        // 更新时，如果有值则校验
        if (StrUtil.isNotBlank(appName)) {
            ThrowUtils.throwIf(appName.length() > 100, ErrorCode.PARAMS_ERROR, "应用名称过长");
        }
        if (StrUtil.isNotBlank(initPrompt)) {
            ThrowUtils.throwIf(initPrompt.length() > 10000, ErrorCode.PARAMS_ERROR, "初始化 prompt 过长");
        }
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();

        return QueryWrapper.create()
                .eq("id", id)
                .like("appName", appName)
                .like("cover", cover)
                .like("initPrompt", initPrompt)
                .eq("codeGenType", codeGenType)
                .eq("deployKey", deployKey)
                .eq("priority", priority)
                .eq("userId", userId)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }
        return appList.stream()
                .map(this::getAppVO)
                .toList();
    }

    @Override
    public void checkAppAuth(App app, User loginUser) {
        ThrowUtils.throwIf(app == null || loginUser == null, ErrorCode.PARAMS_ERROR);
        // 管理员可以操作所有应用
        if (UserRoleEnum.ADMIN.getValue().equals(loginUser.getUserRole())) {
            return;
        }
        // 普通用户只能操作自己创建的应用
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限操作该应用");
        }
    }
}
