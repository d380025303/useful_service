package com.daixinmini.dbBase.service.impl;

import com.daixinmini.base.exception.BizException;
import com.daixinmini.dbBase.model.Message;
import com.daixinmini.dbBase.service.IDbMessageActionService;
import com.daixinmini.dbBase.service.IDbMessageService;
import com.daixinmini.dbBase.util.curd.CrudDaoFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DbMessageService implements IDbMessageService {
    private static final Set<String> queryList = Sets.newHashSet("query", "~q", "-q");
    private static final Set<String> queryDetailList = Sets.newHashSet("query detail", "~qd", "-qd");
    private static final Set<String> saveList = Sets.newHashSet("save", "~s", "-s");
    private static final Set<String> deleteList = Sets.newHashSet("delete", "~d", "-d");
    public static final Set<String> crmList = Sets.newHashSet("~crm", "-crm");
    public static final List<String> actionList = Lists.newArrayList();

    static {
        actionList.addAll(queryList);
        actionList.addAll(queryDetailList);
        actionList.addAll(saveList);
        actionList.addAll(deleteList);
        actionList.addAll(crmList);
        actionList.sort((o1, o2) -> o2.length() - o1.length());
    }

    @Autowired
    private IDbMessageActionService dbMessageActionService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Message findByMsgIdAndType(String thirdMessageId, String type) {
        return CrudDaoFactory.finder(Message.class).where().eq("thirdMessageId", thirdMessageId).eq("type", type).findUnique();
    }

    private String rmMsgType(String msg, String type) {
        return type == null ? msg : msg.substring(type.length(), msg.length());
    }

    private String resolveMsgType(String msg) {
        for (String s : actionList) {
            if (msg.toLowerCase().startsWith(s)) {
                return s;
            }
        }
        return null;
    }

    private String replyMsg(Message message) {
        String sourceMsg = message.getContent();
        String type = resolveMsgType(sourceMsg);
        String searchMsg = rmMsgType(sourceMsg, type);
        if (saveList.contains(type)) {
            return dbMessageActionService.doSaveAction(message, searchMsg);
        } else if (queryList.contains(type)) {
            return dbMessageActionService.doQueryAction(message, searchMsg);
        } else if (queryDetailList.contains(type)) {
            return dbMessageActionService.doQueryDetailAction(message, searchMsg);
        } else if (deleteList.contains(type)) {
            return dbMessageActionService.doDeleteAction(message, searchMsg);
        } else if (crmList.contains(type)) {
            return dbMessageActionService.doCrmAction(searchMsg);
        } {
            return dbMessageActionService.doQueryAction4Default(message, searchMsg);
        }
    }

    @Override
    public String doHandleMessage(Message message) {
        String receiveMsg;
        try {
            String msgId = message.getThirdMessageId();
            String type = message.getType();
            Message dbMessage = findByMsgIdAndType(msgId, type);
            if (dbMessage == null) {
                receiveMsg = replyMsg(message);
            } else {
                throw new BizException("msg exists" + msgId);
            }
        } catch (BizException e) {
            logger.error("e: ", e);
            receiveMsg = e.getMessage();
        }
        return receiveMsg;
    }

}