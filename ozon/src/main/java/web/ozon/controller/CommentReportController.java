package web.ozon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.ozon.DTO.CommentReportDTO;
import web.ozon.service.CommentReportService;

@RestController
@RequestMapping("/comment-reports")
public class CommentReportController {

    @Autowired
    private CommentReportService commentReportService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODER')")
    @GetMapping()
    public ResponseEntity<List<CommentReportDTO>> getCommentRequests(@RequestParam(name="from") Integer from) {
        return new ResponseEntity<>(commentReportService.getAll(from), HttpStatus.OK);
    }
}
