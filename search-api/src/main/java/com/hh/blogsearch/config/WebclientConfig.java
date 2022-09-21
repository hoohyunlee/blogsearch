package com.hh.blogsearch.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;
/**
 * Webclient 빈등록을 위한 컨피그
 */
@Configuration
public class WebclientConfig {
    private final int webclient_Timeout_Value = 5000;

    // Webclient Connection & Read Time Out 5000ms으로 설정
    @Bean
    public WebClient webClient(){
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webclient_Timeout_Value)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(webclient_Timeout_Value, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

}
