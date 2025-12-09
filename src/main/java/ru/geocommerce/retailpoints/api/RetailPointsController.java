package ru.geocommerce.retailpoints.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RetailPointsController {
	private final OverpassApiService overpassApiService;
	private final OverpassMapper mapper;

	@GetMapping
	public List<RetailPoint> run(@ModelAttribute Request req) {
		return mapper.toRetailPoints(overpassApiService.fetchPoints(req).elements());
	}
}