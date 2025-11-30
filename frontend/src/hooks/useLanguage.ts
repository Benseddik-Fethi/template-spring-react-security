import { useState, useEffect, useCallback, useRef } from 'react';
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

  // Get current language from i18n - normalize to 2-letter code
  const currentLanguage = i18n.language?.substring(0, 2) || 'fr';

  // Track previous language for rollback on error
  const previousLanguageRef = useRef(currentLanguage);

  // Check if user is authenticated
  const isAuthenticated = !!user;

  // Keep track if we've already synced for this user to prevent loops
  const hasSyncedRef = useRef(false);

  // Reset sync flag when user changes
  useEffect(() => {
    hasSyncedRef.current = false;
  }, [user?.id]);

  // Sync language from user preferences when authenticated
  useEffect(() => {
    if (isAuthenticated && user?.preferredLanguage && !hasSyncedRef.current) {
      const userLang = user.preferredLanguage;
      const currentLang = i18n.language?.substring(0, 2) || 'fr';
      if (userLang !== currentLang) {
        hasSyncedRef.current = true;
        i18n.changeLanguage(userLang);
      }
    }
  }, [isAuthenticated, user?.preferredLanguage, user?.id, i18n]);

  /**
   * Change language and sync with backend if authenticated
   */
  const changeLanguage = useCallback(
    async (language: string) => {
      const currentLang = i18n.language?.substring(0, 2) || 'fr';
      if (language === currentLang) return;

      // Store the original language before attempting change
      const originalLanguage = currentLang;
      previousLanguageRef.current = originalLanguage;

      setIsLoading(true);
      try {
        // Change language in i18next immediately for responsive UI
        await i18n.changeLanguage(language);

        // Sync with backend if authenticated
        if (isAuthenticated) {
          await userService.updateLanguage(language);
        }
      } catch (error) {
        console.error('Failed to change language:', error);
        // Revert to the original language on error
        await i18n.changeLanguage(originalLanguage);
      } finally {
        setIsLoading(false);
      }
    },
    [i18n, isAuthenticated]
  );

  /**
   * Fetch language preference from backend (for initial sync)
   */
  const fetchLanguage = useCallback(async () => {
    if (!isAuthenticated) return;

    try {
      const { language } = await userService.getLanguage();
      const currentLang = i18n.language?.substring(0, 2) || 'fr';
      if (language && language !== currentLang) {
        await i18n.changeLanguage(language);
      }
    } catch (error) {
      console.error('Failed to fetch language preference:', error);
    }
  }, [isAuthenticated, i18n]);

  return {
    currentLanguage,
    changeLanguage,
    fetchLanguage,
    isLoading,
  };
}
