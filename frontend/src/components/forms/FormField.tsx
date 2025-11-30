/**
 * Form field wrapper component with label and error display
 */

import type { ReactNode } from 'react';
import { Label } from '@/components/ui/label';
import { cn } from '@/lib/utils';

interface FormFieldProps {
  /** Field label */
  label: string;
  /** Error message */
  error?: string;
  /** Child input component */
  children: ReactNode;
  /** Additional CSS classes */
  className?: string;
  /** HTML for attribute for label */
  htmlFor?: string;
  /** Whether field is required */
  required?: boolean;
}

/**
 * Wrapper component for form fields with consistent label and error styling
 */
export function FormField({
  label,
  error,
  children,
  className,
  htmlFor,
  required,
}: FormFieldProps) {
  return (
    <div className={cn('space-y-1.5', className)}>
      <Label
        htmlFor={htmlFor}
        className="text-gray-600 dark:text-gray-300 font-medium pl-1"
      >
        {label}
        {required && <span className="text-red-500 ml-1">*</span>}
      </Label>
      {children}
      {error && <p className="text-sm text-red-500 pl-1 mt-1">{error}</p>}
    </div>
  );
}
