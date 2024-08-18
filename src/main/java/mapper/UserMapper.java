package mapper;

import dto.UserDTO;
import model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
}
