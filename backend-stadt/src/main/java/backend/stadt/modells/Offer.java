package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Offer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OfferId")
    private Integer offerId;

    @Column(name = "EditingFinished")
    private Boolean editingFinished;

    @Column(name = "Published")
    private Boolean published;

    @Column(name = "Name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProviderID", referencedColumnName = "ID")
    private Provider provider;

    @OneToOne(mappedBy = "offer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
    private OfferType offerType;

    @OneToOne(mappedBy = "offer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
    private TargetGroup targetGroup;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Column(name = "StartTime")
    private LocalTime startTime;

    @Column(name = "EndTime")
    private LocalTime endTime;

    @Column(name = "RegistrationRequired")
    private Boolean registrationRequired;

    @Lob
    @Column(name = "AdditionalInformation")
    private String additionalInformation;

    @Column(name = "RegistrationLink")
    private String registrationLink;

    @Column(name = "Cost")
    private Double cost; // Besser: BigDecimal für Währungen

    @OneToOne(mappedBy = "offer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
    private Filter filter;

    @OneToOne(mappedBy = "offer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
    private Languages languages;

    @Column(name = "MinAge")
    private Integer minAge;

    @Column(name = "MaxAge")
    private Integer maxAge;

    // Convenience Methoden (optional)
    public void setOfferTypeDetails(OfferType offerType) {
        if (offerType != null) {
            offerType.setOffer(this);
        }
        this.offerType = offerType;
    }

    public void setTargetGroupDetails(TargetGroup targetGroup) {
        if (targetGroup != null) {
            targetGroup.setOffer(this);
        }
        this.targetGroup = targetGroup;
    }

    public void setFilterDetails(Filter filter) {
        if (filter != null) {
            filter.setOffer(this);
        }
        this.filter = filter;
    }

    public void setLanguagesDetails(Languages languages) {
        if (languages != null) {
            languages.setOffer(this);
        }
        this.languages = languages;
    }

    // equals() und hashCode() basierend auf angebotsId empfohlen
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        // Wenn die ID null ist (noch nicht persistiert), sind Objekte nur gleich, wenn sie dieselbe Instanz sind.
        // Ansonsten Vergleich über die ID.
        return offerId != null && offerId.equals(offer.offerId);
    }

    @Override
    public int hashCode() {
        // Wenn die ID null ist, gibt super.hashCode() zurück (oder eine Konstante).
        // Nach dem Persistieren basiert der Hashcode auf der ID.
        return offerId != null ? offerId.hashCode() : super.hashCode();
    }
}
