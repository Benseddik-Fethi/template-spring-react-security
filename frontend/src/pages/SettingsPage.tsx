import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useTheme } from "@/components/ThemeProvider";
import { useLanguage } from "@/hooks/useLanguage";
import { Card } from "@/components/ui/card";
import { Switch } from "@/components/ui/switch";
import { Bell, CheckCircle, Globe, Moon, Palette, Sun, Monitor } from "lucide-react";

export default function SettingsPage() {
    const { t } = useTranslation('pages');
    const { theme, setTheme } = useTheme();
    const { currentLanguage, changeLanguage } = useLanguage();
    const [emailNotifications, setEmailNotifications] = useState(true);

    return (
        <div className="space-y-8 animate-in fade-in duration-500">
            {/* Header */}
            <div>
                <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
                    {t('settings.title')}
                </h1>
                <p className="text-gray-500 dark:text-gray-400 mt-2">
                    {t('settings.subtitle')}
                </p>
            </div>

            {/* Language Section */}
            <Card className="p-6 border border-gray-100 dark:border-slate-800 shadow-sm bg-white dark:bg-slate-900 rounded-2xl">
                <div className="flex items-center gap-3 mb-6">
                    <div className="w-10 h-10 rounded-xl bg-indigo-100 dark:bg-indigo-900/20 flex items-center justify-center text-indigo-600">
                        <Globe size={20} />
                    </div>
                    <div>
                        <h2 className="text-xl font-bold text-gray-900 dark:text-white">
                            {t('settings.language.title')}
                        </h2>
                        <p className="text-sm text-gray-500 dark:text-gray-400">{t('settings.language.description')}</p>
                    </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                    {/* Card Français */}
                    <button
                        onClick={() => changeLanguage('fr')}
                        className={`p-4 rounded-xl border-2 text-center transition-all ${
                            currentLanguage === 'fr'
                                ? 'border-indigo-600 bg-indigo-50 dark:bg-indigo-900/20'
                                : 'border-gray-200 dark:border-slate-700 hover:border-indigo-400 dark:hover:border-indigo-600'
                        }`}
                    >
                        <span className="text-3xl mb-2 block">🇫🇷</span>
                        <span className="font-medium text-gray-900 dark:text-white">Français</span>
                        {currentLanguage === 'fr' && <CheckCircle className="h-4 w-4 text-indigo-600 mx-auto mt-2" />}
                    </button>

                    {/* Card English */}
                    <button
                        onClick={() => changeLanguage('en')}
                        className={`p-4 rounded-xl border-2 text-center transition-all ${
                            currentLanguage === 'en'
                                ? 'border-indigo-600 bg-indigo-50 dark:bg-indigo-900/20'
                                : 'border-gray-200 dark:border-slate-700 hover:border-indigo-400 dark:hover:border-indigo-600'
                        }`}
                    >
                        <span className="text-3xl mb-2 block">🇬🇧</span>
                        <span className="font-medium text-gray-900 dark:text-white">English</span>
                        {currentLanguage === 'en' && <CheckCircle className="h-4 w-4 text-indigo-600 mx-auto mt-2" />}
                    </button>
                </div>
            </Card>

            {/* Theme Section */}
            <Card className="p-6 border border-gray-100 dark:border-slate-800 shadow-sm bg-white dark:bg-slate-900 rounded-2xl">
                <div className="flex items-center gap-3 mb-6">
                    <div className="w-10 h-10 rounded-xl bg-amber-100 dark:bg-amber-900/20 flex items-center justify-center text-amber-600">
                        <Palette size={20} />
                    </div>
                    <div>
                        <h2 className="text-xl font-bold text-gray-900 dark:text-white">
                            {t('settings.theme.title')}
                        </h2>
                        <p className="text-sm text-gray-500 dark:text-gray-400">{t('settings.theme.description')}</p>
                    </div>
                </div>

                <div className="grid grid-cols-3 gap-4">
                    {/* Card Light */}
                    <button
                        onClick={() => setTheme("light")}
                        className={`p-4 rounded-xl border-2 text-center transition-all ${
                            theme === 'light'
                                ? 'border-indigo-600 bg-indigo-50 dark:bg-indigo-900/20'
                                : 'border-gray-200 dark:border-slate-700 hover:border-indigo-400 dark:hover:border-indigo-600'
                        }`}
                    >
                        <Sun className="h-6 w-6 mx-auto mb-2 text-gray-700 dark:text-gray-300" />
                        <span className="font-medium text-gray-900 dark:text-white">{t('settings.theme.light')}</span>
                        {theme === 'light' && <CheckCircle className="h-4 w-4 text-indigo-600 mx-auto mt-2" />}
                    </button>

                    {/* Card Dark */}
                    <button
                        onClick={() => setTheme("dark")}
                        className={`p-4 rounded-xl border-2 text-center transition-all ${
                            theme === 'dark'
                                ? 'border-indigo-600 bg-indigo-50 dark:bg-indigo-900/20'
                                : 'border-gray-200 dark:border-slate-700 hover:border-indigo-400 dark:hover:border-indigo-600'
                        }`}
                    >
                        <Moon className="h-6 w-6 mx-auto mb-2 text-gray-700 dark:text-gray-300" />
                        <span className="font-medium text-gray-900 dark:text-white">{t('settings.theme.dark')}</span>
                        {theme === 'dark' && <CheckCircle className="h-4 w-4 text-indigo-600 mx-auto mt-2" />}
                    </button>

                    {/* Card System */}
                    <button
                        onClick={() => setTheme("system")}
                        className={`p-4 rounded-xl border-2 text-center transition-all ${
                            theme === 'system'
                                ? 'border-indigo-600 bg-indigo-50 dark:bg-indigo-900/20'
                                : 'border-gray-200 dark:border-slate-700 hover:border-indigo-400 dark:hover:border-indigo-600'
                        }`}
                    >
                        <Monitor className="h-6 w-6 mx-auto mb-2 text-gray-700 dark:text-gray-300" />
                        <span className="font-medium text-gray-900 dark:text-white">{t('settings.theme.system')}</span>
                        {theme === 'system' && <CheckCircle className="h-4 w-4 text-indigo-600 mx-auto mt-2" />}
                    </button>
                </div>
            </Card>

            {/* Notifications Section */}
            <Card className="p-6 border border-gray-100 dark:border-slate-800 shadow-sm bg-white dark:bg-slate-900 rounded-2xl">
                <div className="flex items-center gap-3 mb-6">
                    <div className="w-10 h-10 rounded-xl bg-green-100 dark:bg-green-900/20 flex items-center justify-center text-green-600">
                        <Bell size={20} />
                    </div>
                    <h2 className="text-xl font-bold text-gray-900 dark:text-white">
                        {t('settings.notifications.title')}
                    </h2>
                </div>

                <div className="flex items-center justify-between p-4 rounded-xl border border-gray-100 dark:border-slate-800 bg-gray-50 dark:bg-slate-800/50">
                    <div>
                        <p className="font-medium text-gray-900 dark:text-white">{t('settings.notifications.emailNotifications')}</p>
                        <p className="text-sm text-gray-500 dark:text-gray-400">{t('settings.notifications.emailNotificationsDescription')}</p>
                    </div>
                    <Switch
                        checked={emailNotifications}
                        onCheckedChange={setEmailNotifications}
                    />
                </div>
            </Card>
        </div>
    );
}