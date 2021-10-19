package com.daixinmini.dingTalk.vo.robot;

import com.daixinmini.dingTalk.vo.gitlab.GitlabLastCommitVo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GitlabMergeRequestVo {
    @JsonProperty("source_branch")
    private String sourceBranch;
    @JsonProperty("target_branch")
    private String targetBranch;
    private String url;
    private String state;
    @JsonProperty("last_commit")
    private GitlabLastCommitVo lastCommit;

    public GitlabLastCommitVo getLastCommit() {
        return lastCommit;
    }

    public void setLastCommit(GitlabLastCommitVo lastCommit) {
        this.lastCommit = lastCommit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSourceBranch() {
        return sourceBranch;
    }

    public void setSourceBranch(String sourceBranch) {
        this.sourceBranch = sourceBranch;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }
}