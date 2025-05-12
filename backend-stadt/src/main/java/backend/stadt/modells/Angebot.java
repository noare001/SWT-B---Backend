package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
// import java.time.LocalDate; // Falls Sie diese verwenden
// import java.time.LocalTime; // Falls Sie diese verwenden
// import java.math.BigDecimal; // Falls Sie diese verwenden

@Entity
@Table(name = "Angebot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Angebot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AngebotsId")
    private Integer angebotsId;

    @Column(name = "BearbeitungAbgeschlossen")
    private Boolean bearbeitungAbgeschlossen;

    @Column(name = "Veröffentlichen")
    private Boolean veroeffentlichen;

    @Column(name = "AngebotsName")
    private String angebotsName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AnbieterID", referencedColumnName = "ID")
    private Anbieter anbieter;

    @OneToOne(mappedBy = "angebot", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
    private AngebotsArt angebotsArt;

    @OneToOne(mappedBy = "angebot", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
    private Zielgruppe zielgruppe;

    @Column(name = "StartDatum")
    private String startDatum; // Besser: LocalDate

    @Column(name = "EndDatum")
    private String endDatum; // Besser: LocalDate

    @Column(name = "StartZeit")
    private String startZeit; // Besser: LocalTime

    @Column(name = "EndZeit")
    private String endZeit; // Besser: LocalTime

    @Column(name = "AnmeldungErforderlich")
    private Boolean anmeldungErforderlich;

    @Lob
    @Column(name = "WeitereAngaben")
    private String weitereAngaben;

    @Column(name = "AnmeldeLink")
    private String anmeldeLink;

    @Column(name = "Kosten")
    private Double kosten; // Besser: BigDecimal für Währungen

    @Column(name = "Barrierefreiheit")
    private Integer barrierefreiheitId; // FK zu einer nicht definierten Tabelle? Oder Enum?

    @OneToOne(mappedBy = "angebot", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
    private Filter filter;

    @OneToOne(mappedBy = "angebot", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
    private Sprachen sprachen;

    @Column(name = "Min_Alter")
    private Integer minAlter;

    @Column(name = "Max_Alter")
    private Integer maxAlter;

    // Convenience Methoden (optional)
    public void setAngebotsArtDetails(AngebotsArt angebotsArt) {
        if (angebotsArt != null) {
            angebotsArt.setAngebot(this);
        }
        this.angebotsArt = angebotsArt;
    }

    public void setZielgruppeDetails(Zielgruppe zielgruppe) {
        if (zielgruppe != null) {
            zielgruppe.setAngebot(this);
        }
        this.zielgruppe = zielgruppe;
    }

    public void setFilterDetails(Filter filter) {
        if (filter != null) {
            filter.setAngebot(this);
        }
        this.filter = filter;
    }

    public void setSprachenDetails(Sprachen sprachen) {
        if (sprachen != null) {
            sprachen.setAngebot(this);
        }
        this.sprachen = sprachen;
    }

    // equals() und hashCode() basierend auf angebotsId empfohlen
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Angebot angebot = (Angebot) o;
        // Wenn die ID null ist (noch nicht persistiert), sind Objekte nur gleich, wenn sie dieselbe Instanz sind.
        // Ansonsten Vergleich über die ID.
        return angebotsId != null && angebotsId.equals(angebot.angebotsId);
    }

    @Override
    public int hashCode() {
        // Wenn die ID null ist, gibt super.hashCode() zurück (oder eine Konstante).
        // Nach dem Persistieren basiert der Hashcode auf der ID.
        return angebotsId != null ? angebotsId.hashCode() : super.hashCode();
    }
}
