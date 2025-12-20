package ru.geocommerce.retailpoints.api.dto;

public record RetailPoint(
    Double lat,
    Double lon,
    String name
) {}
