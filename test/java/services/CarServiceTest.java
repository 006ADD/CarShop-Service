package services;


import model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class CarServiceTest {

    private CarService carService;
    private Car car1;
    private Car car2;

//    @BeforeEach
//    void setUp() {
//        carService = new CarService();
//        car1 = new Car(1, "Toyota", "Camry", 2021);
//        car2 = new Car(2, "Honda", "Civic", 2020);
//    }
//
//    @Test
//    void addCar_ShouldAddCarToCarsMap() {
//        carService.addCar(car1);
//
//        Car retrievedCar = carService.getCar(1);
//        assertThat(retrievedCar).isEqualTo(car1);
//    }
//
//    @Test
//    void getAllCars_ShouldReturnListOfAllCars() {
//        carService.addCar(car1);
//        carService.addCar(car2);
//
//        List<Car> cars = carService.getAllCars();
//        assertThat(cars).hasSize(2).containsExactlyInAnyOrder(car1, car2);
//    }
//
//    @Test
//    void updateCar_ShouldUpdateExistingCar() {
//        carService.addCar(car1);
//
//        Car updatedCar = new Car(1, "Toyota", "Camry", 2022);
//        carService.updateCar(1, updatedCar);
//
//        Car retrievedCar = carService.getCar(1);
//        assertThat(retrievedCar).isEqualTo(updatedCar);
//    }
//
//    @Test
//    void deleteCar_ShouldRemoveCarFromCarsMap() {
//        carService.addCar(car1);
//        carService.addCar(car2);
//
//        carService.deleteCar(1);
//
//        Car retrievedCar = carService.getCar(1);
//        assertThat(retrievedCar).isNull();
//
//        List<Car> cars = carService.getAllCars();
//        assertThat(cars).hasSize(1).containsOnly(car2);
//    }
//
//    @Test
//    void searchCar_ShouldReturnMatchingCars() {
//        carService.addCar(car1);
//        carService.addCar(car2);
//        Car car3 = new Car(3, "Toyota", "Camry", 2021);
//        carService.addCar(car3);
//
//        //List<Car> result = carService.searchCar("Toyota", "Camry", 2021);
//
//        assertThat(result).hasSize(2).containsExactlyInAnyOrder(car1, car3);
//    }
//
//    @Test
//    void searchCar_ShouldReturnEmptyListWhenNoMatch() {
//        carService.addCar(car1);
//        carService.addCar(car2);
//
//        //List<Car> result = carService.searchCar("Ford", "Focus", 2019);
//
//        assertThat(result).isEmpty();
//    }
}
