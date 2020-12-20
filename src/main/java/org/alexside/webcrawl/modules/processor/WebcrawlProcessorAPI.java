package org.alexside.webcrawl.modules.processor;

import org.alexside.webcrawl.modules.processor.api.error.WebcrawlProcessorException;

import java.nio.file.Path;
import java.util.Collection;

public interface WebcrawlProcessorAPI {
    Collection<String> searchUrls(Path path, String searchPhrase) throws WebcrawlProcessorException;
}