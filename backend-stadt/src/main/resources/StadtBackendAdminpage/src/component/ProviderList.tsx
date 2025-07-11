import { useEffect, useState } from "react";
import './provider.css';

interface Provider{
    name: string;
    city: string;
    street: string;
    postalCode: string;
    contactPerson: string;
    phoneNumber: string;
    email: string;
    link: string;

}

export default function ProviderList() {
    const [search, setSearch] = useState("");
    const [providers, setProviders] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [newProvider, setNewProvider] = useState<Provider>({
        name: "",
        city: "",
        street: "",
        postalCode: "",
        contactPerson: "",
        phoneNumber: "",
        email: "",
        link: ""
    });

    useEffect(() => {
        getProvider()
    }, []);

    function getProvider(){
        fetch("/admin/provider")
            .then((res) => res.json())
            .then((data) => setProviders(data));
    }

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

    function handleInputChange(e: React.ChangeEvent<HTMLInputElement>) {
        const { name, value } = e.target;
        setNewProvider(prev => ({ ...prev, [name]: value }));
    }

    function handleFormSubmit(e: React.FormEvent) {
        e.preventDefault();

        fetch("/admin/provider", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(newProvider)
        })
            .then(res => res.ok ? res.json() : Promise.reject("Fehler beim Speichern"))
            .then(() => {
                setShowForm(false);
                setNewProvider({
                    name: "", city: "", street: "", postalCode: "", contactPerson: "",
                    phoneNumber: "", email: "", link: ""
                });
                getProvider()
            })
            .catch(err => alert(err));
    }
    function deleteProvider(id: number){
        fetch(`/admin/provider/${id}`, {
            method: "DELETE"
        })
            .then(res => res.ok ? res.json() : Promise.reject("Fehler beim Speichern"))
            .then(getProvider)
    }

    return (
        <div>
            <h2>Provider Liste</h2>
            <div className={"provicer-actions"}>
                <input
                    type="text"
                    placeholder="Suche nach Provider"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
                <button onClick={() => setShowForm(prev => !prev)}>
                    {showForm ? "Abbrechen" : "Add"}
                </button>
            </div>

            {showForm && (
                <form className="provider-form" onSubmit={handleFormSubmit}>
                    <input name="name" placeholder="Name" value={newProvider.name} onChange={handleInputChange} required />
                    <input name="city" placeholder="Stadt" value={newProvider.city} onChange={handleInputChange} />
                    <input name="street" placeholder="Straße" value={newProvider.street} onChange={handleInputChange} />
                    <input name="postalCode" placeholder="PLZ" value={newProvider.postalCode} onChange={handleInputChange} />
                    <input name="contactPerson" placeholder="Kontaktperson" value={newProvider.contactPerson} onChange={handleInputChange} />
                    <input name="phoneNumber" placeholder="Telefon" value={newProvider.phoneNumber} onChange={handleInputChange} />
                    <input name="email" type="email" placeholder="E-Mail" value={newProvider.email} onChange={handleInputChange} />
                    <input name="link" placeholder="Link" value={newProvider.link} onChange={handleInputChange} />
                    <button type="submit">Speichern</button>
                </form>
            )}

            <div className="provider-list">
                {providers.filter(filter).map((p: any) => (
                    <div
                        key={p.id}
                        className="data-card"
                        onClick={(e) => e.currentTarget.classList.toggle("expanded")}
                    >
                        <div className="data-summary">
                            <span><strong>#{p.id}</strong> – {p.name}</span>
                            <span>{p.city ?? "-"}</span>
                            <span>
                                <span>{p.email ?? "-"}</span>
                                <button className="delete-button" onClick={() =>deleteProvider(p.id)}>X</button>
                            </span>
                        </div>
                        <div className="data-details">
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
