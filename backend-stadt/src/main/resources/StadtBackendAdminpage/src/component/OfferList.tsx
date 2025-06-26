import {use, useEffect, useState} from "react";

export default function OfferList(){
    const [search, setSearch] = useState<string>("")
    const [offers, setOffers] = useState([])

    useEffect(() => {
        fetch("/admin/offer")
            .then((res) => res.json())
            .then((data) => setOffers(data));
    }, []);

    function filter(offer) :boolean {
        const q = search.toLowerCase();
        return (
            (offer.name && offer.name.toLowerCase().includes(q)) ||
            (offer.city && offer.city.toLowerCase().includes(q)) ||
            (offer.offerId && offer.offerId.toString().includes(q))
        );
    }

    return (
        <div>
            <h2>Offer Liste</h2>
            <input
                type="text"
                placeholder="Suche nach Offer"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
            />
            <div>
                {offers.filter(o => filter(o)).map(o => (
                    <div key={o.offerId} className="offer-card">
                        <div className="offer-summary">
                            <span><strong>#{o.offerId}</strong> – {o.name}</span>
                            <span>{o.city ?? "-"}</span>
                            <span>{o.startDate ?? "-"} – {o.endDate ?? "-"}</span>
                        </div>
                        <div className="offer-details">
                            <div>
                                <strong>Provider ID:</strong> {o.providerId}
                            </div>
                            <div>
                                <strong>PLZ:</strong> {o.postalCode}
                            </div>
                            <div>
                                <strong>Straße:</strong> {o.street}
                            </div>
                            <div>
                                <strong>Registrierung:</strong> {o.registrationRequired ? "Ja" : "Nein"}
                            </div>
                            <div>
                                <strong>Kosten:</strong> {o.cost ?? "0"} €
                            </div>
                            <div>
                                <strong>Sprachen:</strong> {o.languages?.join(", ") ?? "-"}
                            </div>
                            <div>
                                <strong>Zielgruppen:</strong> {o.targetGroups?.join(", ") ?? "-"}
                            </div>
                            <div>
                                <strong>Weitere Infos:</strong> {o.additionalInformation ?? "—"}
                            </div>
                            <div>
                                <strong>Typen:</strong> {o.offerTypes?.join(", ") ?? "-"}
                            </div>
                        </div>
                    </div>
                ))}
            </div>

        </div>
    )
}