package org.alexside.webcrawl.modules.loader;

import lombok.extern.slf4j.Slf4j;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderRequest;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderResponse;
import org.alexside.webcrawl.modules.store.impl.local_file.config.LocalFileWebcrawlStoreConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;

import static org.alexside.webcrawl.utils.JsonUtils.readJson;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class WebcrawlLoaderTest {
    @Autowired
    private LocalFileWebcrawlStoreConfig storeConfig;

    @Autowired
    private WebcrawlLoaderAPI loaderAPI;

    @Value("classpath:test/loader/loader_request.json")
    private File loaderRequestRsrc;

    @Test
    public void loadLocalDataFileTest() {
        log.debug("Start loadLocalDataFileTest..");
        WebcrawlLoaderRequest req = givenLoaderRequest();
        WebcrawlLoaderResponse resp = loaderAPI.load(req).orElseThrow();
        log.debug("Web data object path: {}", resp.getPath());
        thenWebDataPathShouldBeEqualAndExists(req.getPath(), resp.getPath());
    }

    private WebcrawlLoaderRequest givenLoaderRequest() {
        return readJson(loaderRequestRsrc, WebcrawlLoaderRequest.class);
    }

    private void thenWebDataPathShouldBeEqualAndExists(String expectedPath, String actualPath) {
        assertNotNull(actualPath);
        assertEquals(expectedPath, actualPath);
        assertTrue(Files.exists(storeConfig.getDataDir().resolve(actualPath)));
    }
}