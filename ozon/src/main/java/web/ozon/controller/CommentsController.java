package web.ozon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import web.ozon.DTO.CommentDTO;
import web.ozon.service.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{productId}/{from}/{to}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long productId,
            @PathVariable Integer from,
            @PathVariable Integer to) {
        return new ResponseEntity<>(commentService.getAllByProductId(productId, from, to), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<CommentDTO> postComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO result = commentService.save(commentDTO);
        return new ResponseEntity<>(result, result != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }
}
