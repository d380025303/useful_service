package com.daixinmini.base.service.impl;

import com.daixinmini.base.service.IRestTemplateBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateBaseService implements IRestTemplateBaseService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public <T> T sendGet(String url, ParameterizedTypeReference<T> reference, Object... uriVariables) {
       return sendGet(url,reference,getHeaders(),uriVariables);
    }

    @Override
    public <T> T sendGet(String url, ParameterizedTypeReference<T> reference, HttpHeaders headers, Object... uriVariables) {
        if (reference == null) {
            reference = new ParameterizedTypeReference<T>() {
            };
        }
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<T> exchange = null;
            if (uriVariables.length == 0) {
                exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, reference);
            } else {
                exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, reference, uriVariables);
            }
            return exchange.getBody();
        } catch (Exception e) {
            logger.error("send get fail: ", e);
            throw e;
        }
    }

    @Override
    public <T> T sendPost(String url, ParameterizedTypeReference<T> reference, Object reqVo, Object... uriVariables) {
        return sendPost(url, reference, reqVo, getHeaders(), uriVariables);
    }

    @Override
    public <T> T sendPost(String url, ParameterizedTypeReference<T> reference, Object reqVo, HttpHeaders headers, Object... uriVariables) {
        if (reference == null) {
            reference = new ParameterizedTypeReference<T>() {
            };
        }
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(reqVo, headers);
            ResponseEntity<T> exchange = null;
            if (uriVariables.length == 0) {
                exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, reference);
            } else {
                exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, reference, uriVariables);
            }
            return exchange.getBody();
        } catch (Exception e) {
            logger.error("send get fail: ", e);
            throw e;
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        return headers;
    }

}