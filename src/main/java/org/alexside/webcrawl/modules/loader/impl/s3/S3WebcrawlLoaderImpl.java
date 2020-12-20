package org.alexside.webcrawl.modules.loader.impl.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexside.webcrawl.modules.loader.WebcrawlLoaderAPI;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderRequest;
import org.alexside.webcrawl.modules.loader.api.dto.WebcrawlLoaderResponse;
import org.alexside.webcrawl.modules.loader.api.error.WebcrawlLoaderException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

import static com.amazonaws.util.IOUtils.toByteArray;
import static java.util.Optional.ofNullable;
import static org.alexside.webcrawl.utils.JsonUtils.prettyJson;
import static org.jooq.lambda.Unchecked.consumer;

@Slf4j
@Component("S3WebcrawlLoaderImpl")
@RequiredArgsConstructor
public class S3WebcrawlLoaderImpl implements WebcrawlLoaderAPI {
    private final AmazonS3 amazonS3;

    @Override
    public Optional<WebcrawlLoaderResponse> load(WebcrawlLoaderRequest req) throws WebcrawlLoaderException {
        log.info("Loading a web data object from:\n{}", prettyJson(req));
        final S3Object s3Object = amazonS3.getObject(req.getRoot(), req.getPath());
        try (S3ObjectInputStream stream = s3Object.getObjectContent()) {
            log.info("Web data object is loaded successfully.");
            return Optional.of(WebcrawlLoaderResponse.builder()
                    .root(req.getRoot())
                    .path(req.getPath())
                    .dataBuffer(ByteBuffer.wrap(toByteArray(stream)))
                    .build());
        } catch(final IOException ex) {
            log.error("Error occurred when loading web data object:", ex);
            throw new WebcrawlLoaderException(ex);
        } finally {
            ofNullable(s3Object).ifPresent(consumer(S3Object::close, WebcrawlLoaderException::new));
        }
    }
}