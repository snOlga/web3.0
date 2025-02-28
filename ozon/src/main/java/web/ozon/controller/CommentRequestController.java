package web.ozon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentRequestDTO> updateCommentRequest(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO dto) {
        dto.setId(id);
        CommentRequestDTO result = commentRequestService.update(dto);
        return result != null
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentRequest(@PathVariable Long id) {
        return commentRequestService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
