import { useEffect, useState } from "react";
import { useSearchParams, useNavigate, Link } from "react-router-dom";
import { Lock, Eye, EyeOff, KeyRound, Loader2, CheckCircle, XCircle, ArrowLeft } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { api } from "@/lib/api";
import { isAxiosError } from "axios";

// Password validation rules matching backend requirements
const passwordRules = [
    { id: 'length', label: '12 à 128 caractères', test: (p: string) => p.length >= 12 && p.length <= 128 },
    { id: 'lowercase', label: 'Au moins une minuscule', test: (p: string) => /[a-z]/.test(p) },
    { id: 'uppercase', label: 'Au moins une majuscule', test: (p: string) => /[A-Z]/.test(p) },
    { id: 'digit', label: 'Au moins un chiffre', test: (p: string) => /\d/.test(p) },
    { id: 'special', label: "Au moins un caractère spécial (@$!%*?&#^()_+-=[]{};':\"\\|,.<>/`~)", test: (p: string) => /[@$!%*?&#^()_+\-=[\]{};':"\\|,.<>/`~]/.test(p) },
];

export default function ResetPasswordPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const token = searchParams.get("token");

    const [status, setStatus] = useState<"loading" | "valid" | "invalid" | "success" | "error">("loading");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    // Validate token on mount
    useEffect(() => {
        if (!token) {
            setStatus("invalid");
            return;
        }

        api.get(`/users/reset-password/validate?token=${encodeURIComponent(token)}`)
            .then((response) => {
                if (response.data.valid) {
                    setStatus("valid");
                } else {
                    setStatus("invalid");
                }
            })
            .catch(() => {
                setStatus("invalid");
            });
    }, [token]);

    // Check if all password rules are valid
    const isPasswordValid = passwordRules.every(rule => rule.test(password));
    const doPasswordsMatch = password === confirmPassword && confirmPassword.length > 0;
    const canSubmit = isPasswordValid && doPasswordsMatch && !isSubmitting;

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        
        if (!canSubmit || !token) return;

        setIsSubmitting(true);
        setErrorMessage(null);

        try {
            await api.post('/users/reset-password', {
                token,
                newPassword: password
            });
            setStatus("success");
        } catch (err) {
            if (isAxiosError(err)) {
                setErrorMessage(err.response?.data?.message || "Une erreur est survenue");
            } else {
                setErrorMessage("Une erreur est survenue");
            }
            setStatus("error");
        } finally {
            setIsSubmitting(false);
        }
    };

    // Loading state
    if (status === "loading") {
        return (
            <div className="min-h-screen bg-gradient-to-br from-rose-50 via-pink-50 to-amber-50 flex items-center justify-center p-6 relative overflow-hidden dark:from-slate-950 dark:via-slate-900 dark:to-slate-950">
                <Card className="w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800">
                    <CardContent className="p-8 text-center">
                        <Loader2 className="w-12 h-12 text-rose-500 animate-spin mx-auto mb-4" />
                        <p className="text-gray-600 dark:text-gray-300 font-medium">Vérification du lien...</p>
                    </CardContent>
                </Card>
            </div>
        );
    }

    // Invalid token state
    if (status === "invalid") {
        return (
            <div className="min-h-screen bg-gradient-to-br from-rose-50 via-pink-50 to-amber-50 flex items-center justify-center p-6 relative overflow-hidden dark:from-slate-950 dark:via-slate-900 dark:to-slate-950">
                <Card className="w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800">
                    <CardHeader className="text-center pt-10">
                        <div className="mx-auto w-20 h-20 bg-red-100 dark:bg-red-900/30 rounded-3xl mb-4 flex items-center justify-center">
                            <XCircle size={40} className="text-red-500" />
                        </div>
                        <CardTitle className="text-2xl font-bold text-red-500 mb-1">
                            Lien invalide ou expiré
                        </CardTitle>
                        <CardDescription className="text-gray-500 dark:text-gray-400 font-medium">
                            Ce lien de réinitialisation n'est plus valide
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="p-8 space-y-4">
                        <p className="text-center text-gray-600 dark:text-gray-300 text-sm">
                            Le lien a peut-être expiré ou a déjà été utilisé. Vous pouvez demander un nouveau lien de réinitialisation.
                        </p>
                        <div className="space-y-3">
                            <Link to="/auth/forgot-password">
                                <Button className="w-full h-12 text-base font-bold rounded-2xl bg-gradient-to-r from-[#FF6B6B] to-[#FF8E8E] hover:opacity-90 shadow-md shadow-rose-100 dark:shadow-none text-white">
                                    Demander un nouveau lien
                                </Button>
                            </Link>
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
                    </CardContent>
                </Card>
            </div>
        );
    }

    // Success state
    if (status === "success") {
        return (
            <div className="min-h-screen bg-gradient-to-br from-rose-50 via-pink-50 to-amber-50 flex items-center justify-center p-6 relative overflow-hidden dark:from-slate-950 dark:via-slate-900 dark:to-slate-950">
                <Card className="w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800">
                    <CardHeader className="text-center pt-10">
                        <div className="mx-auto w-20 h-20 bg-green-100 dark:bg-green-900/30 rounded-3xl mb-4 flex items-center justify-center">
                            <CheckCircle size={40} className="text-green-500" />
                        </div>
                        <CardTitle className="text-2xl font-bold text-green-500 mb-1">
                            Mot de passe réinitialisé !
                        </CardTitle>
                        <CardDescription className="text-gray-500 dark:text-gray-400 font-medium">
                            Vous pouvez maintenant vous connecter avec votre nouveau mot de passe
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="p-8">
                        <Button
                            onClick={() => navigate("/login")}
                            className="w-full h-14 text-base font-bold rounded-2xl bg-gradient-to-r from-[#FF6B6B] to-[#FF8E8E] hover:opacity-90 shadow-md shadow-rose-100 dark:shadow-none text-white"
                        >
                            Se connecter
                        </Button>
                    </CardContent>
                </Card>
            </div>
        );
    }

    // Valid token - show form (also handles error state with form)
    return (
        <div className="min-h-screen bg-gradient-to-br from-rose-50 via-pink-50 to-amber-50 flex items-center justify-center p-6 relative overflow-hidden dark:from-slate-950 dark:via-slate-900 dark:to-slate-950">
            <Card className="w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800">
                <CardHeader className="text-center pt-10">
                    <div className="mx-auto w-20 h-20 bg-gradient-to-br from-rose-400 to-pink-500 rounded-3xl mb-4 flex items-center justify-center shadow-lg shadow-rose-200 dark:shadow-none">
                        <KeyRound size={40} className="text-white" />
                    </div>
                    <CardTitle className="text-3xl font-bold text-rose-500 mb-1">
                        Nouveau mot de passe
                    </CardTitle>
                    <CardDescription className="text-gray-500 dark:text-gray-400 font-medium">
                        Créez un mot de passe sécurisé pour votre compte
                    </CardDescription>
                </CardHeader>

                <CardContent className="p-8 space-y-6">
                    <form onSubmit={handleSubmit} className="space-y-5">
                        <div className="space-y-1.5">
                            <Label className="text-gray-600 dark:text-gray-300 font-medium pl-1">
                                Nouveau mot de passe
                            </Label>
                            <div className="relative">
                                <Input
                                    icon={Lock}
                                    className="h-12 bg-slate-50 dark:bg-slate-950 border-transparent focus:bg-white dark:focus:bg-slate-900 focus:border-rose-200 rounded-xl pl-11 pr-10 text-gray-600 dark:text-white shadow-sm"
                                    placeholder="••••••••••••"
                                    type={showPassword ? "text" : "password"}
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowPassword(!showPassword)}
                                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200"
                                >
                                    {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                                </button>
                            </div>
                        </div>

                        {/* Password rules */}
                        <div className="bg-slate-50 dark:bg-slate-950 rounded-xl p-4 space-y-2">
                            <p className="text-xs font-medium text-gray-500 dark:text-gray-400 mb-2">
                                Critères du mot de passe :
                            </p>
                            {passwordRules.map((rule) => {
                                const isValid = rule.test(password);
                                return (
                                    <div key={rule.id} className="flex items-center gap-2 text-sm">
                                        {isValid ? (
                                            <CheckCircle size={16} className="text-green-500 flex-shrink-0" />
                                        ) : (
                                            <XCircle size={16} className="text-gray-300 dark:text-gray-600 flex-shrink-0" />
                                        )}
                                        <span className={isValid ? "text-green-600 dark:text-green-400" : "text-gray-500 dark:text-gray-400"}>
                                            {rule.label}
                                        </span>
                                    </div>
                                );
                            })}
                        </div>

                        <div className="space-y-1.5">
                            <Label className="text-gray-600 dark:text-gray-300 font-medium pl-1">
                                Confirmer le mot de passe
                            </Label>
                            <div className="relative">
                                <Input
                                    icon={Lock}
                                    className="h-12 bg-slate-50 dark:bg-slate-950 border-transparent focus:bg-white dark:focus:bg-slate-900 focus:border-rose-200 rounded-xl pl-11 pr-10 text-gray-600 dark:text-white shadow-sm"
                                    placeholder="••••••••••••"
                                    type={showConfirmPassword ? "text" : "password"}
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200"
                                >
                                    {showConfirmPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                                </button>
                            </div>
                            {confirmPassword && !doPasswordsMatch && (
                                <p className="text-sm text-red-500 pl-1 mt-1">
                                    Les mots de passe ne correspondent pas
                                </p>
                            )}
                        </div>

                        {errorMessage && (
                            <p className="text-sm text-red-500 text-center">{errorMessage}</p>
                        )}

                        <Button
                            type="submit"
                            disabled={!canSubmit}
                            className="w-full h-14 text-base font-bold rounded-2xl bg-gradient-to-r from-[#FF6B6B] to-[#FF8E8E] hover:opacity-90 shadow-md shadow-rose-100 dark:shadow-none text-white disabled:opacity-50"
                        >
                            {isSubmitting ? (
                                <>
                                    <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                                    Réinitialisation...
                                </>
                            ) : (
                                "Réinitialiser le mot de passe"
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
                </CardContent>
            </Card>
        </div>
    );
}
