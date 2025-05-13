package backend.stadt.modells;

import backend.stadt.modells.Offer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Languages {

    @Id
    @Column(name = "OfferId")
    private Integer offerId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "OfferId")
    private Offer offer;

    @Column(name = "German")
    private Boolean german;

    @Column(name = "Arabic")
    private Boolean arabic;

    @Column(name = "BosnianCroatianSerbian")
    private Boolean bosnianCroatianSerbian;

    @Column(name = "Bulgarian")
    private Boolean bulgarian;

    @Column(name = "Chinese")
    private Boolean chinese;

    @Column(name = "Dari")
    private Boolean dari;

    @Column(name = "English")
    private Boolean english;

    @Column(name = "French")
    private Boolean french;

    @Column(name = "Greek")
    private Boolean greek;

    @Column(name = "Italian")
    private Boolean italian;

    @Column(name = "Polish")
    private Boolean polish;

    @Column(name = "Portuguese")
    private Boolean portuguese;

    @Column(name = "Romani")
    private Boolean romani;

    @Column(name = "Romanian")
    private Boolean romanian;

    @Column(name = "Russian")
    private Boolean russian;

    @Column(name = "Albanian")
    private Boolean albanian;

    @Column(name = "Spanish")
    private Boolean spanish;

    @Column(name = "Turkish")
    private Boolean turkish;

    @Column(name = "Ukrainian")
    private Boolean ukrainian;

    @Column(name = "Urdu")
    private Boolean urdu;

    @Lob
    @Column(name = "Other_Language", columnDefinition = "TEXT")
    private String otherLanguage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Languages languages = (Languages) o;
        return offerId != null && offerId.equals(languages.offerId);
    }

    @Override
    public int hashCode() {
        return offerId != null ? offerId.hashCode() : getClass().hashCode();
    }
}