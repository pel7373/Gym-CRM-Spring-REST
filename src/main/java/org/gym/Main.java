package org.gym;

import org.gym.config.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Main {
    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://reqres.in/api/users/2";
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);

        Map<String, String> json = new HashMap<>();
        json.put("name", "Test name");
        json.put("job", "Test job");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(json);

        url = "https://reqres.in/api/users/";
        response = restTemplate.postForObject(url, request, String.class);
        System.out.println(response);
    }
}
