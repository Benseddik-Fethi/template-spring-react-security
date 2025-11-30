import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Mail, ArrowLeft, KeyRound, Loader2, CheckCircle } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Link } from "react-router-dom";
import { authService } from "@/services";
import { handleApiError } from "@/lib/error-handler";
import { ROUTES } from "@/config";
import { AuthCard } from "@/components/auth/AuthCard";
import { ErrorMessage } from "@/components/common/ErrorMessage";

export default function ForgotPasswordPage() {
    const { t } = useTranslation('auth');
    const [email, setEmail] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError(null);

        try {
            await authService.forgotPassword({ email });
            setIsSubmitted(true);
        } catch (err) {
            setError(handleApiError(err));
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <AuthCard
            icon={KeyRound}
            title={t('forgotPassword.title')}
            description={isSubmitted
                ? t('forgotPassword.subtitleSuccess')
                : t('forgotPassword.subtitle')}
            iconGradient="from-rose-400 to-pink-500"
            titleColor="text-rose-500"
            backgroundGradient="from-rose-50 via-pink-50 to-amber-50"
            iconShadowColor="shadow-rose-200"
        >
            {isSubmitted ? (
                <div className="text-center space-y-6">
                    <div className="mx-auto w-16 h-16 bg-green-100 dark:bg-green-900/30 rounded-full flex items-center justify-center">
                        <CheckCircle size={32} className="text-green-500" />
                    </div>
                    <div className="space-y-2">
                        <p className="text-gray-600 dark:text-gray-300">
                            {t('forgotPassword.successMessage')}
                        </p>
                        <p className="text-sm text-gray-500 dark:text-gray-400">
                            {t('forgotPassword.checkSpam')}
                        </p>
                    </div>
                    <Link to={ROUTES.LOGIN}>
                        <Button
                            variant="outline"
                            className="w-full h-12 rounded-xl border-gray-200 dark:border-slate-700 font-semibold"
                        >
                            <ArrowLeft className="mr-2 h-4 w-4" />
                            {t('forgotPassword.backToLogin')}
                        </Button>
                    </Link>
                </div>
            ) : (
                <form onSubmit={handleSubmit} className="space-y-5">
                    <div className="space-y-1.5">
                        <Label className="text-gray-600 dark:text-gray-300 font-medium pl-1">
                            {t('forgotPassword.email')}
                        </Label>
                        <Input
                            icon={Mail}
                            className="h-12 bg-slate-50 dark:bg-slate-950 border-transparent focus:bg-white dark:focus:bg-slate-900 focus:border-rose-200 rounded-xl pl-11 text-gray-600 dark:text-white shadow-sm"
                            placeholder={t('forgotPassword.emailPlaceholder')}
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    {error && <ErrorMessage message={error} />}

                    <Button
                        type="submit"
                        disabled={isLoading || !email}
                        className="w-full h-14 text-base font-bold rounded-2xl bg-gradient-to-r from-[#FF6B6B] to-[#FF8E8E] hover:opacity-90 shadow-md shadow-rose-100 dark:shadow-none text-white disabled:opacity-50"
                    >
                        {isLoading ? (
                            <>
                                <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                                {t('forgotPassword.submitting')}
                            </>
                        ) : (
                            t('forgotPassword.submit')
                        )}
                    </Button>

                    <div className="text-center pt-2">
                        <Link
                            to={ROUTES.LOGIN}
                            className="text-sm text-gray-500 dark:text-gray-400 hover:text-rose-500 font-medium inline-flex items-center"
                        >
                            <ArrowLeft className="mr-1 h-4 w-4" />
                            {t('forgotPassword.backToLogin')}
                        </Link>
                    </div>
                </form>
            )}
        </AuthCard>
    );
}
