package ru.geocommerce.retailpoints.api.config.dto;

import java.util.List;

public record Category(String name, List<CategoryTag> tags) {}
