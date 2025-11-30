/**
 * Authentication types for login, register, and password management
 */

import type { User } from './user.types';

/**
 * Request payload for user login
 */
export interface LoginRequest {
  email: string;
  password: string;
}

/**
 * Response from login endpoint
 */
export interface LoginResponse {
  user: User;
  accessToken?: string;
}

/**
 * Request payload for user registration
 */
export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

/**
 * Request payload for forgot password
 */
export interface ForgotPasswordRequest {
  email: string;
}

/**
 * Request payload for resetting password
 */
export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}

/**
 * Request payload for email verification
 */
export interface VerifyEmailRequest {
  token: string;
}

/**
 * Request payload for resending verification email
 */
export interface ResendVerificationRequest {
  email: string;
}

/**
 * Response from reset password validation
 */
export interface ResetPasswordValidationResponse {
  valid: boolean;
}
