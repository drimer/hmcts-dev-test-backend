package uk.gov.hmcts.reform.dev.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ValidationHandlerTest {

    @Test
    void testThatReturnsBadRequestWithFieldErrors() {
        ValidationHandler handler = new ValidationHandler();

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "field1", "must not be null"));
        bindingResult.addError(new FieldError("objectName", "field2", "must be positive"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        HttpHeaders headers = new HttpHeaders();
        WebRequest request = mock(WebRequest.class);

        var response = handler.handleMethodArgumentNotValid(ex, headers, HttpStatus.BAD_REQUEST, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> errors = (Map<?, ?>) response.getBody();
        assertEquals(2, errors.size());
        assertEquals("must not be null", errors.get("field1"));
        assertEquals("must be positive", errors.get("field2"));
    }

    @Test
    void testThatReturnsEmptyMapWhenNoFieldErrors() {
        ValidationHandler handler = new ValidationHandler();

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        HttpHeaders headers = new HttpHeaders();
        WebRequest request = mock(WebRequest.class);

        var response = handler.handleMethodArgumentNotValid(ex, headers, HttpStatus.BAD_REQUEST, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> errors = (Map<?, ?>) response.getBody();
        assertTrue(errors.isEmpty());
    }
}


