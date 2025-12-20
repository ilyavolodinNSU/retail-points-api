// ru.geocommerce.retailpoints.api.services.overpass.OverpassApiService.java
package ru.geocommerce.retailpoints.api.services.overpass;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.geocommerce.retailpoints.api.RetailPointsService;
import ru.geocommerce.retailpoints.api.config.dto.Category;
import ru.geocommerce.retailpoints.api.dto.Request;
import ru.geocommerce.retailpoints.api.dto.Response;
import ru.geocommerce.retailpoints.api.dto.RetailPoint;
import ru.geocommerce.retailpoints.api.services.overpass.dto.OverpassResponse;
import ru.geocommerce.retailpoints.api.services.overpass.mapper.OverpassMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Profile("overpass")
@RequiredArgsConstructor
public class OverpassApiService implements RetailPointsService {

    private static final Logger log = LoggerFactory.getLogger(OverpassApiService.class);

    private final WebClient webClient;
    private final OverpassMapper mapper;
    private final Map<String, Category> categoryMap;

    private static final String BASE_QUERY_TEMPLATE = "[out:json][timeout:180];(%s);out center tags;";

    @Override
    public Response fetchPoints(Request req) {
        log.info("Запрос точек: category={}, bbox=[{},{},{},{}]",
            req.category(), req.latMin(), req.lonMin(), req.latMax(), req.lonMax());

        String categoryKey = req.category();
        if (categoryKey == null || categoryKey.isBlank()) {
            log.warn("Категория не указана — возвращаем пустой результат");
            return new Response(List.of());
        }

        Category category = categoryMap.get(categoryKey);
        if (category == null) {
            log.warn("Неизвестная категория '{}' — возвращаем пустой результат", categoryKey);
            return new Response(List.of());
        }

        // Валидация bbox (опционально)
        if (req.latMin() == null || req.lonMin() == null || req.latMax() == null || req.lonMax() == null) {
            log.warn("Некорректный bounding box — возвращаем пустой результат");
            return new Response(List.of());
        }

        String bbox = String.format(
            Locale.US,
            "%f,%f,%f,%f",
            req.latMin(), req.lonMin(), req.latMax(), req.lonMax()
        );

        String queryPart = category.tags().stream()
            .flatMap(tag -> tag.values().stream().map(value -> {
                String kv = String.format("[\"%s\"=\"%s\"]", tag.key(), value);
                return String.format("node%s(%s); way%s(%s);", kv, bbox, kv, bbox);
            }))
            .collect(Collectors.joining(" "));

        if (queryPart.isEmpty()) {
            log.warn("Нет тегов для категории '{}' — возвращаем пустой результат", categoryKey);
            return new Response(List.of());
        }

        String fullQuery = String.format(BASE_QUERY_TEMPLATE, queryPart);
        log.debug("Сформирован Overpass QL:\n{}", fullQuery);

        String encodedData = URLEncoder.encode(fullQuery, StandardCharsets.UTF_8);

        log.info("Отправка запроса к Overpass API...");
        OverpassResponse overpassResp = webClient.post()
            .uri("https://overpass-api.de/api/interpreter")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue("data=" + encodedData)
            .retrieve()
            .bodyToMono(OverpassResponse.class)
            .doOnSuccess(resp -> {
                int count = (resp != null && resp.elements() != null) ? resp.elements().size() : 0;
                log.info("Получено {} элементов от Overpass API", count);
            })
            .onErrorResume(throwable -> {
                log.error("Ошибка при вызове Overpass API", throwable);
                return Mono.just(new OverpassResponse(List.of()));
            })
            .block();

        List<RetailPoint> retailPoints = overpassResp != null && overpassResp.elements() != null
            ? mapper.toRetailPoints(overpassResp.elements())
            : List.of();

        log.info("Возвращено {} retail points", retailPoints.size());
        return new Response(retailPoints);
    }
}