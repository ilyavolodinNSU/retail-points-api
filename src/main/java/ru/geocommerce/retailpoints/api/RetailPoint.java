package ru.geocommerce.retailpoints.api;

public record RetailPoint(
    Double lat,
    Double lon,
    String name
) {}
