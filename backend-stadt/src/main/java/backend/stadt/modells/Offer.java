package backend.stadt.modells;

import backend.stadt.enums.FilterCategory;
import backend.stadt.enums.OfferStatus;
import backend.stadt.enums.OfferType;
import backend.stadt.enums.TargetAudience;
import backend.stadt.helperClasses.TimeRange;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

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
    @JsonIgnore
    private Provider provider;

    @ElementCollection(targetClass = OfferType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "Offer_OfferTypes", joinColumns = @JoinColumn(name = "OfferId"))
    @Column(name = "OfferType")
    private Set<OfferType> offerTypes;

    @ElementCollection(targetClass = TargetAudience.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "OfferTargetGroups", joinColumns = @JoinColumn(name = "OfferId"))
    @Column(name = "Audience")
    private Set<TargetAudience> targetGroups;

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

    @Column(name = "Cost")
    private Double cost;

    @ElementCollection(targetClass = FilterCategory.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "OfferFilters", joinColumns = @JoinColumn(name = "OfferId"))
    @Column(name = "FilterCategory")
    private Set<FilterCategory> filters;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private OfferStatus status;

    @Column(name = "MinAge")
    private Integer minAge;

    @Column(name = "MaxAge")
    private Integer maxAge;

    @ElementCollection
    @CollectionTable(name = "OfferLanguages", joinColumns = @JoinColumn(name = "OfferId"))
    @Column(name = "Languages")
    private Set<String> languages;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    private Set<OfferRegistration> registrations = new HashSet<>();

    @JsonProperty("providerName")
    public String getProviderName(){
        return provider.getName();
    }
}
