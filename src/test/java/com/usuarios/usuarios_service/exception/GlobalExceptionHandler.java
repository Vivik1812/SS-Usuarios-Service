package com.usuarios.usuarios_service.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void manejarRuntimeException_DeberiaRetornarBadRequest() {

        RuntimeException ex =
                new RuntimeException("Usuario no encontrado");

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/v1/usuarios/1");

        ResponseEntity<ErrorResponse> response =
                handler.manejarRuntimeException(
                        ex,
                        request);

        assertEquals(400, response.getStatusCode().value());

        ErrorResponse body = response.getBody();

        assertNotNull(body);

        assertEquals(
                "Bad Request",
                body.getError());

        assertEquals(
                "Usuario no encontrado",
                body.getMensaje());

        assertEquals(
                "/api/v1/usuarios/1",
                body.getPath());

        assertEquals(
                400,
                body.getStatus());

        assertNotNull(body.getFecha());
    }
}