package edu.icet.ecom.exception;

import edu.icet.ecom.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardResponse> handleNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(
                new StandardResponse(404, "Not Found", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardResponse> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(
                new StandardResponse(403, "Access Denied", "You don't have permission for this action!"),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse> handleGeneralException(Exception e) {
        return new ResponseEntity<>(
                new StandardResponse(500, "Internal Server Error", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse> handleValidationExceptions(org.springframework.web.bind.MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return new ResponseEntity<>(
                new StandardResponse(400, "Validation Failed", errorMessage),
                HttpStatus.BAD_REQUEST
        );
    }
}