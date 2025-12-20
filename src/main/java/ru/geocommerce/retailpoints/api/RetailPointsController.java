package ru.geocommerce.retailpoints.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.geocommerce.retailpoints.api.config.dto.Category;
import ru.geocommerce.retailpoints.api.dto.CategoryDto;
import ru.geocommerce.retailpoints.api.dto.Request;
import ru.geocommerce.retailpoints.api.dto.Response;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RetailPointsController {
	private final RetailPointsService retailPointsService;
	private final Map<String, Category> categoryMap;

	@GetMapping("/points")
	public Response run(@ModelAttribute Request req) {
		return retailPointsService.fetchPoints(req);
	}

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return categoryMap.entrySet().stream()
            .map(entry -> new CategoryDto(
                entry.getKey(),
                entry.getValue().name()
            ))
            .collect(Collectors.toList());
    }
}