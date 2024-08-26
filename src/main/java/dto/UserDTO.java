package dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String contactInfo;
}
