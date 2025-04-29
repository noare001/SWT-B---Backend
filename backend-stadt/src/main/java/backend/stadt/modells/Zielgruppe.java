package backend.stadt.modells;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "Zielgruppe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Zielgruppe {

    @Id
    @Column(name = "AngebotsId")
    private Integer angebotsId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "AngebotsId")
    private Angebot angebot;

    @Column(name = "Eltern")
    private Boolean eltern;

    @Column(name = "Kinder")
    private Boolean kinder;

    @Column(name = "Jugendliche")
    private Boolean jugendliche;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zielgruppe that = (Zielgruppe) o;
        return angebotsId != null && angebotsId.equals(that.angebotsId);
    }

    @Override
    public int hashCode() {
        return angebotsId != null ? angebotsId.hashCode() : getClass().hashCode();
    }
}