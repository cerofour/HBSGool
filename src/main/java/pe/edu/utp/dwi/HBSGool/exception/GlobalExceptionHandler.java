package pe.edu.utp.dwi.HBSGool.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 📘 DTO simple para respuesta uniforme
    private record ApiError(String message, int status, LocalDateTime timestamp) {}

    // 📌 1️⃣ Validaciones de DTOs (anotaciones @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 📌 2️⃣ Tipos inválidos en query params o path variables
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var error = new ApiError("Parámetro inválido: " + ex.getName(), 400, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 📌 3️⃣ No encontrado (404)
    @ExceptionHandler({
            CashierNotFoundException.class,
            ReservationNotFoundException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        var error = new ApiError(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 📌 4️⃣ Regla de negocio o conflicto (400)
    @ExceptionHandler({
            ReservationOverlapException.class,
            SesionCajeroException.class,
            UserIsNotCashierException.class
    })
    public ResponseEntity<ApiError> handleBusinessErrors(RuntimeException ex) {
        var error = new ApiError(ex.getMessage(), 400, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 📌 5️⃣ Errores de autenticación (401)
    @ExceptionHandler({
            UnauthenticatedException.class,
            NoCashierLoggedInException.class
    })
    public ResponseEntity<ApiError> handleAuthErrors(RuntimeException ex) {
        var error = new ApiError(ex.getMessage(), 401, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 📌 6️⃣ Cualquier otro error no controlado (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        var error = new ApiError("Error inesperado: " + ex.getMessage(), 500, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

