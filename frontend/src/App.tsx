import {Navigate, Route, Routes} from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import SettingsPage from "@/pages/SettingsPage";
import ProfilePage from "@/pages/ProfilePage";
import {useAuth} from "@/context/AuthContext";
import RegisterPage from "@/pages/RegisterPage.tsx";
import LoginPage from "@/pages/LoginPage.tsx";
import {ProtectedRoute} from "@/components/ProtectedRoute.tsx";
import DashboardLayout from "@/layouts/DashboardLayout.tsx";
import DashboardPage from "@/pages/DashboardPage.tsx";
import AuthCallbackPage from "@/pages/AuthCallbackPage.tsx";
import EmailSentPage from "@/pages/EmailSentPage.tsx";
import VerifyEmailPage from "@/pages/VerifyEmailPage.tsx";
import ResendVerificationPage from "@/pages/ResendVerificationPage.tsx";
import ForgotPasswordPage from "@/pages/ForgotPasswordPage.tsx";
import ResetPasswordPage from "@/pages/ResetPasswordPage.tsx";
import { ROUTES } from "@/config";

function RootRedirect() {
    const { t } = useTranslation('pages');
    const {user, isLoading} = useAuth();
    if (isLoading) return <div className="min-h-screen flex items-center justify-center">{t('loading')}</div>;
    return <Navigate to={user ? ROUTES.DASHBOARD : ROUTES.LOGIN} replace/>;
}

function App() {
    return (
        <Routes>
            {/* Route racine */}
            <Route path={ROUTES.HOME} element={<RootRedirect/>}/>

            {/* Pages publiques */}
            <Route path={ROUTES.REGISTER} element={<RegisterPage/>}/>
            <Route path={ROUTES.LOGIN} element={<LoginPage/>}/>

            {/* Flux d'authentification et emails */}
            <Route path={ROUTES.AUTH.CALLBACK} element={<AuthCallbackPage/>}/>
            <Route path={ROUTES.AUTH.VERIFY_EMAIL_SENT} element={<EmailSentPage/>}/>
            <Route path={ROUTES.AUTH.VERIFY_EMAIL} element={<VerifyEmailPage/>}/>
            <Route path={ROUTES.AUTH.RESEND_VERIFICATION} element={<ResendVerificationPage/>}/>
            <Route path={ROUTES.AUTH.FORGOT_PASSWORD} element={<ForgotPasswordPage/>}/>
            <Route path={ROUTES.AUTH.RESET_PASSWORD} element={<ResetPasswordPage/>}/>

            {/* Espace sécurisé (Dashboard) */}
            <Route element={<ProtectedRoute/>}>
                <Route element={<DashboardLayout/>}>
                    <Route path={ROUTES.DASHBOARD} element={<DashboardPage/>}/>
                    <Route path={ROUTES.PROFILE} element={<ProfilePage/>}/>
                    <Route path={ROUTES.SETTINGS} element={<SettingsPage/>}/>
                </Route>
            </Route>

            {/* Fallback 404 (optionnel) */}
            <Route path="*" element={<Navigate to={ROUTES.HOME} replace />} />
        </Routes>
    );
}

export default App;