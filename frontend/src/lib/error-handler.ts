/**
 * Centralized error handler for API errors
 */

import { isAxiosError } from 'axios';
import type { ApiError } from '@/types/api.types';

/**
 * Default error messages for common HTTP status codes
 */
const DEFAULT_ERROR_MESSAGES: Record<number, string> = {
  400: 'Requête invalide',
  401: 'Non autorisé. Veuillez vous connecter.',
  403: 'Accès interdit',
  404: 'Ressource non trouvée',
  409: 'Conflit - Cette ressource existe déjà',
  422: 'Données invalides',
  429: 'Trop de requêtes. Veuillez réessayer plus tard.',
  500: 'Erreur serveur. Veuillez réessayer plus tard.',
  502: 'Service temporairement indisponible',
  503: 'Service indisponible',
};

/**
 * Extracts a user-friendly error message from an error object
 * @param error - The error object to handle
 * @returns A user-friendly error message
 */
export const handleApiError = (error: unknown): string => {
  if (isAxiosError(error)) {
    const status = error.response?.status;
    const serverMessage = error.response?.data?.message;

    // Use server message if available
    if (serverMessage && typeof serverMessage === 'string') {
      return serverMessage;
    }

    // Fall back to default messages based on status code
    if (status && DEFAULT_ERROR_MESSAGES[status]) {
      return DEFAULT_ERROR_MESSAGES[status];
    }

    // Network error
    if (error.code === 'ERR_NETWORK') {
      return 'Erreur de connexion. Vérifiez votre connexion internet.';
    }

    // Timeout error
    if (error.code === 'ECONNABORTED') {
      return 'La requête a expiré. Veuillez réessayer.';
    }
  }

  // Generic error message
  return 'Une erreur est survenue. Veuillez réessayer.';
};

/**
 * Creates an ApiError object from an error
 * @param error - The error to convert
 * @returns An ApiError object
 */
export const toApiError = (error: unknown): ApiError => {
  if (isAxiosError(error)) {
    return {
      message: handleApiError(error),
      status: error.response?.status ?? 500,
      code: error.code,
      timestamp: new Date().toISOString(),
    };
  }

  return {
    message: handleApiError(error),
    status: 500,
    timestamp: new Date().toISOString(),
  };
};
