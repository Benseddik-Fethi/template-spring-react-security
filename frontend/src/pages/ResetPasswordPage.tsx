import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useSearchParams, useNavigate, Link } from "react-router-dom";
import { KeyRound, Loader2, CheckCircle, XCircle, ArrowLeft } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { authService } from "@/services";
import { handleApiError } from "@/lib/error-handler";
import { ROUTES } from "@/config";
import { PasswordInput } from "@/components/forms/PasswordInput";
import { passwordRules } from "@/lib/validators";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { LanguageSwitcher } from "@/components/LanguageSwitcher";

export default function ResetPasswordPage() {
    const { t } = useTranslation('auth');
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const token = searchParams.get("token");

    const [status, setStatus] = useState<"loading" | "valid" | "invalid" | "success" | "error">("loading");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    // Validate token on mount
    useEffect(() => {
        if (!token) {
            setStatus("invalid");
            return;
        }

        authService.validateResetToken(token)
            .then((response) => {
                if (response.valid) {
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
            await authService.resetPassword({
                token,
                newPassword: password
            });
            setStatus("success");
        } catch (err) {
            setErrorMessage(handleApiError(err));
            setStatus("error");
        } finally {
            setIsSubmitting(false);
        }
    };

    // Loading state
    if (status === "loading") {
        return (
            <div className="min-h-screen bg-gradient-to-br from-rose-50 via-pink-50 to-amber-50 flex items-center justify-center p-6 relative overflow-hidden dark:from-slate-950 dark:via-slate-900 dark:to-slate-950">
                {/* Language switcher - top right position */}
                <div className="absolute top-4 right-4 z-20">
                    <LanguageSwitcher />
                </div>
                <Card className="w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800">
                    <CardContent className="p-8 text-center">
                        <Loader2 className="w-12 h-12 text-rose-500 animate-spin mx-auto mb-4" />
                        <p className="text-gray-600 dark:text-gray-300 font-medium">{t('resetPassword.verifyingLink')}</p>
                    </CardContent>
                </Card>
            </div>
        );
    }

    // Invalid token state
    if (status === "invalid") {
        return (
            <div className="min-h-screen bg-gradient-to-br from-rose-50 via-pink-50 to-amber-50 flex items-center justify-center p-6 relative overflow-hidden dark:from-slate-950 dark:via-slate-900 dark:to-slate-950">
                {/* Language switcher - top right position */}
                <div className="absolute top-4 right-4 z-20">
                    <LanguageSwitcher />
                </div>
                <Card className="w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800">
                    <CardHeader className="text-center pt-10">
                        <div className="mx-auto w-20 h-20 bg-red-100 dark:bg-red-900/30 rounded-3xl mb-4 flex items-center justify-center">
                            <XCircle size={40} className="text-red-500" />
                        </div>
                        <CardTitle className="text-2xl font-bold text-red-500 mb-1">
                            {t('resetPassword.invalidLink')}
                        </CardTitle>
                        <CardDescription className="text-gray-500 dark:text-gray-400 font-medium">
                            {t('resetPassword.invalidLinkDescription')}
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="p-8 space-y-4">
                        <p className="text-center text-gray-600 dark:text-gray-300 text-sm">
                            {t('resetPassword.linkExpiredMessage')}
                        </p>
                        <div className="space-y-3">
                            <Link to={ROUTES.AUTH.FORGOT_PASSWORD}>
                                <Button className="w-full h-12 text-base font-bold rounded-2xl bg-gradient-to-r from-[#FF6B6B] to-[#FF8E8E] hover:opacity-90 shadow-md shadow-rose-100 dark:shadow-none text-white">
                                    {t('resetPassword.requestNewLink')}
                                </Button>
                            </Link>
                            <Link to={ROUTES.LOGIN}>
                                <Button
                                    variant="outline"
                                    className="w-full h-12 rounded-xl border-gray-200 dark:border-slate-700 font-semibold"
                                >
                                    <ArrowLeft className="mr-2 h-4 w-4" />
                                    {t('resetPassword.backToLogin')}
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
                {/* Language switcher - top right position */}
                <div className="absolute top-4 right-4 z-20">
                    <LanguageSwitcher />
                </div>
                <Card className="w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800">
                    <CardHeader className="text-center pt-10">
                        <div className="mx-auto w-20 h-20 bg-green-100 dark:bg-green-900/30 rounded-3xl mb-4 flex items-center justify-center">
                            <CheckCircle size={40} className="text-green-500" />
                        </div>
                        <CardTitle className="text-2xl font-bold text-green-500 mb-1">
                            {t('resetPassword.successTitle')}
                        </CardTitle>
                        <CardDescription className="text-gray-500 dark:text-gray-400 font-medium">
                            {t('resetPassword.successMessage')}
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="p-8">
                        <Button
                            onClick={() => navigate(ROUTES.LOGIN)}
                            className="w-full h-14 text-base font-bold rounded-2xl bg-gradient-to-r from-[#FF6B6B] to-[#FF8E8E] hover:opacity-90 shadow-md shadow-rose-100 dark:shadow-none text-white"
                        >
                            {t('resetPassword.signIn')}
                        </Button>
                    </CardContent>
                </Card>
            </div>
        );
    }

    // Valid token - show form (also handles error state with form)
    return (
        <div className="min-h-screen bg-gradient-to-br from-rose-50 via-pink-50 to-amber-50 flex items-center justify-center p-6 relative overflow-hidden dark:from-slate-950 dark:via-slate-900 dark:to-slate-950">
            {/* Language switcher - top right position */}
            <div className="absolute top-4 right-4 z-20">
                <LanguageSwitcher />
            </div>
            <Card className="w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800">
                <CardHeader className="text-center pt-10">
                    <div className="mx-auto w-20 h-20 bg-gradient-to-br from-rose-400 to-pink-500 rounded-3xl mb-4 flex items-center justify-center shadow-lg shadow-rose-200 dark:shadow-none">
                        <KeyRound size={40} className="text-white" />
                    </div>
                    <CardTitle className="text-3xl font-bold text-rose-500 mb-1">
                        {t('resetPassword.title')}
                    </CardTitle>
                    <CardDescription className="text-gray-500 dark:text-gray-400 font-medium">
                        {t('resetPassword.subtitle')}
                    </CardDescription>
                </CardHeader>

                <CardContent className="p-8 space-y-6">
                    <form onSubmit={handleSubmit} className="space-y-5">
                        <PasswordInput
                            label={t('resetPassword.newPassword')}
                            value={password}
                            onChange={setPassword}
                            placeholder={t('resetPassword.passwordPlaceholder')}
                        />

                        {/* Password rules */}
                        <div className="bg-slate-50 dark:bg-slate-950 rounded-xl p-4 space-y-2">
                            <p className="text-xs font-medium text-gray-500 dark:text-gray-400 mb-2">
                                {t('resetPassword.passwordCriteria')}
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

                        <PasswordInput
                            label={t('resetPassword.confirmPassword')}
                            value={confirmPassword}
                            onChange={setConfirmPassword}
                            placeholder={t('resetPassword.passwordPlaceholder')}
                            error={confirmPassword && !doPasswordsMatch ? t('resetPassword.passwordMismatch') : undefined}
                        />

                        {errorMessage && <ErrorMessage message={errorMessage} />}

                        <Button
                            type="submit"
                            disabled={!canSubmit}
                            className="w-full h-14 text-base font-bold rounded-2xl bg-gradient-to-r from-[#FF6B6B] to-[#FF8E8E] hover:opacity-90 shadow-md shadow-rose-100 dark:shadow-none text-white disabled:opacity-50"
                        >
                            {isSubmitting ? (
                                <>
                                    <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                                    {t('resetPassword.submitting')}
                                </>
                            ) : (
                                t('resetPassword.submit')
                            )}
                        </Button>

                        <div className="text-center pt-2">
                            <Link
                                to={ROUTES.LOGIN}
                                className="text-sm text-gray-500 dark:text-gray-400 hover:text-rose-500 font-medium inline-flex items-center"
                            >
                                <ArrowLeft className="mr-1 h-4 w-4" />
                                {t('resetPassword.backToLogin')}
                            </Link>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
}
