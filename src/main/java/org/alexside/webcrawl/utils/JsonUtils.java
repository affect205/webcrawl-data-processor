package org.alexside.webcrawl.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String prettyJson(Object o) {
        try {
            return o == null ? null : mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String json(Object o) {
        try {
            return o == null ? null : mapper.writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static synchronized <DTO> DTO readJson(File jsonFile, Class<DTO> clazz) throws UncheckedIOException {
        try {
            return mapper.readValue(jsonFile, clazz);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}