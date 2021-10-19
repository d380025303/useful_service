package com.daixinmini.dingTalk.vo.robot;

import com.daixinmini.dingTalk.vo.gitlab.GitlabLastCommitVo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RobotRespVo {
    private String conversationId;
    private List<RobotUserVo> atUsers;
    private String chatbotCorpId;
    private String chatbotUserId;
    private String msgId;
    private String senderNick;
    private String isAdmin;
    private String senderStaffId;
    private String sessionWebhookExpiredTime;
    private String createAt;
    private String senderCorpId;
    private String conversationType;
    private String senderId;
    private String conversationTitle;
    private String isInAtList;
    private String sessionWebhook;
    private TextVo text;
    @JsonProperty("msgtype")
    private String msgType;


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public List<RobotUserVo> getAtUsers() {
        return atUsers;
    }

    public void setAtUsers(List<RobotUserVo> atUsers) {
        this.atUsers = atUsers;
    }

    public String getChatbotCorpId() {
        return chatbotCorpId;
    }

    public void setChatbotCorpId(String chatbotCorpId) {
        this.chatbotCorpId = chatbotCorpId;
    }

    public String getChatbotUserId() {
        return chatbotUserId;
    }

    public void setChatbotUserId(String chatbotUserId) {
        this.chatbotUserId = chatbotUserId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderNick() {
        return senderNick;
    }

    public void setSenderNick(String senderNick) {
        this.senderNick = senderNick;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getSenderStaffId() {
        return senderStaffId;
    }

    public void setSenderStaffId(String senderStaffId) {
        this.senderStaffId = senderStaffId;
    }

    public String getSessionWebhookExpiredTime() {
        return sessionWebhookExpiredTime;
    }

    public void setSessionWebhookExpiredTime(String sessionWebhookExpiredTime) {
        this.sessionWebhookExpiredTime = sessionWebhookExpiredTime;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getSenderCorpId() {
        return senderCorpId;
    }

    public void setSenderCorpId(String senderCorpId) {
        this.senderCorpId = senderCorpId;
    }

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getConversationTitle() {
        return conversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }

    public String getIsInAtList() {
        return isInAtList;
    }

    public void setIsInAtList(String isInAtList) {
        this.isInAtList = isInAtList;
    }

    public String getSessionWebhook() {
        return sessionWebhook;
    }

    public void setSessionWebhook(String sessionWebhook) {
        this.sessionWebhook = sessionWebhook;
    }

    public TextVo getText() {
        return text;
    }

    public void setText(TextVo text) {
        this.text = text;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}