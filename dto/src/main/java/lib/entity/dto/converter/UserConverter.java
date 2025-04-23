package lib.entity.dto.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lib.entity.dto.DTO.UserDTO;
import lib.entity.dto.entity.UserEntity;
import lib.entity.dto.repository.UserRepository;

@Service
public class UserConverter {

    @Autowired
    private UserRepository userRepository;

    public UserEntity fromDTO(UserDTO dto) {
        if (dto == null)
            return null;

        return userRepository.findByLogin(dto.getLogin());
    }

    public UserDTO fromEntity(UserEntity entity) {
        if (entity == null)
            return null;

        return new UserDTO(entity.getId(), entity.getLogin());
    }

    public UserEntity fromId(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
