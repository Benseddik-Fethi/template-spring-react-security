import { useAuth } from "@/context/AuthContext";
import { Navigate, Outlet } from "react-router-dom";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";

export const ProtectedRoute = () => {
    const { user, isLoading } = useAuth();

    if (isLoading) return (
        <div className="min-h-screen flex items-center justify-center">
            <LoadingSpinner size="lg" />
        </div>
    );
    if (!user) return <Navigate to="/login" replace />;
    if (!user.emailVerified) {
        return <Navigate to="/auth/verify-email-sent" replace />;
    }
    return <Outlet />;
};