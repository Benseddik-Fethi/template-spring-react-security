/**
 * Generic API types for error handling and responses
 */

/**
 * Standard API error structure
 */
export interface ApiError {
  message: string;
  code?: string;
  status: number;
  timestamp?: string;
}

/**
 * Generic API response wrapper
 */
export interface ApiResponse<T> {
  data: T;
  message?: string;
}
