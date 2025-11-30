
import {Outlet, useLocation, useNavigate} from "react-router-dom";
import { useTranslation } from "react-i18next";
import {Home, LogOut, Settings, ShieldCheck, User} from "lucide-react"; // Icônes génériques
import {cn} from "@/lib/utils";
import {useAuth} from "@/context/AuthContext";

export default function DashboardLayout() {
    const { t } = useTranslation('pages');
    const {user, logout} = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    // Menu générique pour le template
    const navItems = [
        {id: "/dashboard", label: t('layout.dashboard'), icon: Home},
        {id: "/profile", label: t('layout.profile'), icon: User},
        {id: "/settings", label: t('layout.settings'), icon: Settings},
    ];

    return (
        <div className="min-h-screen bg-gray-50 dark:from-slate-950 dark:via-slate-900 dark:to-slate-950 flex">
            {/* SIDEBAR */}
            <div className="w-72 bg-white dark:bg-slate-900 border-r border-gray-200 dark:border-slate-800 p-6 flex flex-col fixed h-full z-20 transition-all">

                {/* Logo / Brand */}
                <div className="flex items-center gap-3 mb-10 px-2">
                    <div className="w-10 h-10 bg-indigo-600 rounded-xl flex items-center justify-center shadow-lg shadow-indigo-200 dark:shadow-none">
                        <ShieldCheck size={24} className="text-white"/>
                    </div>
                    <span className="text-xl font-bold text-gray-900 dark:text-white tracking-tight">
                        {t('layout.brandName')}
                    </span>
                </div>

                {/* Navigation */}
                <nav className="flex-1 space-y-1">
                    {navItems.map((item) => {
                        const Icon = item.icon;
                        const isActive = location.pathname === item.id;
                        return (
                            <button
                                key={item.id}
                                onClick={() => navigate(item.id)}
                                className={cn(
                                    "w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 font-medium text-sm",
                                    isActive
                                        ? "bg-indigo-50 dark:bg-indigo-900/20 text-indigo-600 dark:text-indigo-400"
                                        : "text-gray-600 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-slate-800 hover:text-gray-900 dark:hover:text-gray-200"
                                )}
                            >
                                <Icon size={18}/>
                                <span>{item.label}</span>
                            </button>
                        );
                    })}
                </nav>

                {/* User Footer */}
                <div className="pt-4 border-t border-gray-100 dark:border-slate-800">
                    <div className="flex items-center gap-3 p-2 rounded-xl hover:bg-gray-50 dark:hover:bg-slate-800 transition-colors cursor-pointer group">
                        <div className="w-10 h-10 bg-gradient-to-br from-gray-100 to-gray-200 dark:from-slate-700 dark:to-slate-600 rounded-full flex items-center justify-center text-gray-600 dark:text-gray-300 font-bold border border-gray-200 dark:border-slate-700">
                            {user?.firstName?.charAt(0) || "U"}
                        </div>
                        <div className="flex-1 min-w-0">
                            <p className="text-sm font-semibold text-gray-900 dark:text-gray-200 truncate">
                                {user?.firstName} {user?.lastName}
                            </p>
                            <p className="text-xs text-gray-500 dark:text-gray-500 truncate">
                                {user?.email}
                            </p>
                        </div>
                        <button
                            onClick={logout}
                            className="p-2 text-gray-400 hover:text-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-all"
                            title={t('layout.logout')}
                        >
                            <LogOut size={18}/>
                        </button>
                    </div>
                </div>
            </div>

            {/* MAIN CONTENT AREA */}
            <div className="flex-1 ml-72 p-8 overflow-auto bg-gray-50 dark:bg-slate-950 min-h-screen">
                <div className="max-w-7xl mx-auto">
                    <Outlet/>
                </div>
            </div>
        </div>
    );
}