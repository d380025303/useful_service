package com.daixinmini.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * <p>Project: ktcrm-token-starter</p>
 * <p>Description: 初始化RestTemplate.</p>
 * <p>Copyright (c) 2019 Karrytech Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:linguo@karrytech.com">Lin Guo</a>
 */
@Configuration
public class RestTemplateConfigurer {
    /**
     * 用于微服务间调用
     * 
     * @return
     */
    @Bean
    RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30000);
        httpRequestFactory.setConnectTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
//        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
//        list.add(new MappingJackson2HttpMessageConverter());
//
//        Iterator<HttpMessageConverter<?>> iterator = list.iterator();
//
//        while (iterator.hasNext()) {
//            HttpMessageConverter<?> httpMessageConverter = iterator.next();
//            if (httpMessageConverter instanceof StringHttpMessageConverter) {
//                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("utf-8"));
//            } else if (httpMessageConverter instanceof MappingJackson2XmlHttpMessageConverter) {
//                iterator.remove();
//            }
//        }
        return restTemplate;
    }
}
