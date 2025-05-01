package web.ozon.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lib.entity.dto.DTO.CommentReportDTO;
import lib.entity.dto.converter.UserConverter;
import lib.entity.dto.entity.UserEntity;
import lib.entity.dto.exception.CommentNotExistException;
import lib.entity.dto.exception.CommentReportNotNewException;
import lib.entity.dto.exception.NotSameAuthorException;
import lib.entity.dto.exception.NullAuthorIdException;
import lib.entity.dto.exception.NullCommentException;
import lib.entity.dto.exception.NullContentException;
import lib.entity.dto.exception.NullReasonException;
import lib.entity.dto.exception.ReporterIsAuthorException;
import lib.entity.dto.repository.CommentReportRepository;
import lib.entity.dto.repository.CommentRepository;

@Service
public class CommentReportFilter {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private CommentReportRepository commentReportRepository;

    public void filter(CommentReportDTO dto)
            throws NullCommentException, CommentNotExistException, NullReasonException, NullContentException,
            NullAuthorIdException, ReporterIsAuthorException, NotSameAuthorException, CommentReportNotNewException {
        isFieldsOk(dto);
        isCommentExists(dto);
        isReporterTheSame(dto);
        isReporterNotCommentAuthor(dto);
        isReportNew(dto);
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
        if (dto.getReporterId() == null)
            throw new NullAuthorIdException();
    }

    private void isCommentExists(CommentReportDTO dto) throws CommentNotExistException {
        if (!commentRepository.findById(dto.getCommentId()).isPresent())
            throw new CommentNotExistException();
    }

    private void isReporterTheSame(CommentReportDTO dto) throws NotSameAuthorException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserEntity user = userConverter.fromId(dto.getReporterId());
        if (user == null || !user.getLogin().equals(currentPrincipalName))
            throw new NotSameAuthorException();
    }

    private void isReporterNotCommentAuthor(CommentReportDTO dto) throws ReporterIsAuthorException {
        if (dto.getReporterId() == commentRepository.findById(dto.getCommentId()).get().getAuthor().getId())
            throw new ReporterIsAuthorException();
    }

    private void isReportNew(CommentReportDTO dto) throws CommentReportNotNewException {
        if (commentReportRepository.findByCommentIdAndReporterId(dto.getCommentId(), dto.getReporterId()) != null)
            throw new CommentReportNotNewException();
    }
}
