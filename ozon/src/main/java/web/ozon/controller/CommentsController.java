package web.ozon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentsController {
    
    @GetMapping("/{productId}")
    public ResponseEntity<String> firstMethod(@PathVariable Long productId) {
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }
}
