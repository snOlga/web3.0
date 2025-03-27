package web.ozon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.ozon.DTO.CommentReportDTO;
import web.ozon.DTO.CommentRequestDTO;
import web.ozon.exception.CommentNotExistException;
import web.ozon.exception.CommentReportNotExistException;
import web.ozon.exception.NotSameAuthorException;
import web.ozon.exception.NullAuthorIdException;
import web.ozon.exception.NullCommentException;
import web.ozon.exception.NullContentException;
import web.ozon.exception.NullReasonException;
import web.ozon.exception.ReporterIsAuthorException;
import web.ozon.filter.CommentReportFilter;
import web.ozon.service.CommentReportService;

@RestController
@RequestMapping("/comment-reports")
public class CommentReportController {

    @Autowired
    private CommentReportService commentReportService;
    @Autowired
    private CommentReportFilter commentReportFilter;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODER')")
    @GetMapping
    public ResponseEntity<List<CommentReportDTO>> getCommentReports(@RequestParam(name = "from") Integer from) {
        return new ResponseEntity<>(commentReportService.getAll(from), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODER')")
    @GetMapping("/unchecked")
    public ResponseEntity<List<CommentReportDTO>> getUncheckedCommentReports(
            @RequestParam(name = "from") Integer from) {
        return new ResponseEntity<>(commentReportService.getAllNotChecked(from), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODER')")
    @GetMapping("/comment/{id}")
    public ResponseEntity<List<CommentReportDTO>> getAllCommentReportsByCommentId(
            @PathVariable Long id,
            @RequestParam(name = "from") Integer from) {
        return new ResponseEntity<>(commentReportService.getAllByCommentId(id, from), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public ResponseEntity<CommentReportDTO> postCommentReport(@RequestBody CommentReportDTO dto)
            throws NullCommentException, CommentNotExistException, NullReasonException, NullContentException,
            NullAuthorIdException, ReporterIsAuthorException, NotSameAuthorException {
        dto.setIsAccepted(null);
        commentReportFilter.filter(dto);
        CommentReportDTO result = commentReportService.save(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODER')")
    @PatchMapping("/{id}")
    public ResponseEntity<CommentReportDTO> updateCommentReport(
            @PathVariable Long id,
            @RequestBody CommentReportDTO dto) throws CommentReportNotExistException {
        dto.setId(id);
        CommentReportDTO result = commentReportService.updateByChecker(dto);
        return result != null
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().build();
    }
}
