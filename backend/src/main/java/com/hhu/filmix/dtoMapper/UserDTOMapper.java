package com.hhu.filmix.dtoMapper;

import com.hhu.filmix.dto.userDTO.response.UserDTO;
import com.hhu.filmix.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {
    public UserDTO map(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getType()
        );
    }
}
