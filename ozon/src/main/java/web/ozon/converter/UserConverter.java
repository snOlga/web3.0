package web.ozon.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.ozon.DTO.UserDTO;
import web.ozon.entity.UserEntity;
import web.ozon.repository.UserRepository;

@Service
public class UserConverter {

    @Autowired
    private UserRepository userRepository;

    public UserEntity fromDTO(UserDTO dto) {
        return userRepository.findByLogin(dto.getLogin());
    }

    public UserDTO fromEntity(UserEntity entity) {
        return new UserDTO(entity.getId(), entity.getLogin());
    }
}
