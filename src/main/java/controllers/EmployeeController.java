package controllers;

import dto.UserDTO;
import lombok.RequiredArgsConstructor;
import mapper.mapperImpl.UserMapperImpl;
import model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.EmployeeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final UserMapperImpl userMapper;
    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody UserDTO userDTO) {
        employeeService.addEmployee(userMapper.toUser(userDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping("deleteEmployee/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getAllEmployee")
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok().body(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User employee = employeeService.getUserById(id);
        return employee != null ? new ResponseEntity<>(employee, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("updateEmployee/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable int id, @RequestParam String newName,
            @RequestParam String newContactInfo) {
        employeeService.updateEmployee(id, newName, newContactInfo);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
