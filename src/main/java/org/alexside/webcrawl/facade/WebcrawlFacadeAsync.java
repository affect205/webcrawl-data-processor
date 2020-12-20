package org.alexside.webcrawl.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexside.webcrawl.modules.loader.WebcrawlLoaderAPI;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderRequest;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderResponse;
import org.alexside.webcrawl.modules.store.WebcrawlStoreAPI;
import org.alexside.webcrawl.modules.store.api.dto.WebcrawlStoreRequest;
import org.alexside.webcrawl.modules.store.api.dto.WebcrawlStoreResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.alexside.webcrawl.config.AsyncConfig.ASYNC_TASK_EXECUTOR;
import static org.alexside.webcrawl.utils.JsonUtils.prettyJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebcrawlFacadeAsync {
    private final WebcrawlLoaderAPI loaderAPI;
    private final WebcrawlStoreAPI storeAPI;

    @Async(ASYNC_TASK_EXECUTOR)
    public CompletableFuture<Optional<WebcrawlStoreResponse>> load(WebcrawlLoaderRequest req) {
        log.debug("Load web data object..:\n{}", prettyJson(req));
        WebcrawlLoaderResponse loaderResp = loaderAPI.load(req).orElseThrow();
        return CompletableFuture.completedFuture(storeAPI.store(
                WebcrawlStoreRequest.builder()
                        .path(loaderResp.getPath())
                        .buffer(loaderResp.getDataBuffer())
                        .build()));
    }
}