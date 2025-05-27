package backend.stadt.modells;

import backend.stadt.enums.FilterCategory;
import backend.stadt.enums.OfferType;
import backend.stadt.enums.TargetAudience;
import backend.stadt.helperClasses.TimeRange;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Column(name = "Street")
    private String street;

    @Column(name = "City")
    private String city;

    @Column(name = "PostalCode")
    private Integer postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProviderID", referencedColumnName = "ID")
    private Provider provider;

    @ElementCollection(targetClass = OfferType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "Offer_OfferTypes", joinColumns = @JoinColumn(name = "OfferId"))
    @Column(name = "OfferType")
    private Set<OfferType> offerTypes;

    @Column(name = "OtherOfferType")
    private String otherOfferType;

    @ElementCollection(targetClass = TargetAudience.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "OfferTargetGroups", joinColumns = @JoinColumn(name = "OfferId"))
    @Column(name = "Audience")
    private List<TargetAudience> targetGroups;

    @Column(name = "Recurring")
    private Boolean recurring;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @ElementCollection
    @CollectionTable(name = "OfferEventSchedule", joinColumns = @JoinColumn(name = "OfferId"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "DayOfWeek")
    @Enumerated(EnumType.STRING)
    @Column(name = "TimeRange")
    private Map<DayOfWeek, TimeRange> eventSchedule = new EnumMap<>(DayOfWeek.class);


    @Column(name = "RegistrationRequired")
    private Boolean registrationRequired;

    @Lob
    @Column(name = "AdditionalInformation")
    private String additionalInformation;

    @Column(name = "RegistrationLink")
    private String registrationLink;

    @Column(name = "Cost")
    private Double cost; // Besser: BigDecimal für Währungen

    @ElementCollection(targetClass = FilterCategory.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "OfferFilters", joinColumns = @JoinColumn(name = "OfferId"))
    @Column(name = "FilterCategory")
    private Set<FilterCategory> filters;

    @Column(name = "MinAge")
    private Integer minAge;

    @Column(name = "MaxAge")
    private Integer maxAge;

    @ElementCollection
    @CollectionTable(name = "OfferLanguages", joinColumns = @JoinColumn(name = "OfferId"))
    @Column(name = "Languages")
    private List<String> languages;


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
