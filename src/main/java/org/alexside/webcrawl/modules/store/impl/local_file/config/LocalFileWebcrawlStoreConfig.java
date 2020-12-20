package org.alexside.webcrawl.modules.store.impl.local_file.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Data
@Configuration
@ConfigurationProperties("webcrawl.loader.store")
public class LocalFileWebcrawlStoreConfig {
    private Path dataDir;

    @PostConstruct
    public void init() throws Exception {
        Files.createDirectories(dataDir);
    }
}