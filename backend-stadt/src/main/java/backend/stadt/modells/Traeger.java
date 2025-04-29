package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Träger") // Name der Tabelle in der DB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Traeger { // Klassenname in Java

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Straße")
    private String strasse;

    @Column(name = "Ort")
    private String ort;

    @Column(name = "PLZ")
    private Integer plz;

    @Column(name = "TrägerArt")
    private String traegerArt;

    @Column(name = "Kontaktperson")
    private String kontaktperson;

    @Column(name = "Telefonnummer")
    private String telefonnummer;

    @Column(name = "E-Mail")
    private String email;

    @Column(name = "Link")
    private String link;

    @OneToMany(mappedBy = "traeger", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Anbieter> anbieterList;

    // equals() und hashCode() basierend auf id empfohlen
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Traeger traeger = (Traeger) o;
        return id != null && id.equals(traeger.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}
