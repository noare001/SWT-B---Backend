package backend.stadt.user;

import backend.stadt.modells.OfferRegistration;
import backend.stadt.modells.Provider;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private String name;
    private String password;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider; // Nur für Autoren relevant

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<OfferRegistration> registrations = new HashSet<>();
}
