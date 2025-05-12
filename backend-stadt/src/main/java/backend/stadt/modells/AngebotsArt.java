package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "AngebotsArt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AngebotsArt {

    @Id
    @Column(name = "AngebotsId")
    private Integer angebotsId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "AngebotsId")
    private Angebot angebot;

    @Column(name = "Gruppenangebot")
    private Boolean gruppenangebot;

    @Column(name = "Veranstaltung")
    private Boolean veranstaltung;

    @Column(name = "Kurs-Workshop")
    private Boolean kursWorkshop;

    @Column(name = "Beratung")
    private Boolean beratung;

    @Column(name = "MedezinischeBeratung")
    private Boolean medezinischeBeratung;

    @Column(name = "OnlineAngebot")
    private Boolean onlineAngebot;

    @Lob
    @Column(name = "AndereAngebotart")
    private String andereAngebotart;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AngebotsArt that = (AngebotsArt) o;
        // Wichtig: Vergleich über die ID, die vom Angebot gemappt wird
        return angebotsId != null && angebotsId.equals(that.angebotsId);
    }

    @Override
    public int hashCode() {
        // Der Hashcode sollte auf der ID basieren
        // Da die ID erst nach dem Setzen der Beziehung/Persistieren gesetzt wird,
        // ist es sicherer, eine Konstante oder getClass().hashCode() zu verwenden,
        // solange die ID null ist, um Probleme in Collections zu vermeiden.
        return angebotsId != null ? angebotsId.hashCode() : getClass().hashCode();
    }
}