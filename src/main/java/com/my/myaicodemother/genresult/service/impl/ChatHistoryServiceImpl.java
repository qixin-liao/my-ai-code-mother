package com.my.myaicodemother.genresult.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.my.myaicodemother.genresult.entity.ChatHistory;
import com.my.myaicodemother.genresult.mapper.ChatHistoryMapper;
import com.my.myaicodemother.genresult.service.ChatHistoryService;
import org.springframework.stereotype.Service;

/**
 * 对话历史 服务层实现。
 *
 * @author 廖祁新
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

}
