package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "Filter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    @Id
    @Column(name = "AngebotsId")
    private Integer angebotsId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "AngebotsId")
    private Angebot angebot;

    @Column(name = "Kinder_und_Jugendtreffs_Jugendzentren")
    private Boolean kinderUndJugendtreffsJugendzentren;

    @Column(name = "Ferienangebote")
    private Boolean ferienangebote;

    @Column(name = "Spielen_lernen_und_erleben")
    private Boolean spielenLernenUndErleben;

    @Column(name = "Sport_und_Bewegung")
    private Boolean sportUndBewegung;

    @Column(name = "Engagement_und_Ehrenamt")
    private Boolean engagementUndEhrenamt;

    @Column(name = "Kreativitaet_und_Kultur")
    private Boolean kreativitaetUndKultur;

    @Column(name = "Parks_und_Spielplaetze")
    private Boolean parksUndSpielplaetze;

    @Column(name = "Feste_und_Maerkte")
    private Boolean festeUndMaerkte;

    @Column(name = "Weitere_Angebote_Freizeit")
    private Boolean weitereAngeboteFreizeit;

    @Column(name = "KITA")
    private Boolean kita;

    @Column(name = "Kindertagespflege")
    private Boolean kindertagespflege;

    @Column(name = "Notfallbetreuung")
    private Boolean notfallbetreuung;

    @Column(name = "Babysitter")
    private Boolean babysitter;

    @Column(name = "Weitere_Angebote_Betreuung")
    private Boolean weitereAngeboteBetreuung;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return angebotsId != null && angebotsId.equals(filter.angebotsId);
    }

    @Override
    public int hashCode() {
        return angebotsId != null ? angebotsId.hashCode() : getClass().hashCode();
    }
}
