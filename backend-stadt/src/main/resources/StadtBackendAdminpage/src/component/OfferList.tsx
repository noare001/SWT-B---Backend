import { useEffect, useState } from "react";
import './offer.css'


interface EventScheduleEntry {
    startTime: string;
    endTime: string;
}
type Weekday = "MONDAY" | "TUESDAY" | "WEDNESDAY" | "THURSDAY" | "FRIDAY" | "SATURDAY" | "SUNDAY";
type EventSchedule = Partial<Record<Weekday, EventScheduleEntry>>;
interface Offer {
    offerId: number;
    name: string;
    street: string;
    city: string;
    postalCode: number;
    offerTypes: string[];
    targetGroups: string[];
    recurring: boolean;
    startDate: string;
    endDate: string;
    eventSchedule: EventSchedule;
    registrationRequired: boolean;
    additionalInformation: string;
    cost: number;
    filters: string[];
    status: string;
    minAge: number;
    maxAge: number;
    languages: string[];
    providerName: string;
}

export default function OfferList() {
    const [search, setSearch] = useState<string>("");
    const [onlyProcessing, setOnlyProcessing] = useState<boolean>(false);
    const [offers, setOffers] = useState<Offer[]>([]);

    useEffect(() => {
        fetchOffers();
    }, []);

    function fetchOffers() {
        fetch("/admin/offer")
            .then((res) => res.json())
            .then((data) => setOffers(data));
    }

    function filter(offer:Offer): boolean{
        const q = search.toLowerCase();

        const matchesSearch = [
            offer.name?.toLowerCase(),
            offer.city?.toLowerCase(),
            offer.offerId?.toString()
        ].some(field => field?.includes(q));

        const matchesProcessing = !onlyProcessing || offer.status === "PROCESSING";

        return matchesSearch && matchesProcessing;
    }

    function getActionButtons(offer:Offer){
        if(offer.status === "PROCESSING"){
            return (
                <>
                    <button onClick={() => updateStatus(offer.offerId, true)}>
                        ✅ Akzeptieren
                    </button>
                    <button onClick={() => deleteOffer(offer.offerId)} style={{marginLeft: "0"}}>
                        ❌ Ablehnen
                    </button>
                </>
            )
        }else{
            return (
                <>
                    <button onClick={() => {
                            updateStatus(offer.offerId, false);
                    }} className={"edit-button"}>↩</button>
                    <button
                        onClick={() => {
                            if (window.confirm(`Möchten Sie das Angebot wirklich löschen?`)) {
                                deleteOffer(offer.offerId);
                            }
                        }} className="delete-button"
                    >X</button>
                </>

            )
        }
    }

    function updateStatus(offerId: number, accepted: boolean) {
        fetch(`/admin/offer?accepted=${accepted}&id=${offerId}`, {
            method: "POST",
        }).then(() => fetchOffers());
    }

    function deleteOffer(offerId: number) {
        fetch(`/admin/offer?id=${offerId}`, {
            method: "DELETE",
        }).then(() => fetchOffers());
    }

    return (
        <div>
            <h2>Offer Liste</h2>
            <div style={{display: "flex", gap: "20px", marginBottom: "10px" }}>
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
                            <span>
                                <span>{o.startDate ?? "-"} – {o.endDate ?? "-"}</span>
                                {getActionButtons(o)}
                            </span>
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


                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
