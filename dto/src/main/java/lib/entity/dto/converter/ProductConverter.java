package lib.entity.dto.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lib.entity.dto.DTO.ProductDTO;
import lib.entity.dto.entity.ProductEntity;
import lib.entity.dto.repository.ProductRepository;

@Service
public class ProductConverter {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserConverter userConverter;

    public ProductEntity fromDTO(ProductDTO dto) {
        if (dto == null || dto.getOwner() == null)
            return null;

        return productRepository.findById(dto.getId()).get();
    }

    public ProductDTO fromEntity(ProductEntity entity) {
        return new ProductDTO(
                entity.getId(),
                userConverter.fromEntity(entity.getSeller()),
                entity.getContent());
    }

    public ProductEntity fromId(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
