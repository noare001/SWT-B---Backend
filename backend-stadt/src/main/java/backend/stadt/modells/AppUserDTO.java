package backend.stadt.modells;

import backend.stadt.user.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Integer providerId;
}