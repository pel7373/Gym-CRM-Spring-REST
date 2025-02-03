package org.gym;

import org.gym.config.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Main {
    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://regres.in/api/users/2";
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
    }
}
