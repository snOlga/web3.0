package web.ozon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.PermitAll;
import web.ozon.DTO.CommentDTO;
import web.ozon.service.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @PermitAll
    @GetMapping("/{productId}/{from}/{to}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long productId,
            @PathVariable Integer from,
            @PathVariable Integer to) {
        return new ResponseEntity<>(commentService.getAllByProductId(productId, from, to), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public ResponseEntity<CommentDTO> postComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO result = commentService.save(commentDTO);
        return new ResponseEntity<>(result, result != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        if (!id.equals(commentDTO.getId())) {
            return ResponseEntity.badRequest().build();
        }
        CommentDTO result = commentService.update(commentDTO);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        return commentService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
