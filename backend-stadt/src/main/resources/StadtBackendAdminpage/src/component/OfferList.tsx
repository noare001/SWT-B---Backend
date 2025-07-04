import { useEffect, useState } from "react";

export default function OfferList() {
    const [search, setSearch] = useState<string>("");
    const [onlyProcessing, setOnlyProcessing] = useState<boolean>(false);
    const [offers, setOffers] = useState([]);

    useEffect(() => {
        fetchOffers();
    }, []);

    function fetchOffers() {
        fetch("/admin/offer")
            .then((res) => res.json())
            .then((data) => setOffers(data));
    }

    function filter(offer): boolean {
        const q = search.toLowerCase();

        const matchesSearch =
            (offer.name && offer.name.toLowerCase().includes(q)) ||
            (offer.city && offer.city.toLowerCase().includes(q)) ||
            (offer.offerId && offer.offerId.toString().includes(q));

        const matchesProcessing = !onlyProcessing || offer.status === "PROCESSING";

        return matchesSearch && matchesProcessing;
    }

    async function updateStatus(offerId: number, accepted: boolean) {
        await fetch(`/admin/offer?accepted=${accepted}&id=${offerId}`, {
            method: "POST",
        });
        fetchOffers(); // Liste neu laden
    }

    return (
        <div>
            <h2>Offer Liste</h2>
            <div style={{ display: "flex", gap: "20px", marginBottom: "10px" }}>
                <input
                    type="text"
                    placeholder="Suche nach Offer"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
                <div>
                    <label className="switch">
                        <input type="checkbox" checked={onlyProcessing}
                               onChange={() => setOnlyProcessing(!onlyProcessing)}/>
                        <span className="slider round"></span>
                    </label>
                    (Processing only)
                </div>

            </div>
            <div>
                {offers.filter(o => filter(o)).map(o => (
                    <div key={o.offerId} className="offer-card">
                        <div className="offer-summary">
                            <span><strong>#{o.offerId}</strong> – {o.name}</span>
                            <span>{o.city ?? "-"}</span>
                            <span>{o.startDate ?? "-"} – {o.endDate ?? "-"}</span>
                        </div>
                        <div className="offer-details">
                            <div><strong>Provider:</strong> {o.providerName}</div>
                            <div><strong>PLZ:</strong> {o.postalCode}</div>
                            <div><strong>Straße:</strong> {o.street}</div>
                            <div><strong>Registrierung:</strong> {o.registrationRequired ? "Ja" : "Nein"}</div>
                            <div><strong>Kosten:</strong> {o.cost ?? "0"} €</div>
                            <div><strong>Sprachen:</strong> {o.languages?.join(", ") ?? "-"}</div>
                            <div><strong>Zielgruppen:</strong> {o.targetGroups?.join(", ") ?? "-"}</div>
                            <div><strong>Weitere Infos:</strong> {o.additionalInformation ?? "—"}</div>
                            <div><strong>Typen:</strong> {o.offerTypes?.join(", ") ?? "-"}</div>
                            <div><strong>Filter:</strong> {o.filters?.join(", ") ?? "-"}</div>

                            {o.status === "PROCESSING" && (
                                <div style={{ marginTop: "10px" }}>
                                    <button onClick={() => updateStatus(o.offerId, true)}>
                                        ✅ Akzeptieren
                                    </button>
                                    <button onClick={() => updateStatus(o.offerId, false)} style={{ marginLeft: "10px" }}>
                                        ❌ Ablehnen
                                    </button>
                                </div>
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
