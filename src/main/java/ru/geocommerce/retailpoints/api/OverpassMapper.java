package ru.geocommerce.retailpoints.api;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OverpassMapper {

    @Mapping(target = "name", expression = "java(element.tags().getOrDefault(\"name\", \"\"))")
    RetailPoint toRetailPoint(Element element);
    
    List<RetailPoint> toRetailPoints(List<Element> elements);
}


