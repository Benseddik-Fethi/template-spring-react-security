import { useCallback, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useAuth } from "@/context/AuthContext";
import { useTheme } from "@/components/ThemeProvider";
import { Card } from "@/components/ui/card";
import { Switch } from "@/components/ui/switch";
import { AlertTriangle, Download, Globe, Key, LogOut, Moon, Palette, Shield, Sun, Trash2 } from "lucide-react";
import { cn } from "@/lib/utils";
import { LanguageSwitcher } from "@/components/LanguageSwitcher";
import { Button } from "@/components/ui/button";

export default function SettingsPage() {
    const { t } = useTranslation('pages');
    const { logout } = useAuth();
    const { theme, setTheme } = useTheme();
    const [activeSection, setActiveSection] = useState("language");

    // Define menu items with translations
    const menuItems = useMemo(() => [
        { id: "language", label: t('settings.language.title'), icon: Globe },
        { id: "appearance", label: t('settings.appearance.title'), icon: Palette },
        { id: "security", label: t('settings.security.title'), icon: Shield },
        { id: "account", label: t('settings.account.title'), icon: AlertTriangle },
    ] as const, [t]);

    // Memoize dark mode calculation
    const isDarkMode = useMemo(() => {
        const isSystemDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
        return theme === "dark" || (theme === "system" && isSystemDark);
    }, [theme]);

    // Memoize system theme state
    const isSystemTheme = useMemo(() => theme === "system", [theme]);

    // Memoize theme toggle handler
    const toggleTheme = useCallback((checked: boolean) => {
        const newTheme = checked ? "dark" : "light";
        setTheme(newTheme);
    }, [setTheme]);

    // Handler for automatic theme
    const toggleSystemTheme = useCallback((checked: boolean) => {
        if (checked) {
            setTheme("system");
        } else {
            const isSystemDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
            setTheme(isSystemDark ? "dark" : "light");
        }
    }, [setTheme]);

    return (
        <div className="space-y-6 pb-10">
            <div>
                <h1 className="text-3xl font-bold text-gray-800 dark:text-white">{t('settings.title')}</h1>
                <p className="text-gray-500 dark:text-gray-400 mt-1">{t('settings.description')}</p>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
                {/* SIDE MENU */}
                <Card className="p-4 h-fit lg:col-span-1 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm border-white/20">
                    <nav className="space-y-1">
                        {menuItems.map((item) => {
                            const Icon = item.icon;
                            const isActive = activeSection === item.id;
                            return (
                                <button
                                    key={item.id}
                                    onClick={() => setActiveSection(item.id)}
                                    className={cn(
                                        "w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-300 text-sm font-medium",
                                        isActive
                                            ? "bg-gradient-to-r from-indigo-500 to-purple-500 text-white shadow-md"
                                            : "text-gray-600 dark:text-gray-300 hover:bg-indigo-50 dark:hover:bg-slate-800"
                                    )}
                                >
                                    <Icon size={18} />
                                    {item.label}
                                </button>
                            );
                        })}
                    </nav>

                    <div className="mt-6 pt-6 border-t border-gray-100 dark:border-slate-800">
                        <button
                            onClick={logout}
                            className="w-full flex items-center gap-3 px-4 py-3 rounded-xl text-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 transition-all text-sm font-medium"
                        >
                            <LogOut size={18} />
                            {t('settings.logout')}
                        </button>
                    </div>
                </Card>

                {/* CONTENT */}
                <div className="lg:col-span-3">
                    {/* LANGUAGE SECTION */}
                    {activeSection === "language" && (
                        <Card className="p-8 animate-in fade-in slide-in-from-right-4 dark:bg-slate-900 border-white/10">
                            <h2 className="text-xl font-bold text-gray-800 dark:text-white mb-2 flex items-center gap-2">
                                <Globe className="text-indigo-500" /> {t('settings.language.title')}
                            </h2>
                            <p className="text-gray-500 dark:text-gray-400 mb-6">{t('settings.language.description')}</p>

                            <div className="space-y-6">
                                <div className="flex items-center justify-between p-4 bg-gray-50 dark:bg-slate-950 rounded-2xl border border-gray-100 dark:border-slate-800">
                                    <div className="flex items-center gap-4">
                                        <div className="w-12 h-12 bg-indigo-100 dark:bg-indigo-900/30 rounded-xl flex items-center justify-center">
                                            <Globe className="text-indigo-600 dark:text-indigo-400" size={24} />
                                        </div>
                                        <div>
                                            <p className="font-bold text-gray-800 dark:text-white">{t('settings.language.selectLabel')}</p>
                                            <p className="text-sm text-gray-500 dark:text-gray-400">{t('settings.language.selectDescription')}</p>
                                        </div>
                                    </div>
                                    <LanguageSwitcher />
                                </div>
                            </div>
                        </Card>
                    )}

                    {/* APPEARANCE SECTION */}
                    {activeSection === "appearance" && (
                        <Card className="p-8 animate-in fade-in slide-in-from-right-4 dark:bg-slate-900 border-white/10">
                            <h2 className="text-xl font-bold text-gray-800 dark:text-white mb-2 flex items-center gap-2">
                                <Palette className="text-indigo-500" /> {t('settings.appearance.title')}
                            </h2>
                            <p className="text-gray-500 dark:text-gray-400 mb-6">{t('settings.appearance.description')}</p>

                            <div className="space-y-6">
                                {/* Dark Mode Option */}
                                <div
                                    className={cn(
                                        "flex items-center justify-between p-4 bg-gray-50 dark:bg-slate-950 rounded-2xl border border-gray-100 dark:border-slate-800",
                                        isSystemTheme && "opacity-50 cursor-not-allowed"
                                    )}
                                >
                                    <div className="flex items-center gap-4">
                                        <div
                                            className={cn(
                                                "w-12 h-12 rounded-xl flex items-center justify-center transition-colors",
                                                isDarkMode ? "bg-indigo-100 text-indigo-600" : "bg-amber-100 text-amber-600"
                                            )}
                                        >
                                            {isDarkMode ? <Moon size={24} /> : <Sun size={24} />}
                                        </div>
                                        <div>
                                            <p className="font-bold text-gray-800 dark:text-white">{t('settings.appearance.darkMode')}</p>
                                            <p className="text-sm text-gray-500 dark:text-gray-400">
                                                {isSystemTheme
                                                    ? t('settings.appearance.controlledBySystem')
                                                    : isDarkMode
                                                        ? t('settings.appearance.darkModeEnabled')
                                                        : t('settings.appearance.darkModeDisabled')}
                                            </p>
                                        </div>
                                    </div>
                                    <Switch
                                        checked={isDarkMode}
                                        onCheckedChange={toggleTheme}
                                        disabled={isSystemTheme}
                                        className="data-[state=checked]:bg-indigo-500 data-[state=unchecked]:bg-gray-200"
                                    />
                                </div>

                                {/* System Theme Option */}
                                <div className="flex items-center justify-between p-4 bg-gray-50 dark:bg-slate-950 rounded-2xl border border-gray-100 dark:border-slate-800">
                                    <div className="flex items-center gap-4">
                                        <div
                                            className={cn(
                                                "w-12 h-12 rounded-xl flex items-center justify-center transition-colors",
                                                isSystemTheme ? "bg-indigo-100 text-indigo-600" : "bg-gray-200 text-gray-500"
                                            )}
                                        >
                                            <Palette size={24} />
                                        </div>
                                        <div>
                                            <p className="font-bold text-gray-800 dark:text-white">{t('settings.appearance.autoTheme')}</p>
                                            <p className="text-sm text-gray-500 dark:text-gray-400">
                                                {isSystemTheme
                                                    ? t('settings.appearance.themeFollowsSystem')
                                                    : t('settings.appearance.syncWithSystem')}
                                            </p>
                                        </div>
                                    </div>
                                    <Switch
                                        checked={isSystemTheme}
                                        onCheckedChange={toggleSystemTheme}
                                        className="data-[state=checked]:bg-indigo-500 data-[state=unchecked]:bg-gray-200"
                                    />
                                </div>
                            </div>
                        </Card>
                    )}

                    {/* SECURITY SECTION */}
                    {activeSection === "security" && (
                        <Card className="p-8 animate-in fade-in slide-in-from-right-4 dark:bg-slate-900 border-white/10">
                            <h2 className="text-xl font-bold text-gray-800 dark:text-white mb-2 flex items-center gap-2">
                                <Shield className="text-indigo-500" /> {t('settings.security.title')}
                            </h2>
                            <p className="text-gray-500 dark:text-gray-400 mb-6">{t('settings.security.description')}</p>

                            <div className="space-y-6">
                                {/* Change Password */}
                                <div className="flex items-center justify-between p-4 bg-gray-50 dark:bg-slate-950 rounded-2xl border border-gray-100 dark:border-slate-800">
                                    <div className="flex items-center gap-4">
                                        <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900/30 rounded-xl flex items-center justify-center">
                                            <Key className="text-blue-600 dark:text-blue-400" size={24} />
                                        </div>
                                        <div>
                                            <p className="font-bold text-gray-800 dark:text-white">{t('settings.security.changePassword')}</p>
                                            <p className="text-sm text-gray-500 dark:text-gray-400">{t('settings.security.changePasswordDescription')}</p>
                                        </div>
                                    </div>
                                    <Button variant="outline" size="sm">
                                        {t('settings.security.change')}
                                    </Button>
                                </div>

                                {/* Active Sessions */}
                                <div className="flex items-center justify-between p-4 bg-gray-50 dark:bg-slate-950 rounded-2xl border border-gray-100 dark:border-slate-800">
                                    <div className="flex items-center gap-4">
                                        <div className="w-12 h-12 bg-green-100 dark:bg-green-900/30 rounded-xl flex items-center justify-center">
                                            <Shield className="text-green-600 dark:text-green-400" size={24} />
                                        </div>
                                        <div>
                                            <p className="font-bold text-gray-800 dark:text-white">{t('settings.security.sessions')}</p>
                                            <p className="text-sm text-gray-500 dark:text-gray-400">{t('settings.security.sessionsDescription')}</p>
                                        </div>
                                    </div>
                                    <Button variant="outline" size="sm">
                                        {t('settings.security.view')}
                                    </Button>
                                </div>
                            </div>
                        </Card>
                    )}

                    {/* ACCOUNT / DANGER ZONE */}
                    {activeSection === "account" && (
                        <Card className="p-8 animate-in fade-in slide-in-from-right-4 dark:bg-slate-900 border-white/10">
                            <h2 className="text-xl font-bold text-gray-800 dark:text-white mb-2 flex items-center gap-2">
                                <AlertTriangle className="text-amber-500" /> {t('settings.account.title')}
                            </h2>
                            <p className="text-gray-500 dark:text-gray-400 mb-6">{t('settings.account.description')}</p>

                            <div className="space-y-6">
                                {/* Export Data */}
                                <div className="flex items-center justify-between p-4 bg-gray-50 dark:bg-slate-950 rounded-2xl border border-gray-100 dark:border-slate-800">
                                    <div className="flex items-center gap-4">
                                        <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900/30 rounded-xl flex items-center justify-center">
                                            <Download className="text-blue-600 dark:text-blue-400" size={24} />
                                        </div>
                                        <div>
                                            <p className="font-bold text-gray-800 dark:text-white">{t('settings.account.exportData')}</p>
                                            <p className="text-sm text-gray-500 dark:text-gray-400">{t('settings.account.exportDataDescription')}</p>
                                        </div>
                                    </div>
                                    <Button variant="outline" size="sm">
                                        {t('settings.account.export')}
                                    </Button>
                                </div>

                                {/* Danger Zone - Delete Account */}
                                <div className="mt-8 pt-6 border-t border-gray-200 dark:border-slate-800">
                                    <h3 className="text-lg font-semibold text-red-600 dark:text-red-400 mb-4 flex items-center gap-2">
                                        <AlertTriangle size={20} />
                                        {t('settings.account.dangerZone')}
                                    </h3>
                                    <div className="flex items-center justify-between p-4 bg-red-50 dark:bg-red-900/20 rounded-2xl border border-red-200 dark:border-red-800">
                                        <div className="flex items-center gap-4">
                                            <div className="w-12 h-12 bg-red-100 dark:bg-red-900/30 rounded-xl flex items-center justify-center">
                                                <Trash2 className="text-red-600 dark:text-red-400" size={24} />
                                            </div>
                                            <div>
                                                <p className="font-bold text-gray-800 dark:text-white">{t('settings.account.deleteAccount')}</p>
                                                <p className="text-sm text-red-600 dark:text-red-400">{t('settings.account.deleteWarning')}</p>
                                            </div>
                                        </div>
                                        <Button variant="destructive" size="sm">
                                            {t('settings.account.delete')}
                                        </Button>
                                    </div>
                                </div>
                            </div>
                        </Card>
                    )}
                </div>
            </div>
        </div>
    );
}