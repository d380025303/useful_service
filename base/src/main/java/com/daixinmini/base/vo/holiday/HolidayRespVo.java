package com.daixinmini.base.vo.holiday;


public class HolidayRespVo {
    private String code;
    private HolidayTypeVo type;
    private HolidayVo holiday;

    public HolidayTypeVo getType() {
        return type;
    }

    public void setType(HolidayTypeVo type) {
        this.type = type;
    }

    public HolidayVo getHoliday() {
        return holiday;
    }

    public void setHoliday(HolidayVo holiday) {
        this.holiday = holiday;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}