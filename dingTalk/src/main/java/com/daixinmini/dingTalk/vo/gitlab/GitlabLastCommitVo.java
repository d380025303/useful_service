package com.daixinmini.dingTalk.vo.gitlab;


import com.daixinmini.dingTalk.vo.robot.GitlabUserVo;

public class GitlabLastCommitVo {
    private String id;
    private GitlabUserVo author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GitlabUserVo getAuthor() {
        return author;
    }

    public void setAuthor(GitlabUserVo author) {
        this.author = author;
    }
}