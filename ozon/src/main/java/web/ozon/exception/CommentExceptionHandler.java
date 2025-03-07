package web.ozon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class CommentExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity.badRequest().body("Null value encountered");
    }

    @ExceptionHandler(NullAuthorIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNullAuthorIdException(NullAuthorIdException ex) {
        return ResponseEntity.badRequest().body("Author ID cannot be null");
    }

    @ExceptionHandler(NullProductIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNullProductIdException(NullProductIdException ex) {
        return ResponseEntity.badRequest().body("Product ID cannot be null");
    }

    @ExceptionHandler(NonNullNewIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNonNullNewIdException(NonNullNewIdException ex) {
        return ResponseEntity.badRequest().body("New ID must be null for a new comment");
    }

    @ExceptionHandler(NullContentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNullContentException(NullContentException ex) {
        return ResponseEntity.badRequest().body("Comment content cannot be null");
    }

    @ExceptionHandler(NullAnonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNullAnonException(NullAnonException ex) {
        return ResponseEntity.badRequest().body("Anonymous flag cannot be null");
    }

    @ExceptionHandler(NotSameAuthorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleNotSameAuthorException(NotSameAuthorException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Author mismatch");
    }

    @ExceptionHandler(CommentNotNewException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleCommentNotNewException(CommentNotNewException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("There is an existing comment");
    }

    @ExceptionHandler(CommentNotExistException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleCommentNotExistException(CommentNotExistException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No such comment");
    }

    @ExceptionHandler(ProductNotBoughtException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleProductNotBoughtException(ProductNotBoughtException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Product was not bought");
    }

    @ExceptionHandler(RudeTextException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleRudeTextException(RudeTextException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Comment content is rude");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleGenericException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("An error occurred");
    }
}
