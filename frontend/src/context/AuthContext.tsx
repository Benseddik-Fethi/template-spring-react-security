import { createContext, useContext, useState, useEffect, type ReactNode, useCallback } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { api } from "@/lib/api";

// Type User aligné avec le Backend
export type User = {
    id: string;
    email: string;
    firstName?: string | null;
    lastName?: string | null;
    role: "USER" | "ADMIN";
    avatar?: string;
};

interface AuthContextType {
    user: User | null;
    isLoading: boolean;
    login: (user: User) => void;
    logout: () => void; // Ajouté pour AuthCallbackPage
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const location = useLocation();

    // Initialisation
    const initAuth = useCallback(async () => {
        try {
            // Le cookie est envoyé automatiquement par le navigateur
            const { data } = await api.get<User>("/auth/me");
            setUser(data);
        } catch {
            setUser(null);
        } finally {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        initAuth();

        const handleLogoutEvent = () => logout();
        window.addEventListener('auth:logout', handleLogoutEvent);
        return () => window.removeEventListener('auth:logout', handleLogoutEvent);
    }, [initAuth]);

    // ✅ LOGIN : On ne stocke plus de token manuellement
    const login = (newUser: User) => {
        setUser(newUser);
        // Redirection si on vient de login/register
        if (["/login", "/register"].includes(location.pathname)) {
            navigate("/dashboard");
        }
    };

    // ✅ LOGOUT : On appelle le back pour nettoyer le cookie
    const logout = async () => {
        try {
            await api.post("/auth/logout");
        } catch (e) {
            console.error(e);
        }
        setUser(null);
        navigate("/login");
    };

    return (
        <AuthContext.Provider value={{ user, isLoading, login, logout, initAuth }}>
            {children}
        </AuthContext.Provider>
    );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within AuthProvider");
    }
    return context;
}