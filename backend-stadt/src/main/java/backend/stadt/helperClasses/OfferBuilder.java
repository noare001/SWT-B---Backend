package backend.stadt.helperClasses;

import backend.stadt.enums.OfferStatus;
import backend.stadt.modells.Offer;
import backend.stadt.user.AppUser;
import backend.stadt.user.Role;

public class OfferBuilder {

    private final Offer offer;

    private OfferBuilder(Offer template) {
        this.offer = template;
    }

    public static OfferBuilder fromTemplate(Offer template) {
        if (template == null) throw new IllegalArgumentException("Template darf nicht null sein");
        return new OfferBuilder(template);
    }

    public OfferBuilder withProvider(AppUser user) {
        if (user == null || !Role.AUTHOR.equals(user.getRole())) {
            System.err.println(("Nur Autoren dürfen Angebote erstellen"));
        }
        if (user.getProvider() == null) {
            System.err.println(("Benutzer hat keinen Anbieter verknüpft"));
        }
        offer.setProvider(user.getProvider());
        return this;
    }

    public OfferBuilder withDefaultStatus() {
        offer.setStatus(OfferStatus.PROCESSING);
        return this;
    }

    public OfferBuilder withStatus(OfferStatus status) {
        offer.setStatus(status);
        return this;
    }

    public Offer build() {
        if (offer.getProvider() == null) {
            System.err.println(("Anbieter wurde nicht gesetzt"));
        }
        return offer;
    }
}

