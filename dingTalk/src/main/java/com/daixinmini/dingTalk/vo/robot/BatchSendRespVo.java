package com.daixinmini.dingTalk.vo.robot;

import java.util.List;

/**
 * <p>Project: mul-service </p>
 * <p>Description: </p>
 * <p>Copyright (c) 2021 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:daixin@karrytech.com">Dai Xin</a>
 */
public class BatchSendRespVo {
    private String processQueryKey;
    private List<String> invalidStaffIdList;
    private List<String> flowControlledStaffIdList;

    public String getProcessQueryKey() {
        return processQueryKey;
    }

    public void setProcessQueryKey(String processQueryKey) {
        this.processQueryKey = processQueryKey;
    }

    public List<String> getInvalidStaffIdList() {
        return invalidStaffIdList;
    }

    public void setInvalidStaffIdList(List<String> invalidStaffIdList) {
        this.invalidStaffIdList = invalidStaffIdList;
    }

    public List<String> getFlowControlledStaffIdList() {
        return flowControlledStaffIdList;
    }

    public void setFlowControlledStaffIdList(List<String> flowControlledStaffIdList) {
        this.flowControlledStaffIdList = flowControlledStaffIdList;
    }
}