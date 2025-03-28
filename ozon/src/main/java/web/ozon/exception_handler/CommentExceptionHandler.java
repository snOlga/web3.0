package web.ozon.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import web.ozon.DTO.ExceptionDTO;
import web.ozon.exception.*;

import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class CommentExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Null value encountered"));
    }

    @ExceptionHandler(NullAuthorIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleNullAuthorIdException(NullAuthorIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Author ID cannot be null"));
    }

    @ExceptionHandler(NullProductIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleNullProductIdException(NullProductIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Product ID cannot be null"));
    }

    @ExceptionHandler(NonNullNewIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleNonNullNewIdException(NonNullNewIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO("New ID must be null for a new comment"));
    }

    @ExceptionHandler(NullContentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleNullContentException(NullContentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Comment content cannot be null"));
    }

    @ExceptionHandler(NullAnonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleNullAnonException(NullAnonException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Anonymous flag cannot be null"));
    }

    @ExceptionHandler(NotSameAuthorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionDTO> handleNotSameAuthorException(NotSameAuthorException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionDTO("Author mismatch"));
    }

    @ExceptionHandler(CommentNotNewException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionDTO> handleCommentNotNewException(CommentNotNewException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionDTO("There is an existing comment"));
    }

    @ExceptionHandler(CommentNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDTO> handleCommentNotExistException(CommentNotExistException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionDTO("No such comment"));
    }

    @ExceptionHandler(ProductNotBoughtException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionDTO> handleProductNotBoughtException(ProductNotBoughtException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionDTO("Product was not bought"));
    }

    @ExceptionHandler(RudeTextException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionDTO> handleRudeTextException(RudeTextException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionDTO("Comment content is rude"));
    }

    @ExceptionHandler(ContentTooLongException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionDTO> handleRudeTextException(ContentTooLongException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionDTO("Comment content is too long"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDTO> handleGenericException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO("Something wrong..."));
    }

    @ExceptionHandler(NullReasonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleNullReasonException(NullReasonException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Reason cannot be null"));
    }

    @ExceptionHandler(NullCommentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleNullCommentException(NullCommentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("Comment cannot be null"));
    }

    @ExceptionHandler(ReporterIsAuthorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleReporterIsAuthorException(ReporterIsAuthorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDTO("Reporter cannot be the same as comment author"));
    }

    @ExceptionHandler(CommentReportNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDTO> handleCommentReportNotExistException(CommentReportNotExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO("No such comment report"));
    }

    @ExceptionHandler(CommentReportNotNewException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> handleCommentReportNotNewException(CommentReportNotNewException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO("You have already reported this comment"));
    }
}
