package ru.petrov.supplier.model;

import java.net.URI;

public record Supplier(String id, String baseUrl, String apiKey) {

    public Supplier {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Supplier id must not be blank");
        }
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("Supplier baseUrl must not be blank");
        }
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Supplier apiKey must not be blank");
        }

        String normalizedBaseUrl = baseUrl.trim().replaceAll("/+$", "");
        URI uri;
        try {
            uri = URI.create(normalizedBaseUrl);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid supplier baseUrl: " + baseUrl, exception);
        }
        if (!uri.isAbsolute() || uri.getHost() == null) {
            throw new IllegalArgumentException("Supplier baseUrl must be an absolute HTTP(S) URL");
        }
        String scheme = uri.getScheme();
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("Supplier baseUrl must use HTTP or HTTPS");
        }

        baseUrl = normalizedBaseUrl;
    }
}
