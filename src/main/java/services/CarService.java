package services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.Car;
import repositories.CarRepository;
import utils.DataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
public class CarService {
    private CarRepository carRepository;

    public void addCar(Car car) {
        carRepository.insertCar(car);
    }

    public Car getCar(int id) {
        return carRepository.findById(id);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public void updateCar(int id, Car car) {
        carRepository.update(id, car);
    }

    public void deleteCar(int id) {
        carRepository.remove(id);
    }
}
