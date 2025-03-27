package web.ozon.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.ozon.DTO.CommentReportDTO;
import web.ozon.exception.CommentNotExistException;
import web.ozon.exception.NullAuthorIdException;
import web.ozon.exception.NullCommentException;
import web.ozon.exception.NullContentException;
import web.ozon.exception.NullReasonException;
import web.ozon.exception.ReporterIsAuthorException;
import web.ozon.repository.CommentRepository;

@Service
public class CommentReportFilter {

    @Autowired
    private CommentRepository commentRepository;

    public void filter(CommentReportDTO dto)
            throws NullCommentException, CommentNotExistException, NullReasonException, NullContentException, NullAuthorIdException, ReporterIsAuthorException {
        isFieldsOk(dto);
        isCommentExists(dto);
        isReporterNotCommentAuthor(dto);
    }

    private void isFieldsOk(CommentReportDTO dto)
            throws NullCommentException,
            CommentNotExistException,
            NullReasonException,
            NullContentException, NullAuthorIdException {
        if (dto == null)
            throw new NullPointerException();
        if (dto.getCommentId() == null)
            throw new NullCommentException();
        if (dto.getReason() == null)
            throw new NullReasonException();
        if (dto.getMessage() == null)
            throw new NullContentException();
        if (dto.getReporterId() == null)
            throw new NullAuthorIdException();
    }

    private void isCommentExists(CommentReportDTO dto) throws CommentNotExistException {
        if (!commentRepository.findById(dto.getCommentId()).isPresent())
            throw new CommentNotExistException();
    }

    private void isReporterNotCommentAuthor(CommentReportDTO dto) throws ReporterIsAuthorException {
        if(dto.getReporterId() == commentRepository.findById(dto.getCommentId()).get().getAuthor().getId())
            throw new ReporterIsAuthorException();
    }
}
