package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "OfferType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferType {

    @Id
    @Column(name = "OfferId")
    private Integer offerId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "OfferId")
    private Offer offer;

    @Column(name = "GroupOffer")
    private Boolean groupOffer;

    @Column(name = "Event")
    private Boolean event;

    @Column(name = "CourseWorkshop")
    private Boolean courseWorkshop;

    @Column(name = "Consultation")
    private Boolean consultation;

    @Column(name = "MedicalConsultation")
    private Boolean medicalConsultation;

    @Column(name = "OnlineOffer")
    private Boolean onlineOffer;

    @Lob
    @Column(name = "OtherOfferType")
    private String otherOfferType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfferType that = (OfferType) o;
        // Wichtig: Vergleich über die ID, die vom Angebot gemappt wird
        return offerId != null && offerId.equals(that.offerId);
    }

    @Override
    public int hashCode() {
        // Der Hashcode sollte auf der ID basieren
        // Da die ID erst nach dem Setzen der Beziehung/Persistieren gesetzt wird,
        // ist es sicherer, eine Konstante oder getClass().hashCode() zu verwenden,
        // solange die ID null ist, um Probleme in Collections zu vermeiden.
        return offerId != null ? offerId.hashCode() : getClass().hashCode();
    }
}