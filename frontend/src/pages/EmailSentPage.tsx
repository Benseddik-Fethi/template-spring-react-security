import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Mail, ArrowRight } from "lucide-react";
import { Link } from "react-router-dom";

export default function EmailSentPage() {
    return (
        <div className="min-h-screen bg-gray-50 dark:bg-slate-950 flex items-center justify-center p-4">
            <Card className="w-full max-w-md text-center">
                <CardHeader>
                    <div className="mx-auto w-16 h-16 bg-rose-100 dark:bg-rose-900/30 rounded-full flex items-center justify-center mb-4">
                        <Mail className="w-8 h-8 text-rose-500" />
                    </div>
                    <CardTitle className="text-2xl">Vérifiez vos emails</CardTitle>
                </CardHeader>
                <CardContent className="space-y-6">
                    <p className="text-gray-500 dark:text-gray-400">
                        Nous avons envoyé un lien de confirmation à votre adresse email.
                        Veuillez cliquer dessus pour activer votre compte.
                    </p>
                    <div className="flex flex-col gap-3">
                        <Button asChild className="w-full">
                            <Link to="/login">
                                Retour à la connexion <ArrowRight className="ml-2 w-4 h-4" />
                            </Link>
                        </Button>
                        <Button variant="ghost" asChild>
                            <Link to="/auth/resend-verification">Je n'ai rien reçu</Link>
                        </Button>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}