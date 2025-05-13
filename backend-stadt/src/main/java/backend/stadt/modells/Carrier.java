package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "Carrier") // Name der Tabelle in der DB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carrier { // Klassenname in Java

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

    @Column(name = "CarrierType")
    private String carrierType;

    @Column(name = "ContactPerson")
    private String contactPerson;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Email")
    private String email;

    @Column(name = "Link")
    private String link;

    @OneToMany(mappedBy = "carrier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Provider> providers;

    // equals() und hashCode() basierend auf id empfohlen
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrier carrier = (Carrier) o;
        return id != null && id.equals(carrier.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}
