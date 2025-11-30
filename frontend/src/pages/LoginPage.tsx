import { useState } from "react";
import { ChevronRight, Eye, EyeOff, Lock, Mail, Shield } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Checkbox } from "@/components/ui/checkbox";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { api } from "@/lib/api";
import { Link } from "react-router-dom";
import { isAxiosError } from "axios";
import { FacebookIcon, GoogleIcon } from "@/components/ui/Icons";
import { useAuth } from "@/context/AuthContext";

export default function LoginPage() {
    const [showPassword, setShowPassword] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const { login } = useAuth();

    const handleLogin = async () => {
        try {
            const { data } = await api.post('/auth/login', { email, password });
            login(data.user);
        } catch (error) {
            if (isAxiosError(error)) {
                alert(error.response?.data?.message || "Erreur de connexion");
            } else {
                console.error(error);
            }
        }
    };

    // ✅ NOUVEAU : Redirection vers le Backend Spring Security
    const handleGoogleLogin = () => {
        // Cette URL déclenche le flow OAuth2 côté serveur
        // Assure-toi que le port 8080 est bien celui de ton backend
        const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';
        window.location.href = `${backendUrl}/oauth2/authorization/google`;
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-50 via-gray-50 to-slate-100 flex items-center justify-center p-6 relative overflow-hidden dark:from-slate-950 dark:via-slate-900 dark:to-slate-950">
            <Card className="w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800">
                <CardHeader className="text-center pt-10">
                    <div className="mx-auto w-20 h-20 bg-gradient-to-br from-indigo-500 to-purple-600 rounded-3xl mb-4 flex items-center justify-center shadow-lg shadow-indigo-200 dark:shadow-none">
                        <Shield size={40} className="text-white"/>
                    </div>
                    <CardTitle className="text-3xl font-bold text-indigo-600 mb-1">
                        Mon App
                    </CardTitle>
                    <CardDescription className="text-gray-500 dark:text-gray-400 font-medium">
                        Bienvenue ! Connectez-vous pour continuer
                    </CardDescription>
                </CardHeader>

                <CardContent className="p-8 space-y-6">
                    <div className="space-y-5">
                        <div className="space-y-1.5">
                            <Label className="text-gray-600 dark:text-gray-300 font-medium pl-1">Email</Label>
                            <Input
                                icon={Mail}
                                className="h-12 bg-slate-50 dark:bg-slate-950 border-transparent focus:bg-white dark:focus:bg-slate-900 focus:border-indigo-200 rounded-xl pl-11 text-gray-600 dark:text-white shadow-sm"
                                placeholder="votre@email.com"
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>

                        <div className="space-y-1.5">
                            <Label className="text-gray-600 dark:text-gray-300 font-medium pl-1">Mot de passe</Label>
                            <div className="relative">
                                <Input
                                    icon={Lock}
                                    className="h-12 bg-slate-50 dark:bg-slate-950 border-transparent focus:bg-white dark:focus:bg-slate-900 focus:border-indigo-200 rounded-xl pl-11 pr-10 text-gray-600 dark:text-white shadow-sm"
                                    placeholder="••••••••"
                                    type={showPassword ? "text" : "password"}
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowPassword(!showPassword)}
                                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200"
                                >
                                    {showPassword ? <EyeOff size={20}/> : <Eye size={20}/>}
                                </button>
                            </div>
                        </div>

                        <div className="flex items-center justify-between pt-1">
                            <div className="flex items-center space-x-2">
                                <Checkbox id="remember" className="border-gray-300 data-[state=checked]:bg-indigo-500" />
                                <Label htmlFor="remember" className="text-sm text-gray-500 dark:text-gray-400 cursor-pointer">Se souvenir de moi</Label>
                            </div>
                            <Link to="/auth/forgot-password" className="text-sm text-indigo-500 hover:text-indigo-600 font-semibold">
                                Mot de passe oublié ?
                            </Link>
                        </div>

                        <Button
                            className="w-full h-14 text-base font-bold rounded-2xl bg-gradient-to-r from-indigo-500 to-purple-500 hover:opacity-90 shadow-md shadow-indigo-100 dark:shadow-none text-white"
                            onClick={handleLogin}>
                            Se connecter <ChevronRight className="ml-2 h-5 w-5"/>
                        </Button>
                    </div>

                    <div className="relative py-2">
                        <div className="absolute inset-0 flex items-center"><Separator className="bg-gray-100 dark:bg-slate-800" /></div>
                        <div className="relative flex justify-center text-xs uppercase"><span className="bg-white dark:bg-slate-900 px-4 text-gray-400 font-medium">ou</span></div>
                    </div>

                    <div className="flex gap-4">
                        {/* ✅ Modification ICI : Appel de handleGoogleLogin */}
                        <Button variant="outline" className="flex-1 h-12 rounded-xl border-gray-100 dark:border-slate-800 bg-white dark:bg-slate-950 font-semibold shadow-sm" onClick={handleGoogleLogin}>
                            <GoogleIcon className="mr-2 h-5 w-5"/> Google
                        </Button>
                        <Button variant="outline" className="flex-1 h-12 rounded-xl border-gray-100 dark:border-slate-800 bg-white dark:bg-slate-950 font-semibold shadow-sm">
                            <FacebookIcon className="mr-2 h-5 w-5"/> Facebook
                        </Button>
                    </div>

                    <p className="text-center text-sm text-gray-500 dark:text-gray-400 mt-6 font-medium">
                        Pas encore de compte ? <Link to="/register" className="text-indigo-500 font-bold hover:underline ml-1">Créer un compte</Link>
                    </p>
                </CardContent>
            </Card>
        </div>
    );
}