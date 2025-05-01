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

import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import lib.entity.dto.DTO.CommentReportDTO;
import lib.entity.dto.exception.CommentNotExistException;
import lib.entity.dto.exception.CommentReportNotExistException;
import lib.entity.dto.exception.CommentReportNotNewException;
import lib.entity.dto.exception.NotSameAuthorException;
import lib.entity.dto.exception.NullAuthorIdException;
import lib.entity.dto.exception.NullCommentException;
import lib.entity.dto.exception.NullContentException;
import lib.entity.dto.exception.NullReasonException;
import lib.entity.dto.exception.ReporterIsAuthorException;
import web.ozon.filter.CommentReportFilter;
import web.ozon.service.CommentReportService;

@RestController
@RequestMapping("/comment-reports")
public class CommentReportController {

    @Autowired
    private CommentReportService commentReportService;
    @Autowired
    private CommentReportFilter commentReportFilter;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CommentReportDTO>> getCommentReports(@RequestParam(name = "from") Integer from) {
        return new ResponseEntity<>(commentReportService.getAll(from), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/unchecked")
    public ResponseEntity<List<CommentReportDTO>> getUncheckedCommentReports(
            @RequestParam(name = "from") Integer from) {
        return new ResponseEntity<>(commentReportService.getAllNotChecked(from), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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
            NullAuthorIdException, ReporterIsAuthorException, NotSameAuthorException, CommentReportNotNewException {
        dto.setIsAccepted(null);
        commentReportFilter.filter(dto);
        CommentReportDTO result = commentReportService.save(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<CommentReportDTO> updateCommentReportByChecker(
            @PathVariable Long id,
            @RequestBody CommentReportDTO dto)
            throws CommentReportNotExistException, SystemException, SecurityException, IllegalStateException,
            RollbackException, HeuristicMixedException, HeuristicRollbackException {
        dto.setId(id);
        CommentReportDTO result = commentReportService.updateByChecker(dto);
        return result != null
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentReportDTO> updateCommentReportByUser(
            @PathVariable Long id,
            @RequestBody CommentReportDTO dto)
            throws CommentReportNotExistException, NullCommentException, CommentNotExistException, NullReasonException,
            NullContentException, NullAuthorIdException, ReporterIsAuthorException, NotSameAuthorException, CommentReportNotNewException {
        dto.setId(id);
        commentReportFilter.filter(dto);
        CommentReportDTO result = commentReportService.updateByUser(dto);
        return result != null
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().build();
    }
}
