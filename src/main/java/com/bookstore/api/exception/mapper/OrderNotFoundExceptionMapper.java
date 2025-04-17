package com.bookstore.api.exception.mapper;


import com.bookstore.api.exception.OrderNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Exception mapper for OrderNotFoundException.
 * Converts the exception to a JSON response with a 404 status code.
 */
@Provider
public class OrderNotFoundExceptionMapper implements ExceptionMapper<OrderNotFoundException> {
    @Override
    public Response toResponse(OrderNotFoundException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Book Not Found");
        error.put("message", exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
