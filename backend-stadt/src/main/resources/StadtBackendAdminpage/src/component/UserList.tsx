import { useEffect, useState } from "react";
import './user.css';

interface AppUser {
    id: number;
    name: string;
    email: string;
    role: "USER" | "AUTHOR";
    providerId: number | null;
    changed: boolean;
}

interface Provider {
    id: number;
    name: string;
}

export default function UserList() {
    const [users, setUsers] = useState<AppUser[]>([]);
    const [providers, setProviders] = useState<Provider[]>([]);
    const [validationErrors, setValidationErrors] = useState<{ [key: number]: string }>({});

    useEffect(() => {
        getUser();

        fetch("/admin/provider")
            .then(res => res.json())
            .then(setProviders);
    }, []);

    function updateUser(userId: number, changes: Partial<AppUser>) {
        setUsers(prev =>
            prev.map(u => (u.id === userId ? { ...u, ...changes } : u))
        );

        // Reset validation error if role or provider was changed
        setValidationErrors(prev => {
            const updated = { ...prev };
            delete updated[userId];
            return updated;
        });
    }

    function deleteUser(userId: number) {
        fetch("/admin/user/" + userId, {
            method: "DELETE",
        }).then(() => getUser());
    }

    async function saveChanges(user: AppUser) {
        if (user.role === "AUTHOR" && user.providerId === null) {
            setValidationErrors(prev => ({
                ...prev,
                [user.id]: "Bitte wählen Sie einen Provider aus."
            }));
            return;
        }

        await fetch(`/admin/user/${user.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ ...user, changed: undefined }) // avoid sending the `changed` flag
        });

        getUser();
    }

    function getUser() {
        fetch("/admin/user")
            .then(res => res.json())
            .then(data => {
                // Add "changed: false" on initial load
                setUsers(data.map((u: AppUser) => ({ ...u, changed: false })));
            });
    }

    return (
        <div>
            <h2>User Verwaltung</h2>
            <div className="user-grid">
                <div className="header">ID</div>
                <div className="header">Name</div>
                <div className="header">Email</div>
                <div className="header">Rolle</div>
                <div className="header">Provider</div>
                <div className="header">Aktion</div>
                {users.map(user => {
                    const hasValidationError = !!validationErrors[user.id];
                    return (
                        <>
                            <div>{user.id}</div>
                            <div>{user.name}</div>
                            <div>{user.email}</div>
                            <div>
                                <select
                                    value={user.role}
                                    onChange={e =>
                                        updateUser(user.id, {
                                            role: e.target.value as AppUser["role"],
                                            providerId: e.target.value === "AUTHOR" ? user.providerId : null,
                                            changed: true
                                        })
                                    }
                                >
                                    <option value="USER">USER</option>
                                    <option value="AUTHOR">AUTHOR</option>
                                </select>
                            </div>
                            <div>
                                {user.role === "AUTHOR" ? (
                                    <select
                                        value={user.providerId ?? ""}
                                        onChange={e => {
                                            const id = e.target.value ? Number(e.target.value) : null;
                                            updateUser(user.id, {providerId: id, changed: true});
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
                            </div>
                            <div>
                                <button
                                    onClick={() => saveChanges(user)}
                                    disabled={!user.changed}
                                >
                                    Speichern
                                </button>
                                {hasValidationError && (
                                    <div className="validation-error">{validationErrors[user.id]}</div>
                                )}
                                {(!hasValidationError && user.changed) &&
                                    <span className="unsaved-note">*Nicht gespeichert</span>}
                                <button
                                    onClick={() => {
                                        if (window.confirm(`Möchten Sie den Benutzer "${user.name}" wirklich löschen?`)) {
                                            deleteUser(user.id);
                                        }
                                    }}
                                    className="delete-button"
                                >
                                    X
                                </button>
                            </div>
                        </>
                    );
                })}
            </div>
        </div>
    );
}
