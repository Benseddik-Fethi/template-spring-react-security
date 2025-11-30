import { useTranslation } from "react-i18next";
import { useAuth } from "@/context/AuthContext";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Camera, Mail, Calendar, Clock, User, Edit } from "lucide-react";

export default function ProfilePage() {
    const { t } = useTranslation('pages');
    const { user } = useAuth();

    // Format date helper
    const formatDate = (date: string | undefined | null) => {
        if (!date) return t('profile.notAvailable');
        return new Date(date).toLocaleDateString(undefined, {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    };

    return (
        <div className="space-y-6 pb-10">
            {/* Header */}
            <div>
                <h1 className="text-3xl font-bold text-gray-800 dark:text-white">{t('profile.title')}</h1>
                <p className="text-gray-500 dark:text-gray-400 mt-1">{t('profile.description')}</p>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Avatar Card */}
                <Card className="lg:col-span-1 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm border-white/20">
                    <CardContent className="flex flex-col items-center pt-6">
                        <div className="relative mb-4">
                            <div className="w-32 h-32 bg-gradient-to-br from-indigo-400 to-purple-400 rounded-full flex items-center justify-center text-white text-5xl font-bold shadow-lg">
                                {user?.firstName?.charAt(0) || 'U'}
                            </div>
                            <button
                                className="absolute -bottom-2 -right-2 w-10 h-10 bg-white dark:bg-slate-800 rounded-full shadow-md flex items-center justify-center text-gray-400 hover:text-indigo-500 transition-colors border border-gray-200 dark:border-slate-700"
                                title={t('profile.changeAvatar')}
                            >
                                <Camera size={18} />
                            </button>
                        </div>
                        <h2 className="text-xl font-bold text-gray-800 dark:text-white">
                            {user?.firstName} {user?.lastName}
                        </h2>
                        <p className="text-gray-500 dark:text-gray-400 text-sm">{user?.email}</p>
                        <span className="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800 dark:bg-indigo-900/30 dark:text-indigo-400 mt-3">
                            {user?.role || 'USER'}
                        </span>
                    </CardContent>
                </Card>

                {/* Personal Information */}
                <Card className="lg:col-span-2 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm border-white/20">
                    <CardHeader>
                        <div className="flex items-center gap-2">
                            <User className="text-indigo-500" size={20} />
                            <CardTitle>{t('profile.personalInfo')}</CardTitle>
                        </div>
                        <CardDescription>{t('profile.personalInfoDescription')}</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            {/* First Name */}
                            <div className="space-y-1">
                                <label className="text-sm font-medium text-gray-500 dark:text-gray-400">
                                    {t('profile.firstName')}
                                </label>
                                <p className="text-gray-900 dark:text-white font-medium">
                                    {user?.firstName || '-'}
                                </p>
                            </div>

                            {/* Last Name */}
                            <div className="space-y-1">
                                <label className="text-sm font-medium text-gray-500 dark:text-gray-400">
                                    {t('profile.lastName')}
                                </label>
                                <p className="text-gray-900 dark:text-white font-medium">
                                    {user?.lastName || '-'}
                                </p>
                            </div>

                            {/* Email */}
                            <div className="space-y-1 md:col-span-2">
                                <label className="text-sm font-medium text-gray-500 dark:text-gray-400 flex items-center gap-2">
                                    <Mail size={14} />
                                    {t('profile.email')}
                                </label>
                                <p className="text-gray-900 dark:text-white font-medium">
                                    {user?.email || '-'}
                                </p>
                            </div>
                        </div>

                        <div className="mt-6 pt-6 border-t border-gray-100 dark:border-slate-800">
                            <Button className="bg-gray-900 text-white hover:bg-gray-800 dark:bg-white dark:text-black dark:hover:bg-gray-200">
                                <Edit size={16} className="mr-2" />
                                {t('profile.editProfile')}
                            </Button>
                        </div>
                    </CardContent>
                </Card>

                {/* Account Statistics */}
                <Card className="lg:col-span-3 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm border-white/20">
                    <CardHeader>
                        <div className="flex items-center gap-2">
                            <Calendar className="text-indigo-500" size={20} />
                            <CardTitle>{t('profile.accountStats')}</CardTitle>
                        </div>
                        <CardDescription>{t('profile.accountStatsDescription')}</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                            {/* Member Since */}
                            <div className="flex items-center gap-4 p-4 bg-gray-50 dark:bg-slate-950 rounded-xl">
                                <div className="w-12 h-12 bg-green-100 dark:bg-green-900/30 rounded-xl flex items-center justify-center">
                                    <Calendar className="text-green-600 dark:text-green-400" size={24} />
                                </div>
                                <div>
                                    <p className="text-sm text-gray-500 dark:text-gray-400">{t('profile.memberSince')}</p>
                                    <p className="font-semibold text-gray-900 dark:text-white">{formatDate(null)}</p>
                                </div>
                            </div>

                            {/* Last Login */}
                            <div className="flex items-center gap-4 p-4 bg-gray-50 dark:bg-slate-950 rounded-xl">
                                <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900/30 rounded-xl flex items-center justify-center">
                                    <Clock className="text-blue-600 dark:text-blue-400" size={24} />
                                </div>
                                <div>
                                    <p className="text-sm text-gray-500 dark:text-gray-400">{t('profile.lastLogin')}</p>
                                    <p className="font-semibold text-gray-900 dark:text-white">{formatDate(null)}</p>
                                </div>
                            </div>

                            {/* Email Verified */}
                            <div className="flex items-center gap-4 p-4 bg-gray-50 dark:bg-slate-950 rounded-xl">
                                <div className={`w-12 h-12 rounded-xl flex items-center justify-center ${user?.emailVerified ? 'bg-green-100 dark:bg-green-900/30' : 'bg-amber-100 dark:bg-amber-900/30'}`}>
                                    <Mail className={user?.emailVerified ? 'text-green-600 dark:text-green-400' : 'text-amber-600 dark:text-amber-400'} size={24} />
                                </div>
                                <div>
                                    <p className="text-sm text-gray-500 dark:text-gray-400">{t('profile.emailStatus')}</p>
                                    <p className="font-semibold text-gray-900 dark:text-white">
                                        {user?.emailVerified ? t('profile.emailVerified') : t('profile.emailNotVerified')}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </CardContent>
                </Card>
            </div>
        </div>
    );
}
