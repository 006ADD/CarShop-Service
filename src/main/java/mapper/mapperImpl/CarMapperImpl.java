package mapper.mapperImpl;

import dto.CarDTO;
import mapper.CarMapper;
import model.Car;

public class CarMapperImpl implements CarMapper {

    @Override
    public CarDTO toCarDTO(Car car) {
        if (car == null) {
            return null;
        }
        return new CarDTO(
            car.getId(),
            car.getBrand(),
            car.getModel(),
            car.getYear(),
            car.getPrice(),
            car.getCondition(),
            car.getStatus()
        );
    }

    @Override
    public Car toCar(CarDTO carDTO) {
        if (carDTO == null) {
            return null;
        }
        return new Car(
                carDTO.getId(),
                carDTO.getBrand(),
                carDTO.getModel(),
                carDTO.getYear(),
                carDTO.getPrice(),
                carDTO.getCondition(),
                carDTO.getStatus()
        );
    }
}
