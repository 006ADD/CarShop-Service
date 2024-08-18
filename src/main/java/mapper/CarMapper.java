package mapper;

import dto.CarDTO;
import dto.UserDTO;
import model.Car;
import model.User;
import org.mapstruct.Mapper;

@Mapper
public interface CarMapper {
    CarDTO toCarDTO(Car car);
    Car toCar(CarDTO carDTO);
}
