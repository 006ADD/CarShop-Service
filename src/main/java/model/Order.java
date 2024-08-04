package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int id;
    private User client;
    private Car car;
    private LocalDate orderDate;
    private String status;

    public Order(int id,  String status) {
        this.id = id;
        this.status = status;
    }
}
