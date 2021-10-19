package com.daixinmini.dbBase.service;


import com.daixinmini.dbBase.model.Message;

public interface IDbMessageService {

    Message findByMsgIdAndType(String thirdMessageId, String type);

    String doHandleMessage(Message message);
}