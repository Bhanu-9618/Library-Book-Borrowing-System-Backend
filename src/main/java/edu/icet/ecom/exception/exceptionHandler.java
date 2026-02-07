package edu.icet.ecom.exception;

import edu.icet.ecom.model.dto.error.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class exceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> globalEx(RuntimeException exception){
        ErrorDto errorDto = new ErrorDto(exception.getMessage(),400);
        return ResponseEntity.badRequest().body(errorDto);
    }
}
