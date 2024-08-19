package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {
    private Integer id;

    @NotBlank(message = "Brand cannot be blank")
    private String brand;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @NotNull(message = "Year cannot be null")
    @Min(value = 1886, message = "Year must be after 1886")
    private int year;

    @Positive(message = "Price must be positive")
    private double price;

    @NotBlank(message = "Condition cannot be blank")
    private String condition;

    @NotBlank(message = "Status cannot be blank")
    private String status;

}
