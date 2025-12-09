package ru.geocommerce.retailpoints.api;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OverpassApiService {
    private final WebClient webClient;

    private final String query = """
            [out:json][timeout:180];

            (
              node["shop"="clothes"](%f,%f,%f,%f);
              way["shop"="clothes"](%f,%f,%f,%f);
            );

            out center tags;
            """;

    private final String uri = "https://overpass-api.de/api/interpreter";

    public OverpassResponse fetchPoints(Request req) {
        String data = String.format(
            Locale.US,
            query,
            req.latMin(), req.lonMin(), req.latMax(), req.lonMax(),
            req.latMin(), req.lonMin(), req.latMax(), req.lonMax()
        );

        String encodedData = URLEncoder.encode(data, StandardCharsets.UTF_8);

        return this.webClient.post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue("data=" + encodedData) // без лишнего "?"!
            .retrieve()
            .bodyToMono(OverpassResponse.class)
            .block();
    }
}
