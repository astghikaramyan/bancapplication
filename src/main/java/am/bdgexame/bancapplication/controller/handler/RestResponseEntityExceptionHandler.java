package am.bdgexame.bancapplication.controller.handler;

import am.bdgexame.bancapplication.exceptions.BadRequestException;
import am.bdgexame.bancapplication.exceptions.ConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream().collect(Collectors.toMap(FieldError::getField,
                        FieldError::getDefaultMessage));
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(value = Throwable.class)
    protected ResponseEntity<Object> handleThrowable(Throwable ex) {
        logger.error("ERROR:", ex);
        return new ResponseEntity<>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(Throwable ex) {
        logger.error("ERROR:", ex);
        return new ResponseEntity<>(new HashMap<String, String>() {{
            put("error", ex.getMessage());
        }}, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(Throwable ex) {
        logger.error("ERROR:", ex);
        return new ResponseEntity<>(new HashMap<String, String>() {{
            put("error", ex.getMessage());
        }}, HttpStatus.BAD_REQUEST);
    }
}
