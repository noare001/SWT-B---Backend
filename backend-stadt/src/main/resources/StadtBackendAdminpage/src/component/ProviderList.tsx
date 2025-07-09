import { useEffect, useState } from "react";

export default function ProviderList() {
    const [search, setSearch] = useState("");
    const [providers, setProviders] = useState([]);

    useEffect(() => {
        fetch("/admin/provider")
            .then((res) => res.json())
            .then((data) => setProviders(data));
    }, []);

    function filter(provider: any): boolean {
        const q = search.toLowerCase();

        return Object.values(provider).some(value => {
            if (value == null) return false;

            if (typeof value === "string" || typeof value === "number") {
                return value.toString().toLowerCase().includes(q);
            }

            if (Array.isArray(value) || typeof value === "object") {
                return Object.values(value).some(v =>
                    v?.toString().toLowerCase().includes(q)
                );
            }

            return false;
        });
    }

    return (
        <div>
            <h2>Provider Liste</h2>
            <input
                type="text"
                placeholder="Suche nach Provider"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
            />
            <div className="provider-list">
                {providers.filter(filter).map((p: any) => (
                    <div
                        key={p.id}
                        className="offer-card"
                        onClick={(e) => e.currentTarget.classList.toggle("expanded")}
                    >
                        <div className="offer-summary">
                            <span><strong>#{p.id}</strong> – {p.name}</span>
                            <span>{p.city ?? "-"}</span>
                            <span>{p.email ?? "-"}</span>
                        </div>
                        <div className="offer-details">
                            <div><strong>Straße:</strong> {p.street}</div>
                            <div><strong>PLZ:</strong> {p.postalCode}</div>
                            <div><strong>Kontakt:</strong> {p.contactPerson}</div>
                            <div><strong>Telefon:</strong> {p.phoneNumber}</div>
                            <div><strong>Link:</strong> <a href={p.link}>{p.link}</a></div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
