package com.daixinmini.dingTalk.service;

import com.daixinmini.dingTalk.vo.robot.AtVo;
import com.daixinmini.dingTalk.vo.robot.Robot2UserVo;

import java.security.NoSuchAlgorithmException;

/**
 * <p>Project: mul-service </p>
 * <p>Description: </p>
 * <p>Copyright (c) 2021 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:daixin@karrytech.com">Dai Xin</a>
 */
public interface IDingTalkRobotService {

    void sendMessage4CustomRobot(String msg, AtVo vo, String type);

    Robot2UserVo msg(Long timestamp, String sign, String msg) throws NoSuchAlgorithmException, Exception;

    String sendMsg(String msg);


}