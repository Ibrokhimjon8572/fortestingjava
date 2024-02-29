package org.example.controller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/vendors/**")
                .addResourceLocations("classpath:/template/vendors/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/template/css/");

        registry.addResourceHandler("/image/**")
                .addResourceLocations("classpath:/image/");
    }
}


