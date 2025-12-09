package ru.geocommerce.retailpoints.api;

import java.util.List;
import java.util.Map;

public record OverpassResponse(List<Element> elements) {}

record Element(
    Double lat,
    Double lon,
    Map<String, String> tags
) {}

