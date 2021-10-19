package com.daixinmini.dingTalk.vo.robot;

import com.daixinmini.dingTalk.vo.gitlab.GitlabLastCommitVo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GitlabReqVo {
    @JsonProperty("object_kind")
    private String objectKind;
    @JsonProperty("event_type")
    private String eventType;
    private GitlabProjectVo project;
    @JsonProperty("object_attributes")
    private GitlabMergeRequestVo objectAttributes;
    private GitlabUserVo user;

    public GitlabUserVo getUser() {
        return user;
    }

    public void setUser(GitlabUserVo user) {
        this.user = user;
    }

    public String getObjectKind() {
        return objectKind;
    }

    public void setObjectKind(String objectKind) {
        this.objectKind = objectKind;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public GitlabProjectVo getProject() {
        return project;
    }

    public void setProject(GitlabProjectVo project) {
        this.project = project;
    }

    public GitlabMergeRequestVo getObjectAttributes() {
        return objectAttributes;
    }

    public void setObjectAttributes(GitlabMergeRequestVo objectAttributes) {
        this.objectAttributes = objectAttributes;
    }
}