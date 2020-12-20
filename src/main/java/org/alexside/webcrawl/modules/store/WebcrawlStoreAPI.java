package org.alexside.webcrawl.modules.store;

import org.alexside.webcrawl.modules.store.api.dto.WebcrawlStoreRequest;
import org.alexside.webcrawl.modules.store.api.dto.WebcrawlStoreResponse;
import org.alexside.webcrawl.modules.store.api.error.WebcrawlStoreException;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface WebcrawlStoreAPI {
    Optional<WebcrawlStoreResponse> store(WebcrawlStoreRequest req) throws WebcrawlStoreException;
    List<WebcrawlStoreResponse> getAll() throws WebcrawlStoreException;
    Optional<Path> getByPath(String path) throws WebcrawlStoreException;
}
