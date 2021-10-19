package com.daixinmini.dingTalk.service.impl;

import com.aliyun.dingtalkrobot_1_0.Client;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOHeaders;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTORequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.daixinmini.base.exception.BizException;
import com.daixinmini.base.service.IRestTemplateBaseService;
import com.daixinmini.base.util.JsonUtil;
import com.daixinmini.dbBase.model.Message;
import com.daixinmini.dbBase.service.IDbMessageService;
import com.daixinmini.dbBase.service.IDbService;
import com.daixinmini.dbBase.service.impl.DbMessageService;
import com.daixinmini.dbBase.util.IDbConst;
import com.daixinmini.dingTalk.service.IDingTalkAccessTokenService;
import com.daixinmini.dingTalk.service.IDingTalkRobotService;
import com.daixinmini.dingTalk.vo.robot.AtVo;
import com.daixinmini.dingTalk.vo.robot.Robot2UserVo;
import com.daixinmini.dingTalk.vo.robot.RobotRespVo;
import com.daixinmini.dingTalk.vo.robot.TextVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DingTalkRobotService implements IDingTalkRobotService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDingTalkAccessTokenService accessTokenService;
    @Autowired
    private DingTalkSetting dingTalkSetting;
    @Autowired
    private IDbMessageService dbMessageService;
    @Autowired
    private IDbService dbService;
    @Autowired
    private IRestTemplateBaseService restTemplateBaseService;

    private List<DingTalkCustomRobotSetting> getCustomSettingList() {
        return dingTalkSetting.getCustomRobot();
    }

    private List<DingTalkCustomRobotSetting> getCustomSettingListByType(String type) {
        return dingTalkSetting.getCustomRobot().stream().filter(
                dingTalkCustomRobotSetting -> type.equals(dingTalkCustomRobotSetting.getType())
        ).collect(Collectors.toList());
    }

    private String getCustomRobotSign(Long timestampL, String secret) {
        String stringToSign = timestampL + "\n" + secret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        } catch (Exception e) {
            throw new BizException("生成sign失败: ", e);
        }
    }

    @Override
    public void sendMessage4CustomRobot(String msg, AtVo vo, String type) {
        List<DingTalkCustomRobotSetting> customSettingList = getCustomSettingListByType(type);
        for (DingTalkCustomRobotSetting customRobotSetting : customSettingList) {
            String url = customRobotSetting.getUrl();
            String secret = customRobotSetting.getSecret();
            long timestampL = System.currentTimeMillis();
            String sign = getCustomRobotSign(timestampL, secret);
            url = url + "&timestamp=" + timestampL + "&sign=" + sign;
            Robot2UserVo reqVo = new Robot2UserVo();
            reqVo.setAt(vo);
            TextVo textVo = new TextVo();
            textVo.setContent(msg);
            reqVo.setText(textVo);
            reqVo.setMsgType("text");
            ParameterizedTypeReference<String> typeRef = new ParameterizedTypeReference<String>() {
            };
            String s = restTemplateBaseService.sendPost(url, typeRef, reqVo);
            logger.info(s);
        }
    }

    public Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    @Override
    public Robot2UserVo msg(Long timestamp, String sign, String msg) {
        String receiveMsg = null;
        String senderStaffId = null;
        try {
            DingTalkAccessTokenSetting dingTalkAccessTokenSetting = accessTokenService.getAccessTokenSetting("robot");
            String appSecret = dingTalkAccessTokenSetting.getAppSecret();
            String stringToSign = timestamp + "\n" + appSecret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(appSecret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String mySign = new String(Base64.encodeBase64(signData));
            if (mySign.equalsIgnoreCase(sign)) {
                logger.info(msg);
                ObjectMapper objectMapper = JsonUtil.buildObjectMapper();
                RobotRespVo robotRespVo = objectMapper.readValue(msg, RobotRespVo.class);
                String msgId = robotRespVo.getMsgId();
                String conversationType = robotRespVo.getConversationType();
                senderStaffId = robotRespVo.getSenderStaffId();
                String senderId = robotRespVo.getSenderId();
                if ("1".equalsIgnoreCase(conversationType)) {
                    String msgType = robotRespVo.getMsgType();
                    if ("text".equalsIgnoreCase(msgType)) {
                        TextVo text = robotRespVo.getText();
                        String content = text.getContent();
                        String type = IDbConst.Message.MESSAGE_TYPE_DING_TALK;

                        Message message = new Message();
                        message.setContent(content);
                        message.setType(type);
                        message.setThirdMessageId(msgId);
                        message.setFromUserId(Strings.isBlank(senderStaffId) ? senderId : senderStaffId);

                        receiveMsg = dbMessageService.doHandleMessage(message);
                    } else {
                        throw new BizException("暂不支持群聊哦！");
                    }
                }
            }
        } catch (BizException e) {
            receiveMsg = e.getMessage();
            logger.error("msg: ", e);
        } catch (Exception e) {
            receiveMsg = "出错啦~";
            logger.error("msg: ", e);
        }
        Robot2UserVo vo = new Robot2UserVo();
        vo.setMsgType("text");
        TextVo textVo = new TextVo();
        textVo.setContent(receiveMsg);
        vo.setText(textVo);
        AtVo atVo = new AtVo();
        atVo.setAtUserIds(Lists.newArrayList(senderStaffId));
        return vo;
    }

    @Override
    public String sendMsg(String msg) {
        DingTalkMessageSetting dingTalkMessageSetting = dingTalkSetting.getMessage();
        String userId = dingTalkMessageSetting.getUserId();
        for (String s : DbMessageService.crmList) {
            if (msg.toLowerCase().startsWith(s)) {
                String uuid = "sampleText";
                Message message = new Message();
                message.setContent(msg);
                message.setType(IDbConst.Message.MESSAGE_TYPE_DING_TALK);
                message.setThirdMessageId(uuid);
                message.setFromUserId(userId);

                String receiveMsg = dbMessageService.doHandleMessage(message);
                String accessToken = accessTokenService.getAccessToken("robot");
                DingTalkAccessTokenSetting setting = accessTokenService.getAccessTokenSetting("robot");

                Client client = null;
                try {
                    ObjectNode jsonNodes = JsonUtil.newObject();
                    jsonNodes.put("content", receiveMsg);
                    client = createClient();
                    BatchSendOTOHeaders batchSendOTOHeaders = new BatchSendOTOHeaders();
                    batchSendOTOHeaders.xAcsDingtalkAccessToken = accessToken;
                    BatchSendOTORequest batchSendOTORequest = new BatchSendOTORequest()
                            .setRobotCode(setting.getAppKey())
                            .setUserIds(java.util.Arrays.asList(
                                    userId
                            ))
                            .setMsgKey(uuid)
                            .setMsgParam(jsonNodes.toString());
                    client.batchSendOTOWithOptions(batchSendOTORequest, batchSendOTOHeaders, new RuntimeOptions());
                } catch (TeaException err) {
                    if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                        // err 中含有 code 和 message 属性，可帮助开发定位问题
                        logger.error(err.code, err.message);
                    }
                } catch (Exception _err) {
                    TeaException err = new TeaException(_err.getMessage(), _err);
                    if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                        // err 中含有 code 和 message 属性，可帮助开发定位问题
                        logger.error(err.code, err.message);
                    }
                }
                return receiveMsg;
            }
        }
        return null;
    }


}