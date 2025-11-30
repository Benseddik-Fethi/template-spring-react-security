
import { useTranslation } from "react-i18next";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Activity, CheckCircle, Code, Shield, Zap , User} from "lucide-react";
import {useAuth} from "@/context/AuthContext.tsx";

export default function DashboardPage() {
    const { t } = useTranslation('pages');
    const { user } = useAuth();

    return (
        <div className="space-y-8 animate-in fade-in duration-500">
            {/* Header */}
            <div className="flex justify-between items-end">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
                        {t('dashboard.title')}
                    </h1>
                    <p className="text-gray-500 dark:text-gray-400 mt-2">
                        {t('dashboard.welcomeMessage')}, <span className="font-semibold text-indigo-600 dark:text-indigo-400">{user?.firstName}</span>.
                    </p>
                </div>
                <div className="flex gap-3">
                    <Button variant="outline">{t('dashboard.documentation')}</Button>
                    <Button className="bg-indigo-600 hover:bg-indigo-700 text-white">
                        {t('dashboard.newAction')}
                    </Button>
                </div>
            </div>

            {/* KPI Cards */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <Card className="p-6 border border-gray-100 dark:border-slate-800 shadow-sm bg-white dark:bg-slate-900 rounded-2xl flex items-center gap-4">
                    <div className="w-12 h-12 rounded-xl bg-green-100 dark:bg-green-900/20 flex items-center justify-center text-green-600">
                        <Shield size={24}/>
                    </div>
                    <div>
                        <p className="text-gray-500 dark:text-gray-400 text-sm font-medium">{t('dashboard.securityStatus')}</p>
                        <div className="flex items-center gap-2">
                            <p className="text-xl font-bold text-gray-900 dark:text-white">{t('dashboard.secured')}</p>
                            <CheckCircle size={16} className="text-green-500" />
                        </div>
                    </div>
                </Card>

                <Card className="p-6 border border-gray-100 dark:border-slate-800 shadow-sm bg-white dark:bg-slate-900 rounded-2xl flex items-center gap-4">
                    <div className="w-12 h-12 rounded-xl bg-indigo-100 dark:bg-indigo-900/20 flex items-center justify-center text-indigo-600">
                        <User size={24}/>
                    </div>
                    <div>
                        <p className="text-gray-500 dark:text-gray-400 text-sm font-medium">{t('dashboard.userRole')}</p>
                        <p className="text-xl font-bold text-gray-900 dark:text-white uppercase">{user?.role}</p>
                    </div>
                </Card>

                <Card className="p-6 border border-gray-100 dark:border-slate-800 shadow-sm bg-white dark:bg-slate-900 rounded-2xl flex items-center gap-4">
                    <div className="w-12 h-12 rounded-xl bg-amber-100 dark:bg-amber-900/20 flex items-center justify-center text-amber-600">
                        <Activity size={24}/>
                    </div>
                    <div>
                        <p className="text-gray-500 dark:text-gray-400 text-sm font-medium">{t('dashboard.apiPerformance')}</p>
                        <p className="text-xl font-bold text-gray-900 dark:text-white">{t('dashboard.optimal')}</p>
                    </div>
                </Card>
            </div>

            {/* Starter Content */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                <Card className="p-8 border border-gray-200 dark:border-slate-800 bg-white dark:bg-slate-900 rounded-3xl shadow-sm">
                    <div className="w-12 h-12 bg-blue-50 dark:bg-blue-900/20 rounded-xl flex items-center justify-center text-blue-600 mb-6">
                        <Code size={24}/>
                    </div>
                    <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-2">{t('dashboard.readyToCode')}</h3>
                    <p className="text-gray-500 dark:text-gray-400 mb-6 leading-relaxed">
                        {t('dashboard.templateDescription')}
                    </p>
                    <div className="flex flex-col gap-3">
                        <div className="flex items-center gap-3 text-sm text-gray-600 dark:text-gray-300">
                            <div className="w-6 h-6 rounded-full bg-green-100 text-green-600 flex items-center justify-center text-xs">✓</div>
                            {t('dashboard.backendFeature')}
                        </div>
                        <div className="flex items-center gap-3 text-sm text-gray-600 dark:text-gray-300">
                            <div className="w-6 h-6 rounded-full bg-green-100 text-green-600 flex items-center justify-center text-xs">✓</div>
                            {t('dashboard.frontendFeature')}
                        </div>
                        <div className="flex items-center gap-3 text-sm text-gray-600 dark:text-gray-300">
                            <div className="w-6 h-6 rounded-full bg-green-100 text-green-600 flex items-center justify-center text-xs">✓</div>
                            {t('dashboard.securityFeature')}
                        </div>
                    </div>
                </Card>

                <Card className="p-8 border border-dashed border-gray-300 dark:border-slate-700 bg-gray-50/50 dark:bg-slate-900/50 rounded-3xl flex flex-col items-center justify-center text-center">
                    <div className="w-16 h-16 bg-gray-100 dark:bg-slate-800 rounded-full flex items-center justify-center mb-4">
                        <Zap size={32} className="text-gray-400"/>
                    </div>
                    <h3 className="text-lg font-semibold text-gray-700 dark:text-gray-300">{t('dashboard.yourContentHere')}</h3>
                    <p className="text-gray-500 text-sm mt-2 max-w-xs">
                        {t('dashboard.startAdding')}
                    </p>
                </Card>
            </div>
        </div>
    );
}