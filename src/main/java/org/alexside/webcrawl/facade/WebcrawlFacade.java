package org.alexside.webcrawl.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderRequest;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderResponse;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderStatus;
import org.alexside.webcrawl.modules.processor.WebcrawlProcessorAPI;
import org.alexside.webcrawl.modules.store.WebcrawlStoreAPI;
import org.alexside.webcrawl.modules.store.api.dto.WebcrawlStoreResponse;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebcrawlFacade {// TODO: Don't have enough time to store loading statuses in db
    private final Map<String, WebcrawlLoaderStatus> loadCache = new ConcurrentHashMap<>();
    private final WebcrawlStoreAPI storeAPI;
    private final WebcrawlProcessorAPI processorAPI;
    private final WebcrawlFacadeAsync facadeAsync;

    public Optional<WebcrawlLoaderResponse> preLoad(WebcrawlLoaderRequest req) {
        log.debug("Check and preload web data object..");
        if (storeAPI.getByPath(req.getPath()).isPresent()) {
            log.debug("Web data object {} already loaded.", req.getPath());
            loadCache.put(req.getPath(), FINISHED);
            return loaderResponse(req, FINISHED);
        }
        if (loadCache.getOrDefault(req.getPath(), null) == STARTED) {
            log.debug("Loading of web data object {} already started.", req.getPath());
            return loaderResponse(req);
        }// Start web data object loading asynchronously
        facadeAsync.load(req).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Error occurred when loading web data object: ", ex);
                loadCache.put(req.getPath(), FAILED);
            } else {
                log.info("Web data object is successfully loaded.");
                loadCache.put(req.getPath(), FINISHED);
            }
        });
        return loaderResponse(req);
    }

    public List<WebcrawlStoreResponse> getAll() { return storeAPI.getAll(); }

    public Collection<String> searchUrls(String path, String search) {
        Path webData = storeAPI.getByPath(path).orElseThrow();
        return processorAPI.searchUrls(webData, search);
    }

    public Optional<WebcrawlLoaderResponse> checkStatus(String path) {
        return Optional.ofNullable(WebcrawlLoaderResponse.builder()
                .path(path)
                .status(storeAPI.getByPath(path)
                        .map(p -> FINISHED)
                        .orElse(loadCache.getOrDefault(path, NOT_FOUND)))
                .build());
    }

    private Optional<WebcrawlLoaderResponse> loaderResponse(WebcrawlLoaderRequest req) {
        return loaderResponse(req, STARTED);
    }

    private Optional<WebcrawlLoaderResponse> loaderResponse(WebcrawlLoaderRequest req, WebcrawlLoaderStatus status) {
        return Optional.of(WebcrawlLoaderResponse.builder()
                .root(req.getRoot())
                .path(req.getPath())
                .status(status)
                .build());
    }
}