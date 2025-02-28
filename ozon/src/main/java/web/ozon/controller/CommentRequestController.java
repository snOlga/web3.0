package web.ozon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import web.ozon.DTO.CommentRequestDTO;
import web.ozon.service.CommentRequestService;

@RestController
@RequestMapping("/comment-requests")
public class CommentRequestController {

    @Autowired
    private CommentRequestService commentRequestService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/{from}/{to}")
    public ResponseEntity<List<CommentRequestDTO>> getCommentRequests(@PathVariable Integer from,
            @PathVariable Integer to) {
        return new ResponseEntity<>(commentRequestService.getAll(from, to), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/unchecked/{from}/{to}")
    public ResponseEntity<List<CommentRequestDTO>> getUncheckedCommentRequests(@PathVariable Integer from,
            @PathVariable Integer to) {
        return new ResponseEntity<>(commentRequestService.getAllNotChecked(from, to), HttpStatus.OK);
    }
}
