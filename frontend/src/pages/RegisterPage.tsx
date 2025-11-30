import { useState } from "react";
import { ChevronRight, Mail, Shield } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Separator } from "@/components/ui/separator";
import { Link, useNavigate } from "react-router-dom";
import { Label } from "@/components/ui/label";
import { authService } from "@/services";
import { handleApiError } from "@/lib/error-handler";
import { ROUTES } from "@/config";
import { AuthCard } from "@/components/auth/AuthCard";
import { SocialLoginButtons } from "@/components/auth/SocialLoginButtons";
import { PasswordInput } from "@/components/forms/PasswordInput";
import { ErrorMessage } from "@/components/common/ErrorMessage";

export default function RegisterPage() {
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmPassword: "",
    });
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleRegister = async () => {
        if (formData.password !== formData.confirmPassword) {
            setError("Les mots de passe ne correspondent pas");
            return;
        }
        setIsLoading(true);
        setError(null);
        try {
            await authService.register({
                email: formData.email,
                password: formData.password,
                firstName: formData.firstName,
                lastName: formData.lastName
            });
            navigate(ROUTES.AUTH.VERIFY_EMAIL_SENT);
        } catch (err) {
            setError(handleApiError(err));
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <AuthCard
            icon={Shield}
            title="Créer un compte"
            description="Créez votre compte"
        >
            <div className="space-y-4">
                <div className="grid grid-cols-2 gap-3">
                    <div className="space-y-1.5">
                        <Label className="text-gray-600 dark:text-gray-300 pl-1">Prénom</Label>
                        <Input
                            placeholder="Jean"
                            className="h-11 bg-slate-50 dark:bg-slate-950 border-transparent focus:bg-white dark:focus:bg-slate-900 rounded-xl"
                            value={formData.firstName}
                            onChange={(e) => setFormData({...formData, firstName: e.target.value})}
                        />
                    </div>
                    <div className="space-y-1.5">
                        <Label className="text-gray-600 dark:text-gray-300 pl-1">Nom</Label>
                        <Input
                            placeholder="Dupont"
                            className="h-11 bg-slate-50 dark:bg-slate-950 border-transparent focus:bg-white dark:focus:bg-slate-900 rounded-xl"
                            value={formData.lastName}
                            onChange={(e) => setFormData({...formData, lastName: e.target.value})}
                        />
                    </div>
                </div>

                <div className="space-y-1.5">
                    <Label className="text-gray-600 dark:text-gray-300 pl-1">Email</Label>
                    <Input
                        icon={Mail}
                        placeholder="votre@email.com"
                        type="email"
                        className="h-11 pl-10 bg-slate-50 dark:bg-slate-950 border-transparent focus:bg-white dark:focus:bg-slate-900 rounded-xl"
                        value={formData.email}
                        onChange={(e) => setFormData({...formData, email: e.target.value})}
                    />
                </div>

                <PasswordInput
                    label="Mot de passe"
                    value={formData.password}
                    onChange={(value) => setFormData({...formData, password: value})}
                    placeholder="Min. 8 caractères"
                    className="h-11"
                />

                <PasswordInput
                    label="Confirmation"
                    value={formData.confirmPassword}
                    onChange={(value) => setFormData({...formData, confirmPassword: value})}
                    placeholder="Répétez le mot de passe"
                    className="h-11"
                />

                {error && <ErrorMessage message={error} />}

                <Button
                    className="w-full h-12 text-base font-bold rounded-2xl bg-gradient-to-r from-indigo-500 to-purple-500 hover:opacity-90 shadow-md shadow-indigo-100 dark:shadow-none text-white mt-2"
                    onClick={handleRegister}
                    disabled={isLoading}>
                    {isLoading ? "Création..." : "Créer mon compte"} <ChevronRight className="ml-2 h-5 w-5"/>
                </Button>
            </div>

            <div className="relative py-2">
                <div className="absolute inset-0 flex items-center"><Separator
                    className="bg-gray-100 dark:bg-slate-800"/></div>
                <div className="relative flex justify-center text-xs uppercase"><span
                    className="bg-white dark:bg-slate-900 px-4 text-gray-400 font-medium">ou</span></div>
            </div>

            <SocialLoginButtons disabled={isLoading} />

            <p className="text-center text-sm text-gray-500 dark:text-gray-400 mt-4">
                Déjà un compte ? <Link to={ROUTES.LOGIN} className="text-indigo-500 font-bold hover:underline ml-1">Se
                connecter</Link>
            </p>
        </AuthCard>
    );
}