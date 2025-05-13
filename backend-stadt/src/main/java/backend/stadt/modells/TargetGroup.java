package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "TargetGroup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TargetGroup {

    @Id
    @Column(name = "OfferId")
    private Integer offerId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "OfferId")
    private Offer offer;

    @Column(name = "Parents")
    private Boolean parents;

    @Column(name = "Kids")
    private Boolean kids;

    @Column(name = "Teenagers")
    private Boolean teenagers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TargetGroup that = (TargetGroup) o;
        return offerId != null && offerId.equals(that.offerId);
    }

    @Override
    public int hashCode() {
        return offerId != null ? offerId.hashCode() : getClass().hashCode();
    }
}