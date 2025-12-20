# Retail Points API

Сервис для поиска точек ритейла (магазинов, кафе и др.) по категории и области.

## Эндпоинты

### Получить категории
```http
GET /api/categories
```
Возвращает список доступных категорий:
```json
[
  { "key": "supermarket", "name": "Супермаркет" },
  { "key": "clothing", "name": "Одежда" },
  ...
]
```

### Найти точки ритейла
```http
GET /api/points?category=supermarket&latMin=50&lonMin=802&latMax=55&lonMax=85
```
Параметры:
- `category` — ключ из `/api/categories`
- `latMin`, `lonMin`, `latMax`, `lonMax` — границы области поиска

Ответ:
```json
{
  "retailPoints": [
    { "lat": 55.5555, "lon": 82.2222, "name": "Ашан" },
    ...
  ]
}
```

## Запуск
```bash
./gradlew bootRun
```
Сервис доступен на `http://localhost:8080`