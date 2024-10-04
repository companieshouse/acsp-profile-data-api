package uk.gov.companieshouse.acspprofile.api.controller;

import static uk.gov.companieshouse.acspprofile.api.AcspProfileApplication.NAMESPACE;

import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.gov.companieshouse.acspprofile.api.exception.BadGatewayException;
import uk.gov.companieshouse.acspprofile.api.exception.ConflictException;
import uk.gov.companieshouse.acspprofile.api.exception.NotFoundException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFound() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Void> handleConflict() {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(BadGatewayException.class)
    public ResponseEntity<Void> handleBadGateway(BadGatewayException ex) {
        LOGGER.info("Recoverable exception: %s".formatted(Arrays.toString(ex.getStackTrace())),
                DataMapHolder.getLogMap());
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOGGER.error("Invalid request body", ex, DataMapHolder.getLogMap());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleUnknownException(Exception ex) {
        LOGGER.error(ex.getClass().getName(), ex, DataMapHolder.getLogMap());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
