package org.alexside.webcrawl.config;

import lombok.RequiredArgsConstructor;
import org.alexside.webcrawl.modules.loader.WebcrawlLoaderAPI;
import org.alexside.webcrawl.modules.store.WebcrawlStoreAPI;
import org.alexside.webcrawl.modules.processor.WebcrawlProcessorAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final ApplicationContext context;

    @Bean
    @Primary
    public WebcrawlLoaderAPI webcrawlLoaderAPI(@Value("${webcrawl.loader.type}") String name) {
        return (WebcrawlLoaderAPI) context.getBean(name);
    }

    @Bean
    @Primary
    public WebcrawlStoreAPI webcrawlStoreAPI(@Value("${webcrawl.loader.store.type}") String name) {
        return (WebcrawlStoreAPI) context.getBean(name);
    }

    @Bean
    @Primary
    public WebcrawlProcessorAPI webcrawlProcessorAPI(@Value("${webcrawl.processor.type}") String name) {
        return (WebcrawlProcessorAPI) context.getBean(name);
    }
}