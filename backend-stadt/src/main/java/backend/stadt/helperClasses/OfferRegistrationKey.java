package backend.stadt.helperClasses;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferRegistrationKey implements Serializable {
    private Long userId;
    private Integer offerId;

    // equals() und hashCode() implementieren!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferRegistrationKey)) return false;
        OfferRegistrationKey that = (OfferRegistrationKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(offerId, that.offerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, offerId);
    }
}

