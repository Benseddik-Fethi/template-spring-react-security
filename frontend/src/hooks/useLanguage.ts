import { useState, useEffect, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '@/context/AuthContext';
import { userService } from '@/services';

/**
 * Hook for managing language preferences with API synchronization
 * Handles both i18next language and backend user preferences
 */
export function useLanguage() {
  const { i18n } = useTranslation();
  const { user } = useAuth();
  const [isLoading, setIsLoading] = useState(false);

  // Get current language from i18n
  const currentLanguage = i18n.language?.substring(0, 2) || 'fr';

  // Check if user is authenticated
  const isAuthenticated = !!user;

  // Sync language from user preferences when authenticated
  useEffect(() => {
    if (isAuthenticated && user?.preferredLanguage) {
      const userLang = user.preferredLanguage;
      if (userLang !== currentLanguage) {
        i18n.changeLanguage(userLang);
      }
    }
  }, [isAuthenticated, user?.preferredLanguage, currentLanguage, i18n]);

  /**
   * Change language and sync with backend if authenticated
   */
  const changeLanguage = useCallback(
    async (language: string) => {
      if (language === currentLanguage) return;

      setIsLoading(true);
      try {
        // Change language in i18next immediately
        await i18n.changeLanguage(language);

        // Sync with backend if authenticated
        if (isAuthenticated) {
          await userService.updateLanguage(language);
        }
      } catch (error) {
        console.error('Failed to change language:', error);
        // Revert to previous language on error
        await i18n.changeLanguage(currentLanguage);
      } finally {
        setIsLoading(false);
      }
    },
    [currentLanguage, i18n, isAuthenticated]
  );

  /**
   * Fetch language preference from backend (for initial sync)
   */
  const fetchLanguage = useCallback(async () => {
    if (!isAuthenticated) return;

    try {
      const { language } = await userService.getLanguage();
      if (language && language !== currentLanguage) {
        await i18n.changeLanguage(language);
      }
    } catch (error) {
      console.error('Failed to fetch language preference:', error);
    }
  }, [isAuthenticated, currentLanguage, i18n]);

  return {
    currentLanguage,
    changeLanguage,
    fetchLanguage,
    isLoading,
  };
}
