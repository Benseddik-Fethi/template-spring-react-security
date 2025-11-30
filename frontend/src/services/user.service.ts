/**
 * User service for handling user-related API calls
 */

import { api } from '@/lib/api';
import type { User, UpdateProfileRequest, UpdatePasswordRequest } from '@/types/user.types';

/**
 * Gets the current user's profile
 * @returns The user profile
 */
export const getProfile = async (): Promise<User> => {
  const { data } = await api.get<User>('/users/profile');
  return data;
};

/**
 * Updates the current user's profile
 * @param data - The profile data to update
 * @returns The updated user profile
 */
export const updateProfile = async (data: UpdateProfileRequest): Promise<User> => {
  const { data: responseData } = await api.put<User>('/users/profile', data);
  return responseData;
};

/**
 * Updates the current user's password
 * @param data - The password change data
 */
export const updatePassword = async (data: UpdatePasswordRequest): Promise<void> => {
  await api.put('/users/password', data);
};
