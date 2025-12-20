package ru.geocommerce.retailpoints.api.services.overpass.dto;

import java.util.Map;

public record Element(
    Double lat,
    Double lon,
    Map<String, String> tags
) {}
