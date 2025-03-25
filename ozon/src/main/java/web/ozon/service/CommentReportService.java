package web.ozon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import web.ozon.DTO.CommentReportDTO;
import web.ozon.converter.CommentReportConverter;
import web.ozon.repository.CommentReportRepository;

@Service
public class CommentReportService {

    @Autowired
    private CommentReportRepository commentReportRepository;
    @Autowired
    private CommentReportConverter commentReportConverter;

    @Value("${business.pagination.step}")
    private int PAGINATION_STEP;

    public List<CommentReportDTO> getAll(int from) {
        return commentReportRepository.findAll(PageRequest.of(from, PAGINATION_STEP)).getContent().stream()
                .map(commentReportConverter::fromEntity).toList();
    }

    public List<CommentReportDTO> getAllNotChecked(int from) {
        return getAll(from).stream().filter(report -> !report.getIsChecked()).toList();
    }

    public List<CommentReportDTO> getAllByCommentId(Long id, int from) {
        return commentReportRepository.findAllByCommentId(id, PageRequest.of(from, PAGINATION_STEP)).stream()
                .map(commentReportConverter::fromEntity).toList();
    }
}
