package micro.service.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lib.entity.dto.DTO.CommentRequestDTO;
import lib.entity.dto.exception.NullCommentException;
import lib.entity.dto.exception.NullContentException;
import micro.service.admin.service.CommentRequestService;

@RestController
@RequestMapping("/comment-requests")
public class CommentRequestController {

    @Autowired
    private CommentRequestService commentRequestService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CommentRequestDTO>> getCommentRequests(@RequestParam(name = "from") Integer from) {
        return new ResponseEntity<>(commentRequestService.getAll(from), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/unchecked")
    public ResponseEntity<List<CommentRequestDTO>> getUncheckedCommentRequests(
            @RequestParam(name = "from") Integer from) {
        return new ResponseEntity<>(commentRequestService.getAllNotChecked(from), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentRequestDTO> updateCommentRequest(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO dto) throws NullCommentException, NullContentException {
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
