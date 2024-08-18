package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Car;
import model.User;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotNull(message = "Client ID cannot be null")
    private Integer id;

    private UserDTO client;

    private CarDTO car;

    @NotNull(message = "Order date cannot be null")
    private LocalDate orderDate;

    @NotBlank(message = "Status cannot be blank")
    private String status;
}
