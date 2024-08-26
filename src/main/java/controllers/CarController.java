package controllers;

import dto.CarDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mapper.CarMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.CarService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {
    private final CarService carService;
    private final CarMapper carMapper;

    @PostMapping("/addCar")
    public ResponseEntity<CarDTO> addCar(@Valid @RequestBody CarDTO carDTO){
        carService.addCar(carMapper.toCar(carDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getCarOne/{id}")
    public ResponseEntity<?> getCarId(@PathVariable int id){
        return ResponseEntity.ok().body(carService.getCar(id));
    }

    @GetMapping("/getAllCars")
    public ResponseEntity<?> getAllCars(){
        return ResponseEntity.ok().body(carService.getAllCars());
    }

    @PatchMapping("/editCar/{id,car}")
    public ResponseEntity<?> editCar(@PathVariable int id, @RequestBody CarDTO carDTO){
        carService.updateCar(id, carMapper.toCar(carDTO));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/deleteCarId/{id}")
    public ResponseEntity<?> deleteCarId(@PathVariable int id){
        carService.deleteCar(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
