package backend.stadt.helperClasses;

import backend.stadt.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppUserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Integer providerId;
    private String providerName;
}