import { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { api } from "@/lib/api";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { CheckCircle, XCircle, Loader2 } from "lucide-react";

export default function VerifyEmailPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const token = searchParams.get("token");
    const [status, setStatus] = useState<"loading" | "success" | "error">("loading");

    useEffect(() => {
        if (!token) {
            setStatus("error");
            return;
        }

        api.post(`/users/verify-email?token=${token}`)
            .then(() => setStatus("success"))
            .catch(() => setStatus("error"));
    }, [token]);

    return (
        <div className="min-h-screen bg-gray-50 dark:bg-slate-950 flex items-center justify-center p-4">
            <Card className="w-full max-w-md text-center p-6">
                <CardContent className="space-y-6 pt-6">
                    {status === "loading" && (
                        <>
                            <Loader2 className="w-12 h-12 text-rose-500 animate-spin mx-auto" />
                            <h2 className="text-xl font-semibold">Vérification en cours...</h2>
                        </>
                    )}

                    {status === "success" && (
                        <>
                            <CheckCircle className="w-12 h-12 text-green-500 mx-auto" />
                            <h2 className="text-xl font-bold text-green-600">Email vérifié !</h2>
                            <p className="text-gray-500">Votre compte est maintenant actif.</p>
                            <Button onClick={() => navigate("/login")} className="w-full">
                                Se connecter
                            </Button>
                        </>
                    )}

                    {status === "error" && (
                        <>
                            <XCircle className="w-12 h-12 text-red-500 mx-auto" />
                            <h2 className="text-xl font-bold text-red-600">Lien invalide</h2>
                            <p className="text-gray-500">Ce lien a expiré ou a déjà été utilisé.</p>
                            <Button variant="outline" onClick={() => navigate("/login")} className="w-full">
                                Retour à l'accueil
                            </Button>
                        </>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}