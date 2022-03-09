package com.koreait.cloneinstagram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
//xml 파일과 같은 역할
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Value("${spring.servlet.multipart.location}")
    private String uploadImagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pic/**")//pic 연결되는순간
                .addResourceLocations("file:///" + uploadImagePath + "/")//여기서 시작
                .setCachePeriod(4000)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
