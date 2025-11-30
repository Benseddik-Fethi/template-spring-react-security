import { useTranslation } from "react-i18next";
import { useAuth } from "@/context/AuthContext";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { AlertTriangle, Key, Monitor, Pencil, Shield, Trash2, User } from "lucide-react";

export default function ProfilePage() {
    const { t } = useTranslation(['pages', 'common']);
    const { user } = useAuth();

    // Format date helper
    const formatDate = (date: string | undefined | null) => {
        if (!date) return t('pages:profile.notAvailable');
        return new Date(date).toLocaleDateString(undefined, {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    };

    return (
        <div className="container max-w-2xl py-8">
            <h1 className="text-2xl font-bold mb-6">{t('pages:profile.title')}</h1>

            {/* Personal Information Section */}
            <Card className="mb-6">
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <User className="h-5 w-5" />
                        {t('pages:profile.personalInfo')}
                    </CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="flex items-start gap-6">
                        {/* Avatar */}
                        <div className="flex-shrink-0">
                            <Avatar className="h-20 w-20">
                                <AvatarImage />
                                <AvatarFallback className="text-xl bg-gradient-to-br from-indigo-400 to-purple-400 text-white">
                                    {user?.firstName?.[0] || ''}{user?.lastName?.[0] || ''}
                                </AvatarFallback>
                            </Avatar>
                        </div>

                        {/* Info */}
                        <div className="space-y-3 flex-1">
                            <div>
                                <Label className="text-muted-foreground">{t('pages:profile.firstName')}</Label>
                                <p className="font-medium">{user?.firstName || '-'}</p>
                            </div>
                            <div>
                                <Label className="text-muted-foreground">{t('pages:profile.lastName')}</Label>
                                <p className="font-medium">{user?.lastName || '-'}</p>
                            </div>
                            <div>
                                <Label className="text-muted-foreground">{t('pages:profile.email')}</Label>
                                <p className="font-medium">{user?.email || '-'}</p>
                            </div>
                            <div>
                                <Label className="text-muted-foreground">{t('pages:profile.memberSince')}</Label>
                                <p className="font-medium">{formatDate(null)}</p>
                            </div>
                        </div>
                    </div>

                    <Button className="mt-6">
                        <Pencil className="h-4 w-4 mr-2" />
                        {t('pages:profile.editProfile')}
                    </Button>
                </CardContent>
            </Card>

            {/* Security Section */}
            <Card className="mb-6">
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Shield className="h-5 w-5" />
                        {t('pages:profile.security.title')}
                    </CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                    {/* Change Password */}
                    <div className="flex items-center justify-between p-4 rounded-lg border">
                        <div className="flex items-center gap-3">
                            <Key className="h-5 w-5 text-muted-foreground" />
                            <div>
                                <p className="font-medium">{t('pages:profile.security.changePassword')}</p>
                                <p className="text-sm text-muted-foreground">{t('pages:profile.security.changePasswordDescription')}</p>
                            </div>
                        </div>
                        <Button variant="outline">{t('common:actions.edit')}</Button>
                    </div>

                    {/* Active Sessions */}
                    <div className="flex items-center justify-between p-4 rounded-lg border">
                        <div className="flex items-center gap-3">
                            <Monitor className="h-5 w-5 text-muted-foreground" />
                            <div>
                                <p className="font-medium">{t('pages:profile.security.sessions')}</p>
                                <p className="text-sm text-muted-foreground">{t('pages:profile.security.sessionsDescription')}</p>
                            </div>
                        </div>
                        <Button variant="outline">{t('common:actions.view')}</Button>
                    </div>
                </CardContent>
            </Card>

            {/* Danger Zone */}
            <Card className="border-destructive/50">
                <CardHeader>
                    <CardTitle className="flex items-center gap-2 text-destructive">
                        <AlertTriangle className="h-5 w-5" />
                        {t('pages:profile.dangerZone.title')}
                    </CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="flex items-center justify-between p-4 rounded-lg border border-destructive/30 bg-destructive/5">
                        <div>
                            <p className="font-medium">{t('pages:profile.dangerZone.deleteAccount')}</p>
                            <p className="text-sm text-muted-foreground">{t('pages:profile.dangerZone.deleteWarning')}</p>
                        </div>
                        <Button variant="destructive">
                            <Trash2 className="h-4 w-4 mr-2" />
                            {t('common:actions.delete')}
                        </Button>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}
