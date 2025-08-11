package exception;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> errores = new ArrayList<String>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError fe = fieldErrors.get(i);
            errores.add(fe.getField() + ": " + fe.getDefaultMessage());
        }
        StandardError body = StandardError.of(HttpStatus.BAD_REQUEST.value(), "Validación fallida", join(errores), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        StandardError body = StandardError.of(HttpStatus.UNAUTHORIZED.value(), "Credenciales inválidas", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        StandardError body = StandardError.of(HttpStatus.FORBIDDEN.value(), "Acceso denegado", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        StandardError body = StandardError.of(HttpStatus.BAD_REQUEST.value(), "Petición inválida", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGeneric(Exception ex, HttpServletRequest req) {
        StandardError body = StandardError.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private String join(List<String> textos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textos.size(); i++) {
            if (i > 0) sb.append("; ");
            sb.append(textos.get(i));
        }
        return sb.toString();
    }

    public static class StandardError {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        public static StandardError of(int status, String error, String message, String path) {
            StandardError se = new StandardError();
            se.timestamp = LocalDateTime.now();
            se.status = status;
            se.error = error;
            se.message = message;
            se.path = path;
            return se;
        }

        public LocalDateTime getTimestamp() { return timestamp; }
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
        public String getPath() { return path; }
    }
}
