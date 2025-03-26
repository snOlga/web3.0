package web.ozon.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.ozon.DTO.CommentReportDTO;
import web.ozon.exception.CommentNotExistException;
import web.ozon.exception.NullCommentException;
import web.ozon.exception.NullContentException;
import web.ozon.exception.NullReasonException;
import web.ozon.repository.CommentRepository;

@Service
public class CommentReportFilter {

    @Autowired
    private CommentRepository commentRepository;

    public void filter(CommentReportDTO dto)
            throws NullCommentException, CommentNotExistException, NullReasonException, NullContentException {
        isFieldsOk(dto);
        isCommentExists(dto);
    }

    private void isFieldsOk(CommentReportDTO dto)
            throws NullCommentException,
            CommentNotExistException,
            NullReasonException,
            NullContentException {
        if (dto == null)
            throw new NullPointerException();
        if (dto.getCommentId() == null)
            throw new NullCommentException();
        if (dto.getReason() == null)
            throw new NullReasonException();
        if (dto.getMessage() == null)
            throw new NullContentException();
    }

    private void isCommentExists(CommentReportDTO dto) throws CommentNotExistException {
        if (!commentRepository.findById(dto.getCommentId()).isPresent())
            throw new CommentNotExistException();
    }
}
