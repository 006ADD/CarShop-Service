package repositories;

import lombok.AllArgsConstructor;
import model.Car;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private final DataSource dataSource;

    public CarRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertCar(Car car){
        String SQL = "INSERT INTO car(brand,model,year,price,condition,status) VALUES(?,?,?,?,?,?)";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL);){
            prs.setString(1,car.getBrand());
            prs.setString(2,car.getModel());
            prs.setInt(3,car.getYear());
            prs.setDouble(4, car.getPrice());
            prs.setString(5, car.getCondition());
            prs.setString(6,car.getStatus());
            prs.executeUpdate();
            System.out.println("Car added successfully");
        }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public Car findById(int id){
        Car car = null;
        String SQL = "SELECT FROM car WHERE id = ?";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL);){

            prs.setInt(1, id);

            try(ResultSet rs = prs.executeQuery()){
                if(rs.next()){
                    int carId =rs.getInt("id");
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");
                    double price = rs.getDouble("price");
                    String condition = rs.getString("condition");
                    String status = rs.getString("status");

                    car = new Car(carId, brand, model, year, price, condition, status);
                }
            }

        }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return car;
    }

    public List<Car> findAll(){
        List<Car> cars = new ArrayList<>();
        Car car = null;
        String SQL = "SELECT * FROM car";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL);
            ResultSet rs = prs.executeQuery();){

                while(rs.next()){
                    int id =rs.getInt("id");
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");
                    double price = rs.getDouble("price");
                    String condition = rs.getString("condition");
                    String status = rs.getString("status");
                    car = new Car(id, brand, model, year, price, condition, status);
                    cars.add(car);
                }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return cars;
    }

    public void update(int id, Car car){
        String SQL = "UPDATE car SET brand = ?, model = ?, year = ?, price = ?, condition = ?, status = ? WHERE id = ?";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL);){
            prs.setString(1,car.getBrand());
            prs.setString(2,car.getModel());
            prs.setInt(3,car.getYear());
            prs.setDouble(4, car.getPrice());
            prs.setString(5, car.getCondition());
            prs.setString(6,car.getStatus());
            prs.setInt(7, id);
            prs.executeUpdate();
            System.out.println("Car updated successfully");
        }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void remove(int id){
        String SQL = "DELETE FROM car WHERE id = ?";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL);){
            prs.setInt(1, id);
            prs.executeUpdate();
        }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}
