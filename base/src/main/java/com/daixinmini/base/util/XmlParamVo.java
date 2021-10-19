package com.daixinmini.base.util;

import java.util.List;

public class XmlParamVo {
    private String xmlName;
    private Object value;

    @Override
    public String toString() {
        StringBuilder s=new StringBuilder();
        s.append("{").append("\"xmlName\":\"").append(xmlName).append("\",\"value\":");
        if(value instanceof List){
            s.append("[");
            for (Object o : (List) value) {
                String temp = ((XmlParamVo) o).toString();
                s.append(temp).append(",");
            }
            s.deleteCharAt(s.length()-1);
            s.append("]");
        }else{
            s.append("\"").append(((String)value)).append("\"");
        }
        s.append("}");
        return s.toString();
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
