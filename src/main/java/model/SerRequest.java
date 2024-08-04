package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SerRequest {
    private int id;
    private User user;
    private Car car;
    private String description;
    private String status;

    public SerRequest(int id,String description) {
        this.id = id;
        this.description=description;
    }
}
