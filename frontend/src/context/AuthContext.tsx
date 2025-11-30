import { createContext, useContext, useState, useEffect, type ReactNode, useCallback, useMemo } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { authService } from "@/services";
import { ROUTES } from "@/config";
import type { User } from "@/types";

// Re-export User type for backward compatibility
export type { User } from "@/types";

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
            const user = await authService.me();
            setUser(user);
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
    // eslint-disable-next-line react-hooks/exhaustive-deps -- logout is excluded to prevent infinite loop since it changes on each render cycle
    }, [initAuth]);

    // Memoized pour éviter les re-renders inutiles des composants enfants
    const login = useCallback((newUser: User) => {
        setUser(newUser);
        if (location.pathname === ROUTES.LOGIN || location.pathname === ROUTES.REGISTER) {
            navigate(ROUTES.DASHBOARD);
        }
    }, [location.pathname, navigate]);

    // Memoized pour éviter les re-renders inutiles des composants enfants
    const logout = useCallback(async () => {
        try {
            await authService.logout();
        } catch (e) {
            console.error(e);
        }
        setUser(null);
        navigate(ROUTES.LOGIN);
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