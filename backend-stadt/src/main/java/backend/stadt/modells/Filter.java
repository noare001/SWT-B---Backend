package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Filter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    @Id
    @Column(name = "OfferId")
    private Integer offerId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "OfferId")
    private Offer offer;

    @Column(name = "YouthCenters")
    private Boolean youthCenters;

    @Column(name = "HolidayOffers")
    private Boolean holidayOffers;

    @Column(name = "PlayLearnAndExperience")
    private Boolean playLearnAndExperience;

    @Column(name = "SportAndExercise")
    private Boolean sportAndExercise;

    @Column(name = "EngagementAndVolunteering")
    private Boolean engagementAndVolunteering;

    @Column(name = "CreativityAndCulture")
    private Boolean creativityAndCulture;

    @Column(name = "ParksAndPlaygrounds")
    private Boolean parksAndPlaygrounds;

    @Column(name = "FestivalsAndMarkets")
    private Boolean festivalsAndMarkets;

    @Column(name = "OtherOffersLeisure")
    private Boolean otherOffersLeisure;

    @Column(name = "Daycare")
    private Boolean daycare;

    @Column(name = "EmergencyCare")
    private Boolean emergencyCare;

    @Column(name = "Babysitter")
    private Boolean babysitter;

    @Column(name = "OtherOffersCare")
    private Boolean otherOffersCare;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return offerId != null && offerId.equals(filter.offerId);
    }

    @Override
    public int hashCode() {
        return offerId != null ? offerId.hashCode() : getClass().hashCode();
    }
}
