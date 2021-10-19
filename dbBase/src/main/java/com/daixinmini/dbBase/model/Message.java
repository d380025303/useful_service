package com.daixinmini.dbBase.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "message")
@Entity
public class Message {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "content")
    private String content;
    @Column(name = "type")
    private String type;
    @Column(name = "third_message_id")
    private String thirdMessageId;
    @Column(name = "from_user_id")
    private String fromUserId;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getThirdMessageId() {
        return thirdMessageId;
    }

    public void setThirdMessageId(String thirdMessageId) {
        this.thirdMessageId = thirdMessageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}