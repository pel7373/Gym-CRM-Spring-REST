package org.gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Collections;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.gym"})
public class WebConfig implements WebMvcConfigurer
{
    @Bean
    public HandlerMapping resourseHandlerMapping() {
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(Integer.MAX_VALUE - 1);
        handlerMapping.setUrlMap(Collections.singletonMap("/resources/**", resourceHttpRequestHandler()));
        return handlerMapping;
    }

    @Bean
    public ResourceHttpRequestHandler resourceHttpRequestHandler() {
        ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
        requestHandler.setLocations(Collections.singletonList(new ClassPathResource("resources/")));
        return requestHandler;
    }

    @Bean
    public HandlerMapping defaultServletHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MAX_VALUE);
        mapping.setUrlMap(Collections.singletonMap("/**", resourceHttpRequestHandler()));
        return mapping;
    }
}
