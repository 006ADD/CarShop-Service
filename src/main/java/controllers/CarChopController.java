package controllers;

import dto.UserDTO;
import lombok.AllArgsConstructor;
import mapper.mapperImpl.UserMapperImpl;
import model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.UserService;

@RestController
@RequestMapping("/carshop")
@AllArgsConstructor
public class CarChopController {
    private final UserService userService;
    private final UserMapperImpl userMapper;

    @GetMapping("/login")
    public ResponseEntity<?> login(String name, String password){
return ResponseEntity.ok().body(userService.loginUser(name, password));
    }

    @GetMapping("/register")
    public ResponseEntity<?> register(UserDTO userDTO){
        return ResponseEntity.ok().body(userService.registerUser(userMapper.toUser(userDTO)));
    }
}
