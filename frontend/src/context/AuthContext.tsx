import { createContext, useContext, useState, useEffect, type ReactNode, useCallback, useMemo } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { api } from "@/lib/api";

// ✅ Type mis à jour avec emailVerified
export type User = {
    id: string;
    email: string;
    firstName?: string | null;
    lastName?: string | null;
    role: "USER" | "ADMIN";
    avatar?: string;
    emailVerified: boolean;
};

interface AuthContextType {
    user: User | null;
    isLoading: boolean;
    login: (user: User) => void;
    logout: () => void;
    initAuth: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const location = useLocation();

    // Initialisation - memoized pour éviter les recréations inutiles
    const initAuth = useCallback(async () => {
        try {
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
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [initAuth]);

    // Memoized pour éviter les re-renders inutiles des composants enfants
    const login = useCallback((newUser: User) => {
        setUser(newUser);
        if (["/login", "/register"].includes(location.pathname)) {
            navigate("/dashboard");
        }
    }, [location.pathname, navigate]);

    // Memoized pour éviter les re-renders inutiles des composants enfants
    const logout = useCallback(async () => {
        try {
            await api.post("/auth/logout");
        } catch (e) {
            console.error(e);
        }
        setUser(null);
        navigate("/login");
    }, [navigate]);

    // Memoize le contexte pour éviter les re-renders inutiles
    const contextValue = useMemo(() => ({
        user,
        isLoading,
        login,
        logout,
        initAuth
    }), [user, isLoading, login, logout, initAuth]);

    return (
        <AuthContext.Provider value={contextValue}>
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