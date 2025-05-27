package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "Provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Street")
    private String street;

    @Column(name = "City")
    private String city;

    @Column(name = "PostalCode")
    private Integer postalCode;

    @Column(name = "ContactPerson")
    private String contactPerson;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Email")
    private String email;

    @Column(name = "Link")
    private String link;


    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Offer> offers;

    // equals() und hashCode() basierend auf id empfohlen
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provider provider = (Provider) o;
        return id != null && id.equals(provider.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}
