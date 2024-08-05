package services;

import model.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для управления автомобилями.
 * <p>
 * Этот класс предоставляет методы для добавления, получения, обновления, удаления и поиска автомобилей.
 * Автомобили хранятся в внутренней структуре данных, основанной на карте, где ключом является уникальный идентификатор автомобиля.
 * </p>
 */
public class CarService {
    /**
     * Карта, хранящая автомобили по их уникальным идентификаторам.
     */
    private Map<Integer, Car> cars = new HashMap<>();

    /**
     * Добавляет новый автомобиль в сервис.
     * <p>
     * Если автомобиль с таким же идентификатором уже существует, он будет заменен.
     * </p>
     *
     * @param car Автомобиль, который необходимо добавить.
     */
    public void addCar(Car car) {
        cars.put(car.getId(), car);
    }

    /**
     * Получает автомобиль по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор автомобиля.
     * @return Автомобиль с указанным идентификатором или {@code null}, если автомобиль не найден.
     */
    public Car getCar(int id) {
        return cars.get(id);
    }

    /**
     * Получает все автомобили, хранящиеся в сервисе.
     *
     * @return Список всех автомобилей.
     */
    public List<Car> getAllCars() {
        return new ArrayList<>(cars.values());
    }

    /**
     * Обновляет информацию об автомобиле.
     * <p>
     * Если автомобиль с указанным идентификатором не существует, он будет добавлен как новый.
     * </p>
     *
     * @param id  Уникальный идентификатор автомобиля.
     * @param car Автомобиль с новой информацией.
     */
    public void updateCar(int id, Car car) {
        cars.put(id, car);
    }

    /**
     * Удаляет автомобиль по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор автомобиля.
     */
    public void deleteCar(int id) {
        cars.remove(id);
    }

    /**
     * Ищет автомобили по бренду, модели и году выпуска.
     * <p>
     * Возвращает список автомобилей, соответствующих указанным критериям поиска.
     * Поиск регистронезависимый для бренда и модели.
     * </p>
     *
     * @param brand Бренд автомобиля.
     * @param model Модель автомобиля.
     * @param year  Год выпуска автомобиля.
     * @return Список автомобилей, соответствующих критериям поиска.
     */
    public List<Car> searchCar(String brand, String model, int year) {
        return cars.values().stream().filter(
                car -> car.getBrand().equalsIgnoreCase(brand) &&
                        car.getModel().equalsIgnoreCase(model) &&
                        car.getYear() == year
        ).collect(Collectors.toList());
    }
}
