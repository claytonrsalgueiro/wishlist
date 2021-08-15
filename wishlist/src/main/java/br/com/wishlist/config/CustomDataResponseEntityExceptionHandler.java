/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.config;

import br.com.wishlist.domain.commons.exception.ApiError;
import br.com.wishlist.domain.commons.exception.ErrorMessageEnum;
import br.com.wishlist.domain.commons.exception.InvalidParameterException;
import br.com.wishlist.domain.commons.exception.PreConditionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author clayton.salgueiro
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomDataResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(final Exception ex, final WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiError(null, "Access denied!"));
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ApiError> handlerInvalidParameterException(final InvalidParameterException ex) {
        final String message = ex.getMessage();
        return ResponseEntity.badRequest().body(new ApiError(message, message, ex.getErrors()));
    }

    @ExceptionHandler(PreConditionException.class)
    public ResponseEntity<ApiError> handlerPreConditionException(final PreConditionException ex) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new ApiError(null, ex.getMessage()));
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(final DataIntegrityViolationException ex,
            final WebRequest request) {
        String error = "Unknown error message of database integrity violation.";
        log.error(ex.getLocalizedMessage(), ex);
        final String rootCauseMessage = ex.getRootCause().getMessage();
        if (rootCauseMessage.contains(ErrorMessageEnum.DUPLICATED.getError())) {
            error = ErrorMessageEnum.DUPLICATED.getMessage();

        } else if (rootCauseMessage.contains(ErrorMessageEnum.DELETE.getError())) {
            error = ErrorMessageEnum.DELETE.getMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(null, error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(null, ex.getLocalizedMessage()));
    }
}
