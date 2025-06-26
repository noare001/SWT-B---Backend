import { useEffect, useState, useMemo } from "react";
import './user.css';

interface AppUser {
    id: number;
    name: string;
    email: string;
    role: "USER" | "AUTHOR";
    providerId: number | null;
}

interface Provider {
    id: number;
    name: string;
}

export default function UserList() {
    const [users, setUsers] = useState<AppUser[]>([]);
    const [providers, setProviders] = useState<Provider[]>([]);

    useEffect(() => {
        fetch("/admin/user")
            .then(res => res.json())
            .then(setUsers);

        fetch("/admin/provider")
            .then(res => res.json())
            .then(setProviders);
    }, []);

    const providerMap = useMemo(() => {
        const map = new Map<number, Provider>();
        for (const p of providers) {
            map.set(p.id, p);
        }
        return map;
    }, [providers]);

    function updateUser(userId: number, changes: Partial<AppUser>) {
        setUsers(prev =>
            prev.map(u => (u.id === userId ? { ...u, ...changes } : u))
        );
    }

    async function saveChanges(user: AppUser) {
        await fetch(`/admin/user/${user.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        // Optional: reload user list
        const updated = await fetch("/admin/user").then(res => res.json());
        setUsers(updated);
    }

    return (
        <div>
            <h2>User Verwaltung</h2>
            <table className="user-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Rolle</th>
                    <th>Provider</th>
                    <th>Aktion</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td>{user.id}</td>
                        <td>{user.name}</td>
                        <td>{user.email}</td>
                        <td>
                            <select
                                value={user.role}
                                onChange={e =>
                                    updateUser(user.id, {
                                        role: e.target.value as AppUser["role"],
                                        providerId: e.target.value === "AUTHOR" ? user.providerId : null
                                    })
                                }
                            >
                                <option value="USER">USER</option>
                                <option value="AUTHOR">AUTHOR</option>
                            </select>
                        </td>
                        <td>
                            {user.role === "AUTHOR" ? (
                                <select
                                    value={user.providerId ?? ""}
                                    onChange={e => {
                                        const id = e.target.value ? Number(e.target.value) : null;
                                        updateUser(user.id, { providerId: id });
                                    }}
                                >
                                    <option value="">–</option>
                                    {providers.map(p => (
                                        <option key={p.id} value={p.id}>
                                            {p.name}
                                        </option>
                                    ))}
                                </select>
                            ) : (
                                <em>–</em>
                            )}
                        </td>
                        <td>
                            <button onClick={() => saveChanges(user)}>Speichern</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
