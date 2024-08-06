package services;

import model.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarService {
    private Map<Integer, Car> cars = new HashMap<>();

    public void addCar(Car car) {
        cars.put(car.getId(), car);
    }

    public Car getCar(int id) {
        return cars.get(id);
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars.values());
    }

    public void updateCar(int id, Car car) {
        cars.put(id, car);
    }

    public void deleteCar(int id) {
        cars.remove(id);
    }

    public List<Car> searchCar(String brand, String model, int year){
        return cars.values().stream().filter(
                car -> car.getBrand().equalsIgnoreCase(brand) &&
                        car.getModel().equalsIgnoreCase(model) &&
                        car.getYear() == year
        ).collect(Collectors.toList());
    }
}
