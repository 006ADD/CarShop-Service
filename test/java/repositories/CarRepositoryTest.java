package repositories;

import model.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import utils.DataSource;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarRepositoryTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14.3")
            .withDatabaseName("carshopservice")
            .withUsername("userPostgres")
            .withPassword("passwordPostgres");

    private DataSource dataSource;
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() throws Exception {
        postgresContainer.start();

        dataSource = new DataSource();
        dataSource.setJdbcUrl(postgresContainer.getJdbcUrl());
        dataSource.setUsername(postgresContainer.getUsername());
        dataSource.setPassword(postgresContainer.getPassword());

        carRepository = new CarRepository(dataSource);

        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE car (id SERIAL PRIMARY KEY, brand VARCHAR(255), model VARCHAR(255), year INT, price DECIMAL, condition VARCHAR(50), status VARCHAR(50))");
        }
    }

    @AfterEach
    public void tearDown() {
        postgresContainer.stop();
    }

    @Test
    public void testInsertAndFindCar() {
        Car car = new Car("Toyota", "Corolla", 2020, 20000, "New", "Available");
        carRepository.insertCar(car);

        Car foundCar = carRepository.findById(1);
        assertNotNull(foundCar);
        assertEquals("Toyota", foundCar.getBrand());
        assertEquals("Corolla", foundCar.getModel());
        assertEquals(2020, foundCar.getYear());
        assertEquals(20000, foundCar.getPrice());
        assertEquals("New", foundCar.getCondition());
        assertEquals("Available", foundCar.getStatus());
    }

    @Test
    public void testUpdateCar() {
        Car car = new Car("Toyota", "Corolla", 2020, 20000, "New", "Available");
        carRepository.insertCar(car);

        Car updatedCar = new Car("Toyota", "Camry", 2021, 25000, "Used", "Sold");
        carRepository.update(1, updatedCar);

        Car foundCar = carRepository.findById(1);
        assertNotNull(foundCar);
        assertEquals("Toyota", foundCar.getBrand());
        assertEquals("Camry", foundCar.getModel());
        assertEquals(2021, foundCar.getYear());
        assertEquals(25000, foundCar.getPrice());
        assertEquals("Used", foundCar.getCondition());
        assertEquals("Sold", foundCar.getStatus());
    }

    @Test
    public void testRemoveCar() {
        Car car = new Car("Toyota", "Corolla", 2020, 20000, "New", "Available");
        carRepository.insertCar(car);

        carRepository.remove(1);
        Car foundCar = carRepository.findById(1);
        assertNull(foundCar);
    }

    @Test
    public void testFindAllCars() {
        Car car1 = new Car("Toyota", "Corolla", 2020, 20000, "New", "Available");
        Car car2 = new Car("Honda", "Civic", 2019, 18000, "Used", "Sold");
        carRepository.insertCar(car1);
        carRepository.insertCar(car2);

        List<Car> cars = carRepository.findAll();
        assertEquals(2, cars.size());
    }
}
