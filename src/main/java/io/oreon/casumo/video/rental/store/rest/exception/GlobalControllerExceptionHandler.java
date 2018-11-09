package io.oreon.casumo.video.rental.store.rest.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.badRequest;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Map<String, List<String>>> handleInvalidCustomerParameterException(APIException ex) {
        return badRequest().body(ex.getErrors());
    }
}
