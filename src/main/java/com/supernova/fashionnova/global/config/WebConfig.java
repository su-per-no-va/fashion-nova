package com.supernova.fashionnova.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 기본적으로 FormHttpMessageConverter가 포함되어 있어야 합니다.
        converters.add(new FormHttpMessageConverter());
    }

}
