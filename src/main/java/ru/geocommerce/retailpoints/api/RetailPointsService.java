package ru.geocommerce.retailpoints.api;

import ru.geocommerce.retailpoints.api.dto.Request;
import ru.geocommerce.retailpoints.api.dto.Response;

public interface RetailPointsService {
    public Response fetchPoints(Request req);
}
