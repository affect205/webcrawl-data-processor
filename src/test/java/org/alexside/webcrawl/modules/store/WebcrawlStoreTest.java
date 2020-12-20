package org.alexside.webcrawl.modules.store;

import com.rainerhahnekamp.sneakythrow.Sneaky;
import lombok.extern.slf4j.Slf4j;
import org.alexside.webcrawl.modules.store.api.dto.WebcrawlStoreRequest;
import org.alexside.webcrawl.modules.store.api.dto.WebcrawlStoreResponse;
import org.alexside.webcrawl.modules.store.impl.local_file.config.LocalFileWebcrawlStoreConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class WebcrawlStoreTest {
    @Autowired
    private LocalFileWebcrawlStoreConfig storeConfig;

    @Autowired
    private WebcrawlStoreAPI storeAPI;

    @Value("classpath:test/loader/store/data/wet.paths.gz")
    private Path webDataPaths;

    @Test
    public void storeLocalFileTest() {
        log.debug("Start storeLocalFileTest..");
        WebcrawlStoreRequest req = givenStoreRequest();
        WebcrawlStoreResponse resp = storeAPI.store(req).orElseThrow();
        log.debug("Web data object path: {}", req.getPath());
        thenWebDataPathShouldBeEqualAndExists(req.getPath(), resp.getPath());
    }

    private WebcrawlStoreRequest givenStoreRequest() {
        return Sneaky.sneak(() -> WebcrawlStoreRequest.builder()
                .path("data/wet.paths.gz")
                .buffer(ByteBuffer.wrap(Files.readAllBytes(webDataPaths)))
                .build());
    }

    private void thenWebDataPathShouldBeEqualAndExists(String expectedPath, String actualPath) {
        assertNotNull(actualPath);
        assertEquals(expectedPath, actualPath);
        assertTrue(Files.exists(storeConfig.getDataDir().resolve(actualPath)));
    }
}