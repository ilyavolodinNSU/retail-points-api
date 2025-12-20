package ru.geocommerce.retailpoints.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import ru.geocommerce.retailpoints.api.config.dto.Category;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

@Configuration
public class CategoryConfig {
    @Bean
    public Map<String, Category> categoryMap(ObjectMapper objectMapper) throws Exception {
        InputStream jsonStream = new ClassPathResource("category_config.json").getInputStream();
        return objectMapper.readValue(jsonStream, new TypeReference<Map<String, Category>>() {});
    }
}