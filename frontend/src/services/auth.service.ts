/**
 * Authentication service for handling auth-related API calls
 */

import { api, setAccessToken } from '@/lib/api';
import { API_ENDPOINTS } from '@/config/api.config';
import type {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  ForgotPasswordRequest,
  ResetPasswordRequest,
  VerifyEmailRequest,
  ResendVerificationRequest,
  ResetPasswordValidationResponse,
} from '@/types/auth.types';
import type { User } from '@/types/user.types';

/**
 * Authenticates a user with email and password
 * @param credentials - The login credentials
 * @returns The login response containing user and optional access token
 */
export const login = async (credentials: LoginRequest): Promise<LoginResponse> => {
  const { data } = await api.post<LoginResponse>(API_ENDPOINTS.AUTH.LOGIN, credentials);
  if (data.accessToken) {
    setAccessToken(data.accessToken);
  }
  return data;
};

/**
 * Logs out the current user
 */
export const logout = async (): Promise<void> => {
  await api.post(API_ENDPOINTS.AUTH.LOGOUT);
  setAccessToken(null);
};

/**
 * Registers a new user
 * @param data - The registration data
 */
export const register = async (data: RegisterRequest): Promise<void> => {
  await api.post(API_ENDPOINTS.AUTH.REGISTER, data);
};

/**
 * Gets the current authenticated user
 * @returns The current user
 */
export const me = async (): Promise<User> => {
  const { data } = await api.get<User>(API_ENDPOINTS.AUTH.ME);
  return data;
};

/**
 * Verifies user email with token
 * Note: Token is passed as URL param to match backend API design
 * @param token - The verification token
 */
export const verifyEmail = async ({ token }: VerifyEmailRequest): Promise<void> => {
  await api.post(`${API_ENDPOINTS.USERS.VERIFY_EMAIL}?token=${encodeURIComponent(token)}`);
};

/**
 * Resends verification email
 * @param data - The email to resend verification to
 */
export const resendVerification = async (data: ResendVerificationRequest): Promise<void> => {
  await api.post(API_ENDPOINTS.USERS.RESEND_VERIFICATION, data);
};

/**
 * Initiates forgot password flow
 * @param data - The email to send reset link to
 */
export const forgotPassword = async (data: ForgotPasswordRequest): Promise<void> => {
  await api.post(API_ENDPOINTS.USERS.FORGOT_PASSWORD, data);
};

/**
 * Validates a password reset token
 * Note: Token is passed as URL param to match backend API design
 * @param token - The reset token to validate
 * @returns Whether the token is valid
 */
export const validateResetToken = async (token: string): Promise<ResetPasswordValidationResponse> => {
  const { data } = await api.get<ResetPasswordValidationResponse>(
    `${API_ENDPOINTS.USERS.RESET_PASSWORD_VALIDATE}?token=${encodeURIComponent(token)}`
  );
  return data;
};

/**
 * Resets user password with token
 * @param data - The reset token and new password
 */
export const resetPassword = async (data: ResetPasswordRequest): Promise<void> => {
  await api.post(API_ENDPOINTS.USERS.RESET_PASSWORD, data);
};
