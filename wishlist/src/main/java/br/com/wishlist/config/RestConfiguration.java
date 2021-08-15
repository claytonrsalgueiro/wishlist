/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author clayton.salgueiro
 */
@Configuration
@EnableAsync
public class RestConfiguration {

    /**
     * Creating {@link RestTemplate} bean
     *
     * @param builder The {@link RestTemplateBuilder}
     * @return The {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        return builder.build();
    }

}
