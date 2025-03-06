package web.ozon.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import web.ozon.DTO.CommentDTO;
import web.ozon.converter.UserConverter;
import web.ozon.entity.UserEntity;
import web.ozon.exception.*;

@Service
public class CommentFilter {

    @Autowired
    private UserConverter userConverter;

    public boolean isOkNewDto(CommentDTO commentDTO)
            throws NullPointerException,
            NullAuthorIdException,
            NullProductIdException,
            NonNullNewIdException,
            NullContentException,
            NullAnonException,
            NotSameAuthorException {
        return isFieldsOk(commentDTO) && isAuthorTheSame(commentDTO);
    }

    private Boolean isFieldsOk(CommentDTO commentDTO)
            throws NullPointerException,
            NullAuthorIdException,
            NullProductIdException,
            NonNullNewIdException,
            NullContentException,
            NullAnonException {
        if (commentDTO == null)
            throw new NullPointerException();
        if (commentDTO.getAuthorId() == null)
            throw new NullAuthorIdException();
        if (commentDTO.getProductId() == null)
            throw new NullProductIdException();
        if (commentDTO.getId() != null)
            throw new NonNullNewIdException();
        if (commentDTO.getContent() == null)
            throw new NullContentException();
        if (commentDTO.getIsAnonymous() == null)
            throw new NullAnonException();
        return true;
    }

    private boolean isAuthorTheSame(CommentDTO commentDTO) throws NotSameAuthorException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserEntity user = userConverter.fromId(commentDTO.getAuthorId());
        if (user == null || !user.getLogin().equals(currentPrincipalName))
            throw new NotSameAuthorException();
        return true;
    }
}
