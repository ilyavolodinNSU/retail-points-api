package ru.geocommerce.retailpoints.api.services.overpass.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.geocommerce.retailpoints.api.dto.RetailPoint;
import ru.geocommerce.retailpoints.api.services.overpass.dto.Element;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OverpassMapper {

    @Mapping(
        target = "name",
        expression = "java(element.tags().getOrDefault(\"name\", \"\"))"
    )
    RetailPoint toRetailPoint(Element element);

    List<RetailPoint> toRetailPoints(List<Element> elements);
}