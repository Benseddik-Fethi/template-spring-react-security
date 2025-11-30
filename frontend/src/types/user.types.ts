/**
 * User types for the application
 */

/**
 * User entity representing an authenticated user
 */
export interface User {
  id: string;
  email: string;
  firstName?: string | null;
  lastName?: string | null;
  role: "USER" | "ADMIN";
  avatar?: string;
  emailVerified: boolean;
  preferredLanguage?: string;
}

/**
 * Request payload for updating user profile
 */
export interface UpdateProfileRequest {
  firstName?: string;
  lastName?: string;
  avatar?: string;
}

/**
 * Request payload for updating user password
 */
export interface UpdatePasswordRequest {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}
