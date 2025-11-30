import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useTheme } from "@/components/ThemeProvider";
import { useLanguage } from "@/hooks/useLanguage";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Switch } from "@/components/ui/switch";
import { Bell, CheckCircle, Globe, Moon, Palette, Sun, Monitor } from "lucide-react";

export default function SettingsPage() {
    const { t } = useTranslation('pages');
    const { theme, setTheme } = useTheme();
    const { currentLanguage, changeLanguage } = useLanguage();
    const [emailNotifications, setEmailNotifications] = useState(true);

    return (
        <div className="container max-w-2xl py-8">
            <h1 className="text-2xl font-bold mb-6">{t('settings.title')}</h1>

            {/* Language Section */}
            <Card className="mb-6">
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Globe className="h-5 w-5" />
                        {t('settings.language.title')}
                    </CardTitle>
                    <CardDescription>{t('settings.language.description')}</CardDescription>
                </CardHeader>
                <CardContent>
                    <div className="grid grid-cols-2 gap-4">
                        {/* Card Français */}
                        <button
                            onClick={() => changeLanguage('fr')}
                            className={`p-4 rounded-lg border-2 text-center transition-all ${
                                currentLanguage === 'fr'
                                    ? 'border-primary bg-primary/10'
                                    : 'border-border hover:border-primary/50'
                            }`}
                        >
                            <span className="text-3xl mb-2 block">🇫🇷</span>
                            <span className="font-medium">Français</span>
                            {currentLanguage === 'fr' && <CheckCircle className="h-4 w-4 text-primary mx-auto mt-2" />}
                        </button>

                        {/* Card English */}
                        <button
                            onClick={() => changeLanguage('en')}
                            className={`p-4 rounded-lg border-2 text-center transition-all ${
                                currentLanguage === 'en'
                                    ? 'border-primary bg-primary/10'
                                    : 'border-border hover:border-primary/50'
                            }`}
                        >
                            <span className="text-3xl mb-2 block">🇬🇧</span>
                            <span className="font-medium">English</span>
                            {currentLanguage === 'en' && <CheckCircle className="h-4 w-4 text-primary mx-auto mt-2" />}
                        </button>
                    </div>
                </CardContent>
            </Card>

            {/* Theme Section */}
            <Card className="mb-6">
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Palette className="h-5 w-5" />
                        {t('settings.theme.title')}
                    </CardTitle>
                    <CardDescription>{t('settings.theme.description')}</CardDescription>
                </CardHeader>
                <CardContent>
                    <div className="grid grid-cols-3 gap-4">
                        {/* Card Light */}
                        <button
                            onClick={() => setTheme("light")}
                            className={`p-4 rounded-lg border-2 text-center transition-all ${
                                theme === 'light'
                                    ? 'border-primary bg-primary/10'
                                    : 'border-border hover:border-primary/50'
                            }`}
                        >
                            <Sun className="h-6 w-6 mx-auto mb-2" />
                            <span className="font-medium">{t('settings.theme.light')}</span>
                            {theme === 'light' && <CheckCircle className="h-4 w-4 text-primary mx-auto mt-2" />}
                        </button>

                        {/* Card Dark */}
                        <button
                            onClick={() => setTheme("dark")}
                            className={`p-4 rounded-lg border-2 text-center transition-all ${
                                theme === 'dark'
                                    ? 'border-primary bg-primary/10'
                                    : 'border-border hover:border-primary/50'
                            }`}
                        >
                            <Moon className="h-6 w-6 mx-auto mb-2" />
                            <span className="font-medium">{t('settings.theme.dark')}</span>
                            {theme === 'dark' && <CheckCircle className="h-4 w-4 text-primary mx-auto mt-2" />}
                        </button>

                        {/* Card System */}
                        <button
                            onClick={() => setTheme("system")}
                            className={`p-4 rounded-lg border-2 text-center transition-all ${
                                theme === 'system'
                                    ? 'border-primary bg-primary/10'
                                    : 'border-border hover:border-primary/50'
                            }`}
                        >
                            <Monitor className="h-6 w-6 mx-auto mb-2" />
                            <span className="font-medium">{t('settings.theme.system')}</span>
                            {theme === 'system' && <CheckCircle className="h-4 w-4 text-primary mx-auto mt-2" />}
                        </button>
                    </div>
                </CardContent>
            </Card>

            {/* Notifications Section */}
            <Card>
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Bell className="h-5 w-5" />
                        {t('settings.notifications.title')}
                    </CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="font-medium">{t('settings.notifications.emailNotifications')}</p>
                            <p className="text-sm text-muted-foreground">{t('settings.notifications.emailNotificationsDescription')}</p>
                        </div>
                        <Switch
                            checked={emailNotifications}
                            onCheckedChange={setEmailNotifications}
                        />
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}