package com.jiaxin.aiweb.service;

import com.jiaxin.aiweb.model.dto.chathistory.ChatHistoryQueryRequest;
import com.jiaxin.aiweb.model.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.jiaxin.aiweb.model.entity.ChatHistory;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author 朱佳鑫
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    boolean deleteByAppId(Long appId);

    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);
}
