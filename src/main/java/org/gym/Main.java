package org.gym;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.gym.config.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
//        Tomcat tomcat = new Tomcat();
//
//        final Connector connector = new Connector();
//        connector.setPort(8080);
//        connector.setScheme("http");
//        connector.setProperty("protocol", "HTTP/1.1");
//        connector.setSecure(false);
//        tomcat.getService().addConnector(connector);
//        tomcat.setConnector(connector);

        //        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://reqres.in/api/users/2";
//        String response = restTemplate.getForObject(url, String.class);
//        System.out.println(response);
//
//        Map<String, String> json = new HashMap<>();
//        json.put("name", "Test name");
//        json.put("job", "Test job");
//
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(json);
//
//        url = "https://reqres.in/api/users/";
//        response = restTemplate.postForObject(url, request, String.class);
//        System.out.println(response);
    }
}
