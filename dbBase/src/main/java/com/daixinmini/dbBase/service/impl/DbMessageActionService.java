package com.daixinmini.dbBase.service.impl;

import com.daixinmini.base.util.Base64Util;
import com.daixinmini.base.util.JsonUtil;
import com.daixinmini.dbBase.model.Message;
import com.daixinmini.dbBase.service.IDbMessageActionService;
import com.daixinmini.dbBase.service.IDbService;
import com.daixinmini.dbBase.util.curd.CrudDaoFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import jdk.nashorn.internal.ir.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbMessageActionService implements IDbMessageActionService {

    @Autowired
    private IDbService dbService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String doSaveAction(Message message, String searchMsg) {
        message.setContent(searchMsg);
        message.setType(message.getType());
        message.setThirdMessageId(message.getThirdMessageId());
        message.setFromUserId(message.getFromUserId());
        dbService.save(message, Message.class);
        return "数据已保存~";
    }

    @Override
    public String doQueryAction(Message message, String searchMsg) {
        return doQueryAction(message, searchMsg, "没有查到任何内容哦~");
    }

    @Override
    public String doQueryAction4Default(Message message, String searchMsg) {
        return doQueryAction(message, searchMsg, "no content, you can input :" + Joiner.on(",").join(DbMessageService.actionList));
    }

    @Override
    public String doCrmAction(String searchMsg) {
        searchMsg = searchMsg.replaceAll("\"", "");
        String s = Base64Util.base64Decode(searchMsg);
        ObjectMapper objectMapper = JsonUtil.buildObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(s);
            return jsonNode.toPrettyString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    private  String doQueryAction(Message message, String searchMsg, String emptyContent) {
        String from = message.getFromUserId();
        String fromType = message.getType();
        List<Message> messageList = CrudDaoFactory.finder(Message.class).where() //
                .eq("type", fromType) //
                .eq("fromUserId", from) //
                .like("content", "%" + searchMsg + "%")
                .findList();
        if (messageList.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Message message1 : messageList) {
                builder.append(message1.getContent());
                builder.append("\r\n");
            }
            return builder.toString();
        } else {
            return emptyContent;
        }
    }

    @Override
    public String doQueryDetailAction(Message message, String searchMsg) {
        String from = message.getFromUserId();
        String fromType = message.getType();
        List<Message> messageList = CrudDaoFactory.finder(Message.class).where() //
                .eq("type", fromType) //
                .eq("from_user_id", from) //
                .like("content", "%" + searchMsg + "%")
                .findList();
        if (messageList.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Message message1 : messageList) {
                builder.append("id: ");
                builder.append(message1.getId());
                builder.append("  content: ");
                builder.append(message1.getContent());
                builder.append("\r\n");
            }
            return builder.toString();
        } else {
            return "没有查到任何内容哦~";
        }
    }

    @Override
    public String doDeleteAction(Message message, String searchMsg) {
        Message dbMessage = CrudDaoFactory.finder(Message.class).where() //
                .eq("id", searchMsg) //
                .findUnique();
        if (dbMessage != null) {
            dbService.delete(dbMessage, Message.class);
        }
        return "删除成功~";
    }


}