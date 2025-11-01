package com.my.myaicodemother.service;

import com.my.myaicodemother.mode.dto.app.AppQueryRequest;
import com.my.myaicodemother.mode.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.my.myaicodemother.mode.entity.App;
import com.my.myaicodemother.mode.entity.User;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author 廖祁新
 */
public interface AppService extends IService<App> {

    /**
     * 聊天生成代码
     *
     * @param appId 应用Id
     * @param message 消息
     * @param loginUser 登录用户
     * @return 代码
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 应用部署
     *
     * @param appId 应用Id
     * @param loginUser 登录用户
     * @return 可访问的部署地址
     */
    String deployApp(Long appId, User loginUser);

    /**
     * 校验应用参数
     *
     * @param app 应用
     * @param add 是否为创建
     */
    void validApp(App app, boolean add);

    /**
     * 根据查询条件构造数据查询参数
     *
     * @param appQueryRequest 查询条件
     * @return QueryWrapper
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 获取应用封装
     *
     * @param app 应用
     * @return AppVO
     */
    AppVO getAppVO(App app);

    /**
     * 获取应用封装列表
     *
     * @param appList 应用列表
     * @return AppVO 列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 校验应用权限（用户是否可以操作该应用）
     *
     * @param app 应用
     * @param loginUser 登录用户
     */
    void checkAppAuth(App app, User loginUser);
}
