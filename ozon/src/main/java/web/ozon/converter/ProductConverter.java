package web.ozon.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.ozon.DTO.ProductDTO;
import web.ozon.entity.ProductEntity;
import web.ozon.repository.ProductRepository;

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
}
