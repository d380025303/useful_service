package com.daixinmini.dbBase.service;


import com.daixinmini.dbBase.model.Message;

public interface IDbMessageActionService {

    String doSaveAction(Message message, String searchMsg);

    String doQueryAction(Message message, String searchMsg);

    String doQueryDetailAction(Message message, String searchMsg);

    String doDeleteAction(Message message, String searchMsg);

    String doQueryAction4Default(Message message, String searchMsg);

    String doCrmAction(String searchMsg);

}