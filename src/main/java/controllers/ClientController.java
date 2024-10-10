package controllers;

import dto.UserDTO;
import lombok.RequiredArgsConstructor;
import mapper.mapperImpl.UserMapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ClientService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final UserMapperImpl userMapper;

    @GetMapping("/allClient")
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok().body(clientService.getAllClient());
    }
    @GetMapping("/getIdClient/{id}")
    public ResponseEntity<?> getClientById(@PathVariable int id){
        return ResponseEntity.ok().body(clientService.getUserById(id));
    }

    @PostMapping("/addClient")
    public ResponseEntity<UserDTO> addClient(UserDTO userDTO){
        clientService.addClient(userMapper.toUser(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/updateClient/{id,newName,newContactInfo}")
    public ResponseEntity<UserDTO> updateClient(int id, String newName, String newContactInfo){
        clientService.updateClient(id, newName, newContactInfo);
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).build();
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable int id){
        clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
