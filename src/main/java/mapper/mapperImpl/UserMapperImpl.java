package mapper.mapperImpl;

import dto.UserDTO;
import mapper.UserMapper;
import model.User;

public class UserMapperImpl implements UserMapper {
    @Override
    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getId(), user.getName(), user.getRole(), user.getContactInfo(), user.getPassword());
    }

    @Override
    public User toUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getRole(), userDTO.getContactInfo(), userDTO.getPassword());
    }
}
