package com.daixinmini.base.service;

import java.sql.Timestamp;

public interface IHolidayService {
    boolean isWorkDay(Timestamp timestamp);
}
