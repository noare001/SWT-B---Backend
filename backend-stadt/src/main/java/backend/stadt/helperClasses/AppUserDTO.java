package backend.stadt.helperClasses;

import backend.stadt.user.AppUser;
import backend.stadt.user.Role;
import lombok.AllArgsConstructor;
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
    private String providerName;

    public AppUserDTO(AppUser user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        if(user.getProvider() != null){
            providerId = user.getProvider().getId();
            providerName = user.getProvider().getName();
        }
    }
}