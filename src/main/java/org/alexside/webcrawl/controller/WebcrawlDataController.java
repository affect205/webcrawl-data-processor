package org.alexside.webcrawl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexside.webcrawl.facade.WebcrawlFacade;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderRequest;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderResponse;
import org.alexside.webcrawl.modules.store.api.dto.WebcrawlStoreResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WebcrawlDataController {
    private final WebcrawlFacade facade;

    @PutMapping("/loader")
    public ResponseEntity<WebcrawlLoaderResponse> load(@Valid WebcrawlLoaderRequest req) {
        return facade.preLoad(req).map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @GetMapping("/loader/status")
    public ResponseEntity<WebcrawlLoaderResponse> checkStatus(
            @RequestParam("path") String path) {
        return facade.checkStatus(path).map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @GetMapping("/store")
    public ResponseEntity<List<WebcrawlStoreResponse>> getAll() {
        List<WebcrawlStoreResponse> result = facade.getAll();
        return result.isEmpty() ? notFound().build() : ok(result);
    }

    @GetMapping("/store/search")
    public ResponseEntity<Collection<String>> search(
            @RequestParam("path") String path,
            @RequestParam("search") String search) {
        Collection<String> result = facade.searchUrls(path, search);
        return result.isEmpty() ? notFound().build() : ok(result);
    }
}