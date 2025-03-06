package web.ozon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity.badRequest().body("Null value encountered: " + ex.getMessage());
    }

    @ExceptionHandler(NullAuthorIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNullAuthorIdException(NullAuthorIdException ex) {
        return ResponseEntity.badRequest().body("Author ID cannot be null: " + ex.getMessage());
    }

    @ExceptionHandler(NullProductIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNullProductIdException(NullProductIdException ex) {
        return ResponseEntity.badRequest().body("Product ID cannot be null: " + ex.getMessage());
    }

    @ExceptionHandler(NonNullNewIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNonNullNewIdException(NonNullNewIdException ex) {
        return ResponseEntity.badRequest().body("New ID must be null for a new comment: " + ex.getMessage());
    }

    @ExceptionHandler(NullContentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNullContentException(NullContentException ex) {
        return ResponseEntity.badRequest().body("Comment content cannot be null: " + ex.getMessage());
    }

    @ExceptionHandler(NullAnonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNullAnonException(NullAnonException ex) {
        return ResponseEntity.badRequest().body("Anonymous flag cannot be null: " + ex.getMessage());
    }

    @ExceptionHandler(NotSameAuthorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleNotSameAuthorException(NotSameAuthorException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Author mismatch: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
