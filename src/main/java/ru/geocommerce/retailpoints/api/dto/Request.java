package ru.geocommerce.retailpoints.api.dto;

public record Request(
    String category,
    Double latMin,
    Double lonMin,
    Double latMax,
    Double lonMax
) {}
