package com.szs.prj.config;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    @Value("${webclient.scrap.timeout}")
    private Long timeOut;

    @Value("${webclient.scrap.url}")
    private String scrapUrl;

    @Value("${webclient.scrap.header.key}")
    private String headerKey;

    @Value("${webclient.scrap.header.value}")
    private String headerValue;

    @Bean
    public WebClient scrapWebClient() {
        HttpClient httpClient = HttpClient.create()
                .secure(t -> {
                    try {
                        t.sslContext(SslContextBuilder.forClient()
                                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                .build());
                    } catch (SSLException e) {
                    }
                })
                .responseTimeout(Duration.ofSeconds(timeOut));
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(scrapUrl);
        //encoding 설정
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(scrapUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(headerKey, headerValue)
                .build();
    }
}
