package org.alexside.webcrawl.modules.loader.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebcrawlLoaderResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String root;
    private String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder.Default private WebcrawlLoaderStatus status = WebcrawlLoaderStatus.STARTED;
    @JsonIgnore private ByteBuffer dataBuffer;
}