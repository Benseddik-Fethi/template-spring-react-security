import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Link } from "react-router-dom";
import { authService } from "@/services";
import { ROUTES } from "@/config";
import { LanguageSwitcher } from "@/components/LanguageSwitcher";

export default function ResendVerificationPage() {
    const { t } = useTranslation('pages');
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
        <div className="min-h-screen bg-gray-50 dark:bg-slate-950 flex items-center justify-center p-4 relative">
            {/* Language switcher - top right position */}
            <div className="absolute top-4 right-4 z-20">
                <LanguageSwitcher />
            </div>
            
            <Card className="w-full max-w-md">
                <CardHeader>
                    <CardTitle>{t('resendVerification.title')}</CardTitle>
                    <CardDescription>{t('resendVerification.description')}</CardDescription>
                </CardHeader>
                <CardContent>
                    {status === "success" ? (
                        <div className="text-center space-y-4">
                            <div className="p-4 bg-green-50 text-green-700 rounded-lg">
                                {t('resendVerification.successMessage')}
                            </div>
                            <Button asChild className="w-full"><Link to={ROUTES.LOGIN}>{t('resendVerification.backToLogin')}</Link></Button>
                        </div>
                    ) : (
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div className="space-y-2">
                                <Label>{t('resendVerification.email')}</Label>
                                <Input
                                    type="email"
                                    required
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                            </div>
                            <Button type="submit" className="w-full" disabled={status === "loading"}>
                                {status === "loading" ? t('resendVerification.submitting') : t('resendVerification.submit')}
                            </Button>
                            {status === "error" && (
                                <p className="text-sm text-red-500 text-center">{t('resendVerification.errorMessage')}</p>
                            )}
                        </form>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}