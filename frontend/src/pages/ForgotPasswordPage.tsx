import { useState } from "react";
import { Mail, ArrowLeft, KeyRound, Loader2, CheckCircle } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { api } from "@/lib/api";
import { Link } from "react-router-dom";
import { isAxiosError } from "axios";

export default function ForgotPasswordPage() {
    const [email, setEmail] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError(null);

        try {
            await api.post('/users/forgot-password', { email });
            setIsSubmitted(true);
        } catch (err) {
            if (isAxiosError(err)) {
                setError(err.response?.data?.message || "Une erreur est survenue");
            } else {
                setError("Une erreur est survenue");
            }
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-rose-50 via-pink-50 to-amber-50 flex items-center justify-center p-6 relative overflow-hidden dark:from-slate-950 dark:via-slate-900 dark:to-slate-950">
            <Card className="w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800">
                <CardHeader className="text-center pt-10">
                    <div className="mx-auto w-20 h-20 bg-gradient-to-br from-rose-400 to-pink-500 rounded-3xl mb-4 flex items-center justify-center shadow-lg shadow-rose-200 dark:shadow-none">
                        <KeyRound size={40} className="text-white" />
                    </div>
                    <CardTitle className="text-3xl font-bold text-rose-500 mb-1">
                        Mot de passe oublié
                    </CardTitle>
                    <CardDescription className="text-gray-500 dark:text-gray-400 font-medium">
                        {isSubmitted
                            ? "Vérifiez votre boîte de réception"
                            : "Entrez votre email pour réinitialiser votre mot de passe"}
                    </CardDescription>
                </CardHeader>

                <CardContent className="p-8 space-y-6">
                    {isSubmitted ? (
                        <div className="text-center space-y-6">
                            <div className="mx-auto w-16 h-16 bg-green-100 dark:bg-green-900/30 rounded-full flex items-center justify-center">
                                <CheckCircle size={32} className="text-green-500" />
                            </div>
                            <div className="space-y-2">
                                <p className="text-gray-600 dark:text-gray-300">
                                    Si un compte existe avec cet email, vous recevrez un lien de réinitialisation.
                                </p>
                                <p className="text-sm text-gray-500 dark:text-gray-400">
                                    Pensez à vérifier vos spams.
                                </p>
                            </div>
                            <Link to="/login">
                                <Button
                                    variant="outline"
                                    className="w-full h-12 rounded-xl border-gray-200 dark:border-slate-700 font-semibold"
                                >
                                    <ArrowLeft className="mr-2 h-4 w-4" />
                                    Retour à la connexion
                                </Button>
                            </Link>
                        </div>
                    ) : (
                        <form onSubmit={handleSubmit} className="space-y-5">
                            <div className="space-y-1.5">
                                <Label className="text-gray-600 dark:text-gray-300 font-medium pl-1">
                                    Email
                                </Label>
                                <Input
                                    icon={Mail}
                                    className="h-12 bg-slate-50 dark:bg-slate-950 border-transparent focus:bg-white dark:focus:bg-slate-900 focus:border-rose-200 rounded-xl pl-11 text-gray-600 dark:text-white shadow-sm"
                                    placeholder="votre@email.com"
                                    type="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                />
                            </div>

                            {error && (
                                <p className="text-sm text-red-500 text-center">{error}</p>
                            )}

                            <Button
                                type="submit"
                                disabled={isLoading || !email}
                                className="w-full h-14 text-base font-bold rounded-2xl bg-gradient-to-r from-[#FF6B6B] to-[#FF8E8E] hover:opacity-90 shadow-md shadow-rose-100 dark:shadow-none text-white disabled:opacity-50"
                            >
                                {isLoading ? (
                                    <>
                                        <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                                        Envoi en cours...
                                    </>
                                ) : (
                                    "Envoyer le lien de réinitialisation"
                                )}
                            </Button>

                            <div className="text-center pt-2">
                                <Link
                                    to="/login"
                                    className="text-sm text-gray-500 dark:text-gray-400 hover:text-rose-500 font-medium inline-flex items-center"
                                >
                                    <ArrowLeft className="mr-1 h-4 w-4" />
                                    Retour à la connexion
                                </Link>
                            </div>
                        </form>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}
