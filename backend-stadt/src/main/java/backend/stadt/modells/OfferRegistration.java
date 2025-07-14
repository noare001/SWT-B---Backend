package backend.stadt.modells;

import backend.stadt.enums.RegistrationStatus;
import backend.stadt.helperClasses.OfferRegistrationKey;
import backend.stadt.user.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_offer_registration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferRegistration {

    @EmbeddedId
    private OfferRegistrationKey id = new OfferRegistrationKey();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private AppUser user;

    @ManyToOne
    @MapsId("offerId")
    @JoinColumn(name = "offer_id")
    @JsonIgnore
    private Offer offer;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    private LocalDate registeredAt = LocalDate.now();

    @JsonProperty("offerId")
    public Integer getOfferId(){
        return offer.getOfferId();
    }
    @JsonProperty("userId")
    public Integer getUserId(){
        return user.getId();
    }
}

