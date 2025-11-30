/**
 * Validation schemas using Zod
 */

import { z } from 'zod';

/**
 * Email validation schema
 */
export const emailSchema = z
  .string()
  .min(1, 'Email requis')
  .email('Email invalide');

/**
 * Password validation schema matching backend requirements
 * - 12 to 128 characters
 * - At least one lowercase letter
 * - At least one uppercase letter
 * - At least one digit
 * - At least one special character
 */
export const passwordSchema = z
  .string()
  .min(12, 'Le mot de passe doit contenir au moins 12 caractères')
  .max(128, 'Le mot de passe ne doit pas dépasser 128 caractères')
  .regex(/[a-z]/, 'Le mot de passe doit contenir au moins une minuscule')
  .regex(/[A-Z]/, 'Le mot de passe doit contenir au moins une majuscule')
  .regex(/\d/, 'Le mot de passe doit contenir au moins un chiffre')
  .regex(
    /[@$!%*?&#^()_+\-=[\]{};':"\\|,.<>/`~]/,
    'Le mot de passe doit contenir au moins un caractère spécial'
  );

/**
 * Login form validation schema
 */
export const loginSchema = z.object({
  email: emailSchema,
  password: z.string().min(1, 'Mot de passe requis'),
});

export type LoginFormData = z.infer<typeof loginSchema>;

/**
 * Register form validation schema
 */
export const registerSchema = z
  .object({
    firstName: z.string().min(1, 'Prénom requis'),
    lastName: z.string().min(1, 'Nom requis'),
    email: emailSchema,
    password: passwordSchema,
    confirmPassword: z.string().min(1, 'Confirmation du mot de passe requise'),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: 'Les mots de passe ne correspondent pas',
    path: ['confirmPassword'],
  });

export type RegisterFormData = z.infer<typeof registerSchema>;

/**
 * Forgot password form validation schema
 */
export const forgotPasswordSchema = z.object({
  email: emailSchema,
});

export type ForgotPasswordFormData = z.infer<typeof forgotPasswordSchema>;

/**
 * Reset password form validation schema
 */
export const resetPasswordSchema = z
  .object({
    password: passwordSchema,
    confirmPassword: z.string().min(1, 'Confirmation du mot de passe requise'),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: 'Les mots de passe ne correspondent pas',
    path: ['confirmPassword'],
  });

export type ResetPasswordFormData = z.infer<typeof resetPasswordSchema>;

/**
 * Password validation rules for UI display
 */
export const passwordRules = [
  { id: 'length', label: '12 à 128 caractères', test: (p: string) => p.length >= 12 && p.length <= 128 },
  { id: 'lowercase', label: 'Au moins une minuscule', test: (p: string) => /[a-z]/.test(p) },
  { id: 'uppercase', label: 'Au moins une majuscule', test: (p: string) => /[A-Z]/.test(p) },
  { id: 'digit', label: 'Au moins un chiffre', test: (p: string) => /\d/.test(p) },
  {
    id: 'special',
    label: "Au moins un caractère spécial (@$!%*?&#^()_+-=[]{};':\"\\|,.<>/`~)",
    test: (p: string) => /[@$!%*?&#^()_+\-=[\]{};':"\\|,.<>/`~]/.test(p),
  },
] as const;
