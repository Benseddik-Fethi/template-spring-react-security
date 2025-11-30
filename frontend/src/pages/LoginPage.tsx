import { useState } from "react";
import { useTranslation } from "react-i18next";
import { ChevronRight, Mail, Shield } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Checkbox } from "@/components/ui/checkbox";
import { Label } from "@/components/ui/label";
import { Separator } from "@/components/ui/separator";
import { Link } from "react-router-dom";
import { useAuth } from "@/context/AuthContext";
import { authService } from "@/services";
import { handleApiError } from "@/lib/error-handler";
import { ROUTES } from "@/config";
import { AuthCard } from "@/components/auth/AuthCard";
import { SocialLoginButtons } from "@/components/auth/SocialLoginButtons";
import { PasswordInput } from "@/components/forms/PasswordInput";
import { ErrorMessage } from "@/components/common/ErrorMessage";

export default function LoginPage() {
    const { t } = useTranslation('auth');
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const { login } = useAuth();

    const handleLogin = async () => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await authService.login({ email, password });
            login(response.user);
        } catch (err) {
            setError(handleApiError(err));
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <AuthCard
            icon={Shield}
            title={t('login.title')}
            description={t('login.subtitle')}
        >
            <div className="space-y-5">
                <div className="space-y-1.5">
                    <Label className="text-gray-600 dark:text-gray-300 font-medium pl-1">{t('login.email')}</Label>
                    <Input
                        icon={Mail}
                        className="h-12 bg-slate-50 dark:bg-slate-950 border-transparent focus:bg-white dark:focus:bg-slate-900 focus:border-indigo-200 rounded-xl pl-11 text-gray-600 dark:text-white shadow-sm"
                        placeholder={t('login.emailPlaceholder')}
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>

                <PasswordInput
                    label={t('login.password')}
                    value={password}
                    onChange={setPassword}
                    placeholder={t('login.passwordPlaceholder')}
                />

                <div className="flex items-center justify-between pt-1">
                    <div className="flex items-center space-x-2">
                        <Checkbox id="remember" className="border-gray-300 data-[state=checked]:bg-indigo-500" />
                        <Label htmlFor="remember" className="text-sm text-gray-500 dark:text-gray-400 cursor-pointer">{t('login.rememberMe')}</Label>
                    </div>
                    <Link to={ROUTES.AUTH.FORGOT_PASSWORD} className="text-sm text-indigo-500 hover:text-indigo-600 font-semibold">
                        {t('login.forgotPassword')}
                    </Link>
                </div>

                {error && <ErrorMessage message={error} />}

                <Button
                    className="w-full h-14 text-base font-bold rounded-2xl bg-gradient-to-r from-indigo-500 to-purple-500 hover:opacity-90 shadow-md shadow-indigo-100 dark:shadow-none text-white"
                    onClick={handleLogin}
                    disabled={isLoading}>
                    {isLoading ? t('login.submitting') : t('login.submit')} <ChevronRight className="ml-2 h-5 w-5"/>
                </Button>
            </div>

            <div className="relative py-2">
                <div className="absolute inset-0 flex items-center"><Separator className="bg-gray-100 dark:bg-slate-800" /></div>
                <div className="relative flex justify-center text-xs uppercase"><span className="bg-white dark:bg-slate-900 px-4 text-gray-400 font-medium">{t('login.or')}</span></div>
            </div>

            <SocialLoginButtons disabled={isLoading} />

            <p className="text-center text-sm text-gray-500 dark:text-gray-400 mt-6 font-medium">
                {t('login.noAccount')} <Link to={ROUTES.REGISTER} className="text-indigo-500 font-bold hover:underline ml-1">{t('login.createAccount')}</Link>
            </p>
        </AuthCard>
    );
}