package web.ozon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import web.ozon.DTO.CommentDTO;
import web.ozon.service.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{productId}")
    public ResponseEntity<List<CommentDTO>> firstMethod(@PathVariable Long productId) {
        return new ResponseEntity<>(commentService.getAllByProductId(productId), HttpStatus.OK);
    }
}
