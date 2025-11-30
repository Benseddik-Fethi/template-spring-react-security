import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useTheme } from "@/components/ThemeProvider";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Switch } from "@/components/ui/switch";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Label } from "@/components/ui/label";
import { AlertTriangle, Bell, Download, Key, Moon, Palette, Shield, Sun, Trash2, Monitor } from "lucide-react";
import { Button } from "@/components/ui/button";

export default function SettingsPage() {
    const { t } = useTranslation('pages');
    const { theme, setTheme } = useTheme();
    const [emailNotifications, setEmailNotifications] = useState(true);

    return (
        <div className="space-y-6 pb-10">
            {/* Header */}
            <div>
                <h1 className="text-3xl font-bold text-gray-800 dark:text-white">{t('settings.title')}</h1>
                <p className="text-gray-500 dark:text-gray-400 mt-1">{t('settings.description')}</p>
            </div>

            {/* Theme Section */}
            <Card className="bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm border-white/20">
                <CardHeader>
                    <div className="flex items-center gap-2">
                        <Palette className="text-indigo-500" size={20} />
                        <CardTitle>🎨 {t('settings.theme.title')}</CardTitle>
                    </div>
                    <CardDescription>{t('settings.theme.description')}</CardDescription>
                </CardHeader>
                <CardContent>
                    <RadioGroup
                        value={theme}
                        onValueChange={(value) => setTheme(value as "light" | "dark" | "system")}
                        className="grid grid-cols-1 md:grid-cols-3 gap-4"
                    >
                        {/* Light Theme Option */}
                        <div className="flex items-center space-x-3 p-4 bg-gray-50 dark:bg-slate-950 rounded-xl border border-gray-100 dark:border-slate-800">
                            <RadioGroupItem value="light" id="light" />
                            <Label htmlFor="light" className="flex items-center gap-3 cursor-pointer flex-1">
                                <div className="w-10 h-10 bg-amber-100 dark:bg-amber-900/30 rounded-lg flex items-center justify-center">
                                    <Sun className="text-amber-600 dark:text-amber-400" size={20} />
                                </div>
                                <span className="font-medium text-gray-800 dark:text-white">{t('settings.theme.light')}</span>
                            </Label>
                        </div>

                        {/* Dark Theme Option */}
                        <div className="flex items-center space-x-3 p-4 bg-gray-50 dark:bg-slate-950 rounded-xl border border-gray-100 dark:border-slate-800">
                            <RadioGroupItem value="dark" id="dark" />
                            <Label htmlFor="dark" className="flex items-center gap-3 cursor-pointer flex-1">
                                <div className="w-10 h-10 bg-indigo-100 dark:bg-indigo-900/30 rounded-lg flex items-center justify-center">
                                    <Moon className="text-indigo-600 dark:text-indigo-400" size={20} />
                                </div>
                                <span className="font-medium text-gray-800 dark:text-white">{t('settings.theme.dark')}</span>
                            </Label>
                        </div>

                        {/* System Theme Option */}
                        <div className="flex items-center space-x-3 p-4 bg-gray-50 dark:bg-slate-950 rounded-xl border border-gray-100 dark:border-slate-800">
                            <RadioGroupItem value="system" id="system" />
                            <Label htmlFor="system" className="flex items-center gap-3 cursor-pointer flex-1">
                                <div className="w-10 h-10 bg-gray-100 dark:bg-gray-800 rounded-lg flex items-center justify-center">
                                    <Monitor className="text-gray-600 dark:text-gray-400" size={20} />
                                </div>
                                <span className="font-medium text-gray-800 dark:text-white">{t('settings.theme.system')}</span>
                            </Label>
                        </div>
                    </RadioGroup>
                </CardContent>
            </Card>

            {/* Security Section */}
            <Card className="bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm border-white/20">
                <CardHeader>
                    <div className="flex items-center gap-2">
                        <Shield className="text-indigo-500" size={20} />
                        <CardTitle>🔐 {t('settings.security.title')}</CardTitle>
                    </div>
                </CardHeader>
                <CardContent className="space-y-4">
                    {/* Change Password */}
                    <div className="flex items-center justify-between p-4 bg-gray-50 dark:bg-slate-950 rounded-xl border border-gray-100 dark:border-slate-800">
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
                            {t('settings.security.changePassword')}
                        </Button>
                    </div>

                    {/* Active Sessions */}
                    <div className="flex items-center justify-between p-4 bg-gray-50 dark:bg-slate-950 rounded-xl border border-gray-100 dark:border-slate-800">
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
                            {t('settings.security.sessions')}
                        </Button>
                    </div>
                </CardContent>
            </Card>

            {/* Notifications Section */}
            <Card className="bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm border-white/20">
                <CardHeader>
                    <div className="flex items-center gap-2">
                        <Bell className="text-indigo-500" size={20} />
                        <CardTitle>🔔 {t('settings.notifications.title')}</CardTitle>
                    </div>
                </CardHeader>
                <CardContent>
                    <div className="flex items-center justify-between p-4 bg-gray-50 dark:bg-slate-950 rounded-xl border border-gray-100 dark:border-slate-800">
                        <div className="flex items-center gap-4">
                            <div className="w-12 h-12 bg-purple-100 dark:bg-purple-900/30 rounded-xl flex items-center justify-center">
                                <Bell className="text-purple-600 dark:text-purple-400" size={24} />
                            </div>
                            <div>
                                <p className="font-bold text-gray-800 dark:text-white">{t('settings.notifications.emailNotifications')}</p>
                                <p className="text-sm text-gray-500 dark:text-gray-400">{t('settings.notifications.emailNotificationsDescription')}</p>
                            </div>
                        </div>
                        <Switch
                            checked={emailNotifications}
                            onCheckedChange={setEmailNotifications}
                            className="data-[state=checked]:bg-indigo-500 data-[state=unchecked]:bg-gray-200"
                        />
                    </div>
                </CardContent>
            </Card>

            {/* Danger Zone Section */}
            <Card className="bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm border-red-500">
                <CardHeader>
                    <div className="flex items-center gap-2">
                        <AlertTriangle className="text-red-500" size={20} />
                        <CardTitle className="text-red-500">⚠️ {t('settings.dangerZone.title')}</CardTitle>
                    </div>
                </CardHeader>
                <CardContent className="space-y-4">
                    {/* Export Data */}
                    <div className="flex items-center justify-between p-4 bg-gray-50 dark:bg-slate-950 rounded-xl border border-gray-100 dark:border-slate-800">
                        <div className="flex items-center gap-4">
                            <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900/30 rounded-xl flex items-center justify-center">
                                <Download className="text-blue-600 dark:text-blue-400" size={24} />
                            </div>
                            <div>
                                <p className="font-bold text-gray-800 dark:text-white">{t('settings.dangerZone.exportData')}</p>
                                <p className="text-sm text-gray-500 dark:text-gray-400">{t('settings.dangerZone.exportDataDescription')}</p>
                            </div>
                        </div>
                        <Button variant="outline" size="sm">
                            {t('settings.dangerZone.exportData')}
                        </Button>
                    </div>

                    {/* Delete Account */}
                    <div className="flex items-center justify-between p-4 bg-red-50 dark:bg-red-900/20 rounded-xl border border-red-200 dark:border-red-800">
                        <div className="flex items-center gap-4">
                            <div className="w-12 h-12 bg-red-100 dark:bg-red-900/30 rounded-xl flex items-center justify-center">
                                <Trash2 className="text-red-600 dark:text-red-400" size={24} />
                            </div>
                            <div>
                                <p className="font-bold text-gray-800 dark:text-white">{t('settings.dangerZone.deleteAccount')}</p>
                                <p className="text-sm text-red-600 dark:text-red-400">{t('settings.dangerZone.deleteWarning')}</p>
                            </div>
                        </div>
                        <Button variant="destructive" size="sm">
                            {t('settings.dangerZone.confirmDelete')}
                        </Button>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}