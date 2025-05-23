package com.example.workflow.filter;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lib.entity.dto.DTO.CommentDTO;
import lib.entity.dto.entity.BannedWordEntity;
import lib.entity.dto.entity.PurchaseEntity;
import lib.entity.dto.exception.*;
import lib.entity.dto.repository.BannedWordsRepository;
import lib.entity.dto.repository.PurchaseRepository;

@Service
public class CommentFilter {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private BannedWordsRepository bannedWordsRepository;

    private int PAGINATION_STEP = 10;
    private int MAX_CONTENT_LENGTH = 255;

    public void filter(CommentDTO commentDTO)
            throws NullPointerException,
            NullAuthorIdException,
            NullProductIdException,
            NullContentException,
            NullAnonException,
            NotSameAuthorException,
            CommentNotNewException,
            ProductNotBoughtException,
            RudeTextException,
            NonNullNewIdException, ContentTooLongException {
        isFieldsOk(commentDTO);
        isBusinessOk(commentDTO);
    }

    private void isFieldsOk(CommentDTO commentDTO)
            throws NullPointerException,
            NullAuthorIdException,
            NullProductIdException,
            NullContentException,
            NullAnonException,
            NonNullNewIdException, 
            ContentTooLongException {
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
        if (commentDTO.getContent().length() > MAX_CONTENT_LENGTH)
            throw new ContentTooLongException();
    }

    private void isBusinessOk(CommentDTO commentDTO)
            throws CommentNotNewException, ProductNotBoughtException, RudeTextException {
        isTheProductWasBought(commentDTO);
        isRudeText(commentDTO.getContent());
    }

    private void isTheProductWasBought(CommentDTO commentDTO) throws ProductNotBoughtException {
        PurchaseEntity purchaseEntity = purchaseRepository.findByOwnerIdAndProductId(commentDTO.getAuthorId(),
                commentDTO.getProductId());
        if (purchaseEntity == null)
            throw new ProductNotBoughtException();
    }

    private void isRudeText(String input) throws RudeTextException {
        boolean isRude = Stream.iterate(0, from -> from + PAGINATION_STEP)
                .map(from -> bannedWordsRepository.findAll(PageRequest.of(from, PAGINATION_STEP)))
                .takeWhile(bannedWords -> !bannedWords.isEmpty())
                .flatMap(bannedWords -> bannedWords.getContent().stream())
                .map(BannedWordEntity::getWord)
                .anyMatch(input::contains);
        if (isRude)
            throw new RudeTextException();
    }
}
