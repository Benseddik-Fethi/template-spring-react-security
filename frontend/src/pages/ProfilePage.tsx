import { useTranslation } from "react-i18next";
import { useAuth } from "@/context/AuthContext";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { AlertTriangle, Key, Monitor, Pencil, Shield, Trash2, User } from "lucide-react";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";

export default function ProfilePage() {
    const { t } = useTranslation(['pages', 'common']);
    const { user, isLoading } = useAuth();

    // Format date helper
    const formatDate = (date: string | undefined | null) => {
        if (!date) return t('pages:profile.notAvailable');
        return new Date(date).toLocaleDateString(undefined, {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    };

    if (isLoading) {
        return (
            <div className="flex items-center justify-center min-h-[400px]">
                <LoadingSpinner size="lg" />
            </div>
        );
    }

    return (
        <div className="space-y-8 animate-in fade-in duration-500">
            {/* Header */}
            <div>
                <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
                    {t('pages:profile.title')}
                </h1>
                <p className="text-gray-500 dark:text-gray-400 mt-2">
                    {t('pages:profile.subtitle')}
                </p>
            </div>

            {/* Personal Information Section */}
            <Card className="p-6 border border-gray-100 dark:border-slate-800 shadow-sm bg-white dark:bg-slate-900 rounded-2xl">
                {/* Header */}
                <div className="flex items-center justify-between mb-6">
                    <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-xl bg-indigo-100 dark:bg-indigo-900/20 flex items-center justify-center text-indigo-600">
                            <User size={20} />
                        </div>
                        <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
                            {t('pages:profile.personalInfo')}
                        </h2>
                    </div>
                    {/* Compact button on the right */}
                    <Button variant="outline" size="sm">
                        <Pencil className="h-4 w-4 mr-2" />
                        {t('pages:profile.editProfile')}
                    </Button>
                </div>

                {/* Content with Avatar + 2 columns grid */}
                <div className="flex items-start gap-6">
                    {/* Avatar */}
                    <Avatar className="h-16 w-16 flex-shrink-0">
                        <AvatarImage />
                        <AvatarFallback className="text-lg bg-gradient-to-br from-indigo-400 to-purple-400 text-white">
                            {user?.firstName?.[0] || ''}{user?.lastName?.[0] || ''}
                        </AvatarFallback>
                    </Avatar>

                    {/* 2 columns information grid */}
                    <div className="flex-1 grid grid-cols-2 gap-x-8 gap-y-4">
                        <div>
                            <p className="text-sm text-gray-500 dark:text-gray-400">{t('pages:profile.firstName')}</p>
                            <p className="font-medium text-gray-900 dark:text-white">{user?.firstName || '-'}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500 dark:text-gray-400">{t('pages:profile.lastName')}</p>
                            <p className="font-medium text-gray-900 dark:text-white">{user?.lastName || '-'}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500 dark:text-gray-400">{t('pages:profile.email')}</p>
                            <p className="font-medium text-gray-900 dark:text-white">{user?.email || '-'}</p>
                        </div>
                        <div>
                            <p className="text-sm text-gray-500 dark:text-gray-400">{t('pages:profile.memberSince')}</p>
                            <p className="font-medium text-gray-900 dark:text-white">{formatDate(null)}</p>
                        </div>
                    </div>
                </div>
            </Card>

            {/* Security Section */}
            <Card className="p-6 border border-gray-100 dark:border-slate-800 shadow-sm bg-white dark:bg-slate-900 rounded-2xl">
                <div className="flex items-center gap-3 mb-6">
                    <div className="w-10 h-10 rounded-xl bg-green-100 dark:bg-green-900/20 flex items-center justify-center text-green-600">
                        <Shield size={20} />
                    </div>
                    <h2 className="text-xl font-bold text-gray-900 dark:text-white">
                        {t('pages:profile.security.title')}
                    </h2>
                </div>

                <div className="space-y-4">
                    {/* Change Password */}
                    <div className="flex items-center justify-between p-4 rounded-xl border border-gray-100 dark:border-slate-800 bg-gray-50 dark:bg-slate-800/50">
                        <div className="flex items-center gap-3">
                            <div className="w-10 h-10 rounded-xl bg-amber-100 dark:bg-amber-900/20 flex items-center justify-center text-amber-600">
                                <Key size={20} />
                            </div>
                            <div>
                                <p className="font-medium text-gray-900 dark:text-white">{t('pages:profile.security.changePassword')}</p>
                                <p className="text-sm text-gray-500 dark:text-gray-400">{t('pages:profile.security.changePasswordDescription')}</p>
                            </div>
                        </div>
                        <Button variant="outline">{t('common:actions.edit')}</Button>
                    </div>

                    {/* Active Sessions */}
                    <div className="flex items-center justify-between p-4 rounded-xl border border-gray-100 dark:border-slate-800 bg-gray-50 dark:bg-slate-800/50">
                        <div className="flex items-center gap-3">
                            <div className="w-10 h-10 rounded-xl bg-blue-100 dark:bg-blue-900/20 flex items-center justify-center text-blue-600">
                                <Monitor size={20} />
                            </div>
                            <div>
                                <p className="font-medium text-gray-900 dark:text-white">{t('pages:profile.security.sessions')}</p>
                                <p className="text-sm text-gray-500 dark:text-gray-400">{t('pages:profile.security.sessionsDescription')}</p>
                            </div>
                        </div>
                        <Button variant="outline">{t('common:actions.view')}</Button>
                    </div>
                </div>
            </Card>

            {/* Danger Zone */}
            <Card className="p-6 border border-red-200 dark:border-red-900/50 shadow-sm bg-white dark:bg-slate-900 rounded-2xl">
                <div className="flex items-center gap-3 mb-6">
                    <div className="w-10 h-10 rounded-xl bg-red-100 dark:bg-red-900/20 flex items-center justify-center text-red-600">
                        <AlertTriangle size={20} />
                    </div>
                    <h2 className="text-xl font-bold text-red-600 dark:text-red-400">
                        {t('pages:profile.dangerZone.title')}
                    </h2>
                </div>

                <div className="flex items-center justify-between p-4 rounded-xl border border-red-200 dark:border-red-900/30 bg-red-50 dark:bg-red-900/10">
                    <div>
                        <p className="font-medium text-gray-900 dark:text-white">{t('pages:profile.dangerZone.deleteAccount')}</p>
                        <p className="text-sm text-gray-500 dark:text-gray-400">{t('pages:profile.dangerZone.deleteWarning')}</p>
                    </div>
                    <Button variant="destructive">
                        <Trash2 className="h-4 w-4 mr-2" />
                        {t('common:actions.delete')}
                    </Button>
                </div>
            </Card>
        </div>
    );
}
