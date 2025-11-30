/**
 * Centralized export for all services
 */

import * as authServiceModule from './auth.service';
import * as userServiceModule from './user.service';

export const authService = authServiceModule;
export const userService = userServiceModule;

// Also export individual functions for direct imports
export * from './auth.service';
export * from './user.service';
