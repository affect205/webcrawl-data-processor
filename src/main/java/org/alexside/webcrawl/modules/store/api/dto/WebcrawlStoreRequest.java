package org.alexside.webcrawl.modules.store.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.nio.ByteBuffer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WebcrawlStoreRequest {
    @NotBlank private String path;
    @JsonIgnore @NotBlank private ByteBuffer buffer;
    @JsonIgnore public String[] getPathSegments() { return path.split("/"); }
}