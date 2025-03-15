package web.ozon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.PermitAll;
import web.ozon.DTO.CommentDTO;
import web.ozon.exception.CommentNotExistException;
import web.ozon.exception.CommentNotNewException;
import web.ozon.exception.ContentTooLongException;
import web.ozon.exception.NonNullNewIdException;
import web.ozon.exception.NotSameAuthorException;
import web.ozon.exception.NullAnonException;
import web.ozon.exception.NullAuthorIdException;
import web.ozon.exception.NullContentException;
import web.ozon.exception.NullProductIdException;
import web.ozon.exception.ProductNotBoughtException;
import web.ozon.exception.RudeTextException;
import web.ozon.filter.CommentFilter;
import web.ozon.service.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentFilter commentFilter;

    @PermitAll
    @GetMapping("/{productId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long productId, @RequestParam(name="from") Integer from) {
        return new ResponseEntity<>(commentService.getAllByProductId(productId, from), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public ResponseEntity<CommentDTO> postComment(@RequestBody CommentDTO commentDTO)
            throws NullPointerException, NullAuthorIdException, NullProductIdException, NonNullNewIdException,
            NullContentException, NullAnonException, NotSameAuthorException, CommentNotNewException, ProductNotBoughtException, RudeTextException, ContentTooLongException {
        commentFilter.isOkNewDto(commentDTO);
        CommentDTO result = commentService.save(commentDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) throws CommentNotExistException, NotSameAuthorException  {
        commentDTO.setId(id);
        CommentDTO result = commentService.update(commentDTO);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) throws CommentNotExistException, NotSameAuthorException {
        return commentService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
