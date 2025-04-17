package com.bookstore.api.exception.mapper;
import com.bookstore.api.exception.OutOfStockException;

import com.bookstore.api.exception.BookNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;


/**
 * Exception mapper for OutOfStockException.
 * Converts the exception to a JSON response with a 400 status code.
 */
@Provider
public class OutOfStockExceptionMapper implements ExceptionMapper<OutOfStockException> {
    @Override
    public Response toResponse(OutOfStockException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Out of Stock");
        error.put("message", exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}