import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@/context/AuthContext";

export default function AuthCallbackPage() {
    const { initAuth } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {

        initAuth().then(() => {
            navigate("/dashboard");
        });
    }, [initAuth, navigate]);

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-slate-950">
            <div className="text-center">
                <h2 className="text-xl font-semibold mb-2">Connexion en cours...</h2>
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-rose-500 mx-auto"></div>
            </div>
        </div>
    );
}