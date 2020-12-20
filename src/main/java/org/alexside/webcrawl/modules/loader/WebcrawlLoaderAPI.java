package org.alexside.webcrawl.modules.loader;

import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderRequest;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderResponse;
import org.alexside.webcrawl.modules.loader.api.error.WebcrawlLoaderException;

import java.util.Optional;

public interface WebcrawlLoaderAPI {
    Optional<WebcrawlLoaderResponse> load(WebcrawlLoaderRequest req) throws WebcrawlLoaderException;

}
