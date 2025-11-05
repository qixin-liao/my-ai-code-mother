package com.my.myaicodemother.service;

import com.my.myaicodemother.mode.dto.chathistory.ChatHistoryQueryRequest;
import com.my.myaicodemother.mode.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.my.myaicodemother.mode.entity.ChatHistory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author 廖祁新
 */
public interface ChatHistoryService extends IService<ChatHistory> {
    /**
     * 添加聊天消息。
     *
     * @param appId       应用ID
     * @param message     消息内容
     * @param messageType 消息类型
     * @param userId      用户ID
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 根据应用ID删除对话历史。
     *
     * @param appId 应用ID
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    boolean deleteByAppId(Long appId);

    /**
     * 获取查询包装类
     *
     * @param chatHistoryQueryRequest 查询条件
     * @return 查询包装类
     */

    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    /**
     * 根据应用ID分页查询对话历史。
     *
     * @param appId          应用ID
     * @param pageSize       页面大小
     * @param lastCreateTime 最后创建时间
     * @param loginUser      登录用户
     * @return 对话历史列表
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize, LocalDateTime lastCreateTime, User loginUser);
}
