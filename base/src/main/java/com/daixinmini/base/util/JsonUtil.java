package com.daixinmini.base.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtil {
    public static ObjectMapper mapper = null;

    static {
        mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getTimeZone("Etc/GMT-8"));
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    public static ObjectMapper buildObjectMapper() {
        return mapper;
    }

    public static ObjectNode newObject() {
        return new ObjectMapper().createObjectNode();
    }

    public static JsonNode asJson(InputStream input) throws JsonProcessingException, IOException {
        return new ObjectMapper().readTree(input);
    }

    public static JsonNode asJson(Reader reader) throws JsonProcessingException, IOException {
        return new ObjectMapper().readTree(reader);
    }

    public static JsonNode asJson(String text) throws JsonProcessingException, IOException {
        return new ObjectMapper().readTree(text);
    }

    public static JsonNode asJson(byte[] bytes) throws JsonProcessingException, IOException {
        return new ObjectMapper().readTree(bytes);
    }

    public static JsonNode asJson(File file) throws JsonProcessingException, IOException {
        return new ObjectMapper().readTree(file);
    }

    public static JsonNode asJson(URL url) throws JsonProcessingException, IOException {
        return new ObjectMapper().readTree(url);
    }

    public static List<JsonNode> array(JsonNode jsonNode) {
        return asArray(jsonNode);
    }

    public static List<JsonNode> asArray(JsonNode jsonNode) {
        List<JsonNode> list = new ArrayList<JsonNode>();
        Iterator<JsonNode> arrayIterator = jsonNode.iterator();
        while (arrayIterator.hasNext()) {
            list.add(arrayIterator.next());
        }
        return list;
    }
    
    public static String toText(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
