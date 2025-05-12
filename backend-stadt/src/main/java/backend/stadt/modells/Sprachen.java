package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "Sprachen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sprachen {

    @Id
    @Column(name = "AngebotsId")
    private Integer angebotsId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "AngebotsId")
    private Angebot angebot;

    @Column(name = "Arabisch")
    private Boolean arabisch;

    @Column(name = "Bosnisch_Kroatisch_Serbisch")
    private Boolean bosnischKroatischSerbisch;

    @Column(name = "Bulgarisch")
    private Boolean bulgarisch;

    @Column(name = "Chinesisch")
    private Boolean chinesisch;

    @Column(name = "Dari")
    private Boolean dari;

    @Column(name = "Englisch")
    private Boolean englisch;

    @Column(name = "Franzoesisch")
    private Boolean franzoesisch;

    @Column(name = "Griechisch")
    private Boolean griechisch;

    @Column(name = "Italienisch")
    private Boolean italienisch;

    @Column(name = "Polnisch")
    private Boolean polnisch;

    @Column(name = "Portugiesisch")
    private Boolean portugiesisch;

    @Column(name = "Romanes")
    private Boolean romanes;

    @Column(name = "Rumaenisch")
    private Boolean rumaenisch;

    @Column(name = "Russisch")
    private Boolean russisch;

    @Column(name = "Shqip")
    private Boolean shqip;

    @Column(name = "Spanisch")
    private Boolean spanisch;

    @Column(name = "Tuerkisch")
    private Boolean tuerkisch;

    @Column(name = "Ukrainisch")
    private Boolean ukrainisch;

    @Column(name = "Urdu")
    private Boolean urdu;

    @Lob
    @Column(name = "Andere_Sprache", columnDefinition = "TEXT")
    private String andereSprache;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sprachen sprachen = (Sprachen) o;
        return angebotsId != null && angebotsId.equals(sprachen.angebotsId);
    }

    @Override
    public int hashCode() {
        return angebotsId != null ? angebotsId.hashCode() : getClass().hashCode();
    }
}