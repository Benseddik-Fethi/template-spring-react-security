import {Navigate, Route, Routes} from 'react-router-dom';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import {ProtectedRoute} from './components/ProtectedRoute';
import DashboardLayout from './layouts/DashboardLayout';
import SettingsPage from "@/pages/SettingsPage.tsx";

function RootRedirect() {
    const {user, isLoading} = useAuth();
    if (isLoading) return <div className="min-h-screen flex items-center justify-center">Chargement...</div>;
    return <Navigate to={user ? "/dashboard" : "/login"} replace/>;
}

function App() {
    return (
        <Routes>
            {/* Route racine */}
            <Route path="/" element={<RootRedirect/>}/>

            {/* Pages publiques */}
            <Route path="/register" element={<RegisterPage/>}/>
            <Route path="/login" element={<LoginPage/>}/>

            {/* Flux d'authentification et emails */}
            <Route path="/auth/callback" element={<AuthCallbackPage/>}/>
            <Route path="/auth/verify-email-sent" element={<EmailSentPage/>}/>
            <Route path="/auth/verify-email" element={<VerifyEmailPage/>}/>
            <Route path="/auth/resend-verification" element={<ResendVerificationPage/>}/>

            {/* Espace sécurisé (Dashboard) */}
            <Route element={<ProtectedRoute/>}>
                <Route element={<DashboardLayout/>}>
                    <Route path="/dashboard" element={<DashboardPage/>}/>
                    {/* On map Settings sur /profile pour l'instant aussi */}
                    <Route path="/profile" element={<SettingsPage/>}/>
                    <Route path="/settings" element={<SettingsPage/>}/>
                </Route>
            </Route>

            {/* Fallback 404 (optionnel) */}
            <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
    );
}

export default App;