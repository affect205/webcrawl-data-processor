package org.alexside.webcrawl.modules.store.impl.local_file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexside.webcrawl.modules.store.WebcrawlStoreAPI;
import org.alexside.webcrawl.modules.store.api.dto.WebcrawlStoreRequest;
import org.alexside.webcrawl.modules.store.api.dto.WebcrawlStoreResponse;
import org.alexside.webcrawl.modules.store.api.error.WebcrawlStoreException;
import org.alexside.webcrawl.modules.store.impl.local_file.config.LocalFileWebcrawlStoreConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.Optional.of;
import static org.alexside.webcrawl.utils.JsonUtils.prettyJson;

@Slf4j
@Component("LocalFileWebcrawlStoreImpl")
@RequiredArgsConstructor
public class LocalFileWebcrawlStoreImpl implements WebcrawlStoreAPI {
    private final LocalFileWebcrawlStoreConfig config;
    @Override
    public Optional<WebcrawlStoreResponse> store(WebcrawlStoreRequest req) throws WebcrawlStoreException {
        log.debug("Store web data object:\n{}", prettyJson(req));
        Path dataPath = createDataPath(req);
        if (Files.exists(dataPath)) {
            log.debug("Web data object {} already exists.", dataPath);
            return of(WebcrawlStoreResponse.builder()
                    .path(Objects.toString(config.getDataDir().relativize(dataPath)))
                    .build());
        }
        try {
            if (Files.notExists(dataPath.getParent())) {
                Files.createDirectories(dataPath.getParent());
            }
            Files.write(Files.createFile(dataPath), req.getBuffer().array(), TRUNCATE_EXISTING);
            log.info("Web data object is stored successfully.");
            return of(WebcrawlStoreResponse.builder()
                    .path(Objects.toString(config.getDataDir().relativize(dataPath)))
                    .build());
        } catch (IOException ex) {
            throw new WebcrawlStoreException(ex);
        }
    }

    @Override
    public List<WebcrawlStoreResponse> getAll() throws WebcrawlStoreException {
        try {
            return Files.walk(config.getDataDir())
                    .filter(Files::exists)
                    .filter(Files::isRegularFile)
                    .map(path -> Objects.toString(config.getDataDir().relativize(path)))
                    .map(WebcrawlStoreResponse::new)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            throw new WebcrawlStoreException(ex);
        }
    }

    @Override
    public Optional<Path> getByPath(String path) throws WebcrawlStoreException {
        return getAll().stream().filter(resp -> Objects.equals(resp.getPath(), path))
                .findAny()
                .map(WebcrawlStoreResponse::getPath)
                .map(p -> config.getDataDir().resolve(p));
    }

    private Path createDataPath(WebcrawlStoreRequest req) {
        return Paths.get(Objects.toString(config.getDataDir()), req.getPathSegments());
    }
}