package com.daixinmini.dingTalk.controller;

import com.daixinmini.dingTalk.service.IDingTalkRobotService;
import com.daixinmini.dingTalk.vo.robot.Robot2UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


@RestController
@RequestMapping("/daixinmini/dingtalk/robot")
public class RobotController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDingTalkRobotService robotService;

    @PostMapping("msg")
    public Robot2UserVo receiveMsg(HttpServletRequest request, @RequestBody String msg) throws Exception {
        Long timestamp = Long.valueOf(request.getHeader("timestamp"));
        String sign = request.getHeader("sign");
        return robotService.msg(timestamp, sign, msg);
    }

    @PostMapping("sendmsg/{from}")
    public String sendMsg(HttpServletRequest request, @RequestBody String msg, @PathVariable(name = "from") String from) throws Exception {
        return robotService.sendMsg(msg);
    }

}