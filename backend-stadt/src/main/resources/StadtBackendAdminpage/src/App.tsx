import { useEffect, useState } from "react";
import "./App.css";
import OfferList from "./component/OfferList.tsx";
import ProviderList from "./component/ProviderList.tsx";
import UserList from "./component/UserList.tsx";

function App() {
    const [search, setSearch] = useState("");
    const [render, setRender] = useState<"offer" | "user" | "anfragen" | "provider">("user")

    const [isConnected, setIsConnected] = useState(false);

    useEffect(() => {
        fetch("/admin/status")
            .then(res => res.ok ? setIsConnected(true) : setIsConnected(false))
            .catch(() => setIsConnected(false));
    }, []);

    const renderContent = () => {
        switch (render) {
            case "anfragen":
                return <p>(Noch nicht implementiert)</p>;
            case "offer":
                return <OfferList/>;
            case "user":
                return <UserList/>;
            case "provider":
                return <ProviderList/>;
            default:
                return <p>Unbekannter Inhalt</p>;
        }
    };

    return (
        <div className="App">
            <header className="header">
                <h1>Administration</h1>
                <div className="mqtt-status">
                    <h3>Mqtt Connection:</h3>
                    <p className={isConnected ? "status success" : "status error"}>
                        {isConnected ? "Verbunden!" : "Keine Verbindung"}
                    </p>
                </div>
            </header>
            <main>
                <section className="buttons">
                    <button onClick={() => setRender("user")}>User Liste</button>
                    <button onClick={() => setRender("offer")}>Offer Liste</button>
                    <button onClick={() => setRender("provider")}>Provider Liste</button>
                    <button onClick="checkStatus()">Anfragen Liste</button>
                </section>
                <section className="content-section">{renderContent()}</section>
            </main>
        </div>
    );
}

export default App;