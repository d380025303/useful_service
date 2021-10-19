package com.daixinmini.base.service.impl;

import com.daixinmini.base.service.IHolidayService;
import com.daixinmini.base.service.IRestTemplateBaseService;
import com.daixinmini.base.util.DateUtil;
import com.daixinmini.base.vo.holiday.HolidayRespVo;
import com.daixinmini.base.vo.holiday.HolidayTypeVo;
import com.daixinmini.base.vo.holiday.HolidayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class HolidayService implements IHolidayService {
    @Autowired
    private IRestTemplateBaseService restTemplateBaseService;

    @Override
    public boolean isWorkDay(Timestamp timestamp) {
        ParameterizedTypeReference<HolidayRespVo> typeRef = new ParameterizedTypeReference<HolidayRespVo>() {
        };
        HolidayRespVo s = restTemplateBaseService.sendGet("http://timor.tech/api/holiday/info/" + DateUtil.yyyyMMdd(timestamp), typeRef);
        HolidayVo holiday = s.getHoliday();
        HolidayTypeVo type = s.getType();
        String name = type.getName();
        if (holiday != null && holiday.getHoliday()) {
            return false;
        }
        if (holiday == null && ("周六".equals(name) || "周日".equals(name))) {
            return false;
        }
        return true;
    }


}