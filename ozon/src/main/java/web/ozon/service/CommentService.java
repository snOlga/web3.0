package web.ozon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import web.ozon.DTO.CommentDTO;
import web.ozon.converter.CommentConverter;
import web.ozon.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentConverter commentConverter;

    public List<CommentDTO> getAllByProductId(Long productId, int from, int to) {
        return commentRepository.findAllByProductId(productId, PageRequest.of(from, to)).stream()
                .map(commentConverter::fromEntity).toList();
    }
}
