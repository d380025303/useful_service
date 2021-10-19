package com.daixinmini.base.util;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.daixinmini.base.exception.BizException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.google.common.base.Strings;

public class ParseXmlUtil {
	
	public static String getAttr(Element element, String attrName) {
		return getAttr(element, attrName, null, null);
	}
	
	public static String getAttr(Element element, String attrName, String defaultValue) {
		return getAttr(element, attrName, defaultValue, null);
	}
	
	public static String getAttr(Element element, String attrName, String[] scopes) {
		return getAttr(element, attrName, null, scopes);
	}
    
    public static String getAttr(Element element, String attrName, String defaultValue, String[] scopes) {
        boolean isRequired = false;
        if (attrName.startsWith("*")) {
            attrName = attrName.substring(1);
            isRequired = true;
        }
        
        String attrValue = element.attributeValue(attrName);
        
        //校验必填
        if (isRequired && Strings.isNullOrEmpty(attrValue)) {
			throw new BizException(MessageFormat.format("解析xml错误，字段{0}不可为空", attrName));
        }
        
        //校验范围
        if (scopes != null && scopes.length > 0 && !Strings.isNullOrEmpty(attrValue)) {
            boolean inScope = Arrays.asList(scopes).contains(attrValue);
            if (!inScope) {
				throw new BizException(MessageFormat.format("解析xml错误，字段{0}必须是({1})之一", attrName,
						concatStringArray(scopes)));
            }
        }
        
        //处理默认值
        if (Strings.isNullOrEmpty(attrValue) && !Strings.isNullOrEmpty(defaultValue)) {
            attrValue = defaultValue;
        }
        
        return attrValue;
    }
	
    public static void parseAttr(Element element, String attrName, Object destVo) {
        parseAttr(element, attrName, null, null, destVo, attrName);
    }
    
    public static void parseAttr(Element element, String attrName, String defaultValue, Object destVo) {
        parseAttr(element, attrName, defaultValue, null, destVo, attrName);
    }
    
    public static void parseAttr(Element element, String attrName, String[] scopes, Object destVo) {
        parseAttr(element, attrName, null, scopes, destVo, attrName);
    }
    
    public static void parseAttr(Element element, String attrName, String defaultValue, String[] scopes, Object destVo) {
    	parseAttr(element, attrName, defaultValue, scopes, destVo, attrName);
    }
	
    public static void parseAttr(Element element, String attrName, Object destVo, String destPropertyName) {
        parseAttr(element, attrName, null, null, destVo, destPropertyName);
    }
    
    public static void parseAttr(Element element, String attrName, String defaultValue, Object destVo, String destPropertyName) {
        parseAttr(element, attrName, defaultValue, null, destVo, destPropertyName);
    }
    
    public static void parseAttr(Element element, String attrName, String[] scopes, Object destVo, String destPropertyName) {
        parseAttr(element, attrName, null, scopes, destVo, destPropertyName);
    }
    
    public static void parseAttr(Element element, String attrName, String defaultValue, String[] scopes, Object destVo, String destPropertyName) {
        String attrValue = getAttr(element, attrName, defaultValue, scopes);
        setValue(destVo, destPropertyName, attrValue);
    }
    
    private static String concatStringArray(String[] array){
        String result = "";
        if(array != null){
            for(String str : array){
                result = result + ", " + str;
            }
            result = result.substring(2);
        }
        return result;
    }
    
    private static void setValue(Object dest, String property, String value) {
		if (property.startsWith("*")) {
			property = property.substring(1);
		}

		// 兼容Map
		if (dest instanceof Map) {
			if (value != null) {
				((Map) dest).put(property, value);
			}
			return;
		}
		
		// 常规Model处理
        Field field = null;
        try{
			field = dest.getClass().getDeclaredField(property);
        }catch(NoSuchFieldException e){
			throw new BizException(MessageFormat.format("字段{0}在类{1}中不存在", property, dest.getClass().getName()));
        }
        
        Object convertValue = null;
        if(value != null && !"".equals(value)){
            try{
                if(field.getType().equals(int.class) || field.getType().equals(Integer.class)){
                    convertValue = Integer.valueOf(value);
                }else if(field.getType().equals(BigDecimal.class)){
                	convertValue = BasicUtil.decimal(value);
                }else if(field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)){
                    convertValue = Boolean.valueOf(value);
                }else if(field.getType().equals(String.class)){
                    convertValue = value;
                }
            }catch(Exception e){
				throw new BizException(MessageFormat.format("设置{0}={1}失败", property, value));
            }
        }
        
        if(convertValue != null){
            try {
				if (field.isAccessible()) {
					field.set(dest, convertValue);
				} else {
					String setterName = "set" + field.getName().substring(0, 1).toUpperCase()
							+ field.getName().substring(1);
					Method setterMethod = dest.getClass().getMethod(setterName, field.getType());
					setterMethod.invoke(dest, convertValue);
				}
            } catch (Exception e) {
				throw new BizException(MessageFormat.format("设置{0}={1}失败", property, value));
            }
        }
    }
    
    public static String formatXml(Document document) {
        OutputFormat xmlFormat = OutputFormat.createPrettyPrint();  
        xmlFormat.setEncoding("UTF-8");
        xmlFormat.setNewlines(true);
        xmlFormat.setIndent(true); 
        xmlFormat.setIndent("\t"); 
        
        //创建写文件方法
    	StringWriter stringWriter = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(stringWriter,xmlFormat);
        try {
			xmlWriter.write(document);
		} catch (IOException e) {
		} finally{
			try {
				xmlWriter.close();
			} catch (IOException e) {}
		}

        String xml = stringWriter.toString();
        if(Strings.isNullOrEmpty(xml)){
        	xml = document.asXML();
        }
        return xml;
    }

    public static Map<String, String> xml2Map(String xmlStr) {
        Map<String, String> map = new HashMap<String, String>();
        if (!Strings.isNullOrEmpty(xmlStr)) {
            Document doc = null;
            try {
                doc = DocumentHelper.parseText(xmlStr);
            } catch (DocumentException e) {
                throw new BizException("DocumentException", "xml文件读取失败");
            }
            Element root = doc.getRootElement();
            @SuppressWarnings("rawtypes")
            List children = root.elements();
            if (children != null && children.size() > 0) {
                for (int i = 0; i < children.size(); i++) {
                    Element child = (Element) children.get(i);
                    map.put(child.getName(), child.getTextTrim());
                }
            }
        }
        return map;
    }

    public static List<XmlParamVo> xml2List(String xmlStr) {
        List<XmlParamVo> result=new ArrayList<>();
        if (!Strings.isNullOrEmpty(xmlStr)) {
            Document doc = null;
            try {
                doc = DocumentHelper.parseText(xmlStr);
            } catch (DocumentException e) {
                throw new BizException("DocumentException", "xml文件读取失败");
            }
            Element root = doc.getRootElement();
            List children = root.elements();
            result= xml2List(children);
        }
        return result;
    }
    private static List<XmlParamVo> xml2List(List<Element> list){
        List<XmlParamVo> result=new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element child = (Element) list.get(i);
                List childList = child.elements();
                XmlParamVo temp=new XmlParamVo();
                temp.setXmlName(child.getName());
                if(childList!=null&&childList.size()!=0){
                    List xml2List = xml2List(childList);
                    temp.setValue(xml2List);
                }else{
                    temp.setValue(child.getTextTrim());
                }
                result.add(temp);
            }
        }
        return result;
    }


}