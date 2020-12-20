package org.alexside.webcrawl.modules.processor;

import lombok.extern.slf4j.Slf4j;
import org.alexside.webcrawl.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(classes = {Application.class})
class WebCrawlProcessorTest {
	@Autowired
	private WebcrawlProcessorAPI processorAPI;

	@Value("classpath:/test/loader/store/data/internal.warc.wet.gz")
	private Path smallWarc;

	@Test
	void contextLoads() {
		assertTrue(Files.exists(smallWarc));
		Collection<String> result = processorAPI.searchUrls(smallWarc, "Music");
		assertFalse(result.isEmpty());
	}
}