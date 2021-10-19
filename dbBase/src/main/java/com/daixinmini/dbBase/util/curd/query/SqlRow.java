package com.daixinmini.dbBase.util.curd.query;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

public interface SqlRow<V> extends Map<String, V> {

    Boolean getBoolean(String column);

    UUID getUUID(String column);

    Integer getInteger(String column);

    BigDecimal getBigDecimal(String column);

    Long getLong(String column);

    Double getDouble(String column);

    Float getFloat(String column);

    String getString(String column);

    java.util.Date getUtilDate(String column);

    java.sql.Date getDate(String column);

    Timestamp getTimestamp(String column);

    byte[] getBytes(String column, String charset);

}
