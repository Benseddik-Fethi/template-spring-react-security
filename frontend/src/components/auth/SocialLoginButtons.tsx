/**
 * Social login buttons component (Google, Facebook)
 */

import { Button } from '@/components/ui/button';
import { FacebookIcon, GoogleIcon } from '@/components/ui/Icons';

interface SocialLoginButtonsProps {
  /** Whether the buttons are disabled */
  disabled?: boolean;
}

/**
 * Gets the backend URL for OAuth redirects
 */
const getBackendUrl = () => import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';

/**
 * Handles Google OAuth login redirect
 */
const handleGoogleLogin = () => {
  window.location.href = `${getBackendUrl()}/oauth2/authorization/google`;
};

/**
 * Handles Facebook OAuth login redirect
 */
const handleFacebookLogin = () => {
  window.location.href = `${getBackendUrl()}/oauth2/authorization/facebook`;
};

/**
 * Social login buttons for OAuth providers
 */
export function SocialLoginButtons({ disabled }: SocialLoginButtonsProps) {
  return (
    <div className="flex gap-4">
      <Button
        type="button"
        variant="outline"
        className="flex-1 h-12 rounded-xl border-gray-100 dark:border-slate-800 bg-white dark:bg-slate-950 font-semibold shadow-sm"
        onClick={handleGoogleLogin}
        disabled={disabled}
      >
        <GoogleIcon className="mr-2 h-5 w-5" /> Google
      </Button>
      <Button
        type="button"
        variant="outline"
        className="flex-1 h-12 rounded-xl border-gray-100 dark:border-slate-800 bg-white dark:bg-slate-950 font-semibold shadow-sm"
        onClick={handleFacebookLogin}
        disabled={disabled}
      >
        <FacebookIcon className="mr-2 h-5 w-5" /> Facebook
      </Button>
    </div>
  );
}
