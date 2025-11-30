import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Link } from "react-router-dom";
import { authService } from "@/services";
import { ROUTES } from "@/config";

export default function ResendVerificationPage() {
    const [email, setEmail] = useState("");
    const [status, setStatus] = useState<"idle" | "loading" | "success" | "error">("idle");

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setStatus("loading");
        try {
            await authService.resendVerification({ email });
            setStatus("success");
        } catch (error) {
            console.error(error);
            setStatus("error");
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 dark:bg-slate-950 flex items-center justify-center p-4">
            <Card className="w-full max-w-md">
                <CardHeader>
                    <CardTitle>Renvoyer le lien</CardTitle>
                    <CardDescription>Recevez un nouveau lien d'activation</CardDescription>
                </CardHeader>
                <CardContent>
                    {status === "success" ? (
                        <div className="text-center space-y-4">
                            <div className="p-4 bg-green-50 text-green-700 rounded-lg">
                                Email envoyé ! Vérifiez votre boîte de réception.
                            </div>
                            <Button asChild className="w-full"><Link to={ROUTES.LOGIN}>Retour connexion</Link></Button>
                        </div>
                    ) : (
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div className="space-y-2">
                                <Label>Email</Label>
                                <Input
                                    type="email"
                                    required
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                            </div>
                            <Button type="submit" className="w-full" disabled={status === "loading"}>
                                {status === "loading" ? "Envoi..." : "Envoyer"}
                            </Button>
                            {status === "error" && (
                                <p className="text-sm text-red-500 text-center">Une erreur est survenue.</p>
                            )}
                        </form>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}