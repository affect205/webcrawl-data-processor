package org.alexside.webcrawl.modules.processor.impl.jwarc;

import lombok.extern.slf4j.Slf4j;
import org.alexside.webcrawl.modules.processor.WebcrawlProcessorAPI;
import org.alexside.webcrawl.modules.processor.api.error.WebcrawlProcessorException;
import org.netpreserve.jwarc.WarcConversion;
import org.netpreserve.jwarc.WarcPayload;
import org.netpreserve.jwarc.WarcReader;
import org.netpreserve.jwarc.WarcRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component("JWarcWebcrawlProcessorImpl")
public class JWarcWebcrawlProcessorImpl implements WebcrawlProcessorAPI {
    @Override
    public Collection<String> searchUrls(Path path, String search) throws WebcrawlProcessorException {
        Set<String> result = new HashSet<>();
        try (WarcReader reader = new WarcReader(FileChannel.open(path))) {
            for (WarcRecord record : reader) {
                if (record instanceof WarcConversion) {
                    WarcConversion conversion = (WarcConversion) record;
                    Optional<WarcPayload> payload = conversion.payload();
                    if (payload.isPresent()) {
                        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(payload.get().body().stream()))) {
                            if (reader1.lines().anyMatch(line -> line.contains(search))) {
                                result.add(conversion.target());
                            }
                        }
                    }
                }
            }
            log.debug("Search is done. Found {} pages matching the search phrase '{}'", result.size(), search);
            return result;
        } catch (IOException ex) {
            throw new WebcrawlProcessorException(ex);
        }
    }
}