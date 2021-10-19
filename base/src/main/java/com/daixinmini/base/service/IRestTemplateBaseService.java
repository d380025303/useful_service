package com.daixinmini.base.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;

public interface IRestTemplateBaseService {
    /**
     * get请求获取实体
     * @param url
     * @param reference
     *  {@code ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<List<String>>() {};}
     * @param uriVariables
     *  param请求参数
     */
    <T> T sendGet(String url, ParameterizedTypeReference<T> reference, Object... uriVariables);

    <T> T sendGet(String url, ParameterizedTypeReference<T> reference, HttpHeaders headers,Object... uriVariables);

    /**
     *  post请求并获取实体
     * @param url
     * @param reference
     *  {@code ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<List<String>>() {};}
     * @param reqVo
     *  body请求体
     * @param uriVariables
     *  param请求参数
     */
    <T> T sendPost(String url, ParameterizedTypeReference<T> reference, Object reqVo, Object... uriVariables);

    <T> T sendPost(String url, ParameterizedTypeReference<T> reference, Object reqVo, HttpHeaders headers, Object... uriVariables);
}
