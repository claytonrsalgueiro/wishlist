/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.config;

import br.com.wishlist.domain.commons.exception.ApiError;
import br.com.wishlist.domain.commons.exception.ApiInnerError;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author clayton.salgueiro
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(null, "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(null, "Could not read data type. Please check the data types"));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final List<ApiInnerError> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            final String code = error.getDefaultMessage();
            final ApiInnerError innerErr = new ApiInnerError(error.getField(), code, code);
            errors.add(innerErr);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(null, "Field validation errors", errors));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ApiError(null, ex.getMethod() + " method is not supported for this request."));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ApiError(null, ex.getContentType() + " media type is not supported."));
    }
}
