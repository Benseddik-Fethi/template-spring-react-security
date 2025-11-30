/**
 * Error message display component
 */

import { AlertCircle } from 'lucide-react';
import { cn } from '@/lib/utils';

interface ErrorMessageProps {
  /** Error message to display */
  message: string;
  /** Additional CSS classes */
  className?: string;
  /** Variant style */
  variant?: 'inline' | 'block';
}

/**
 * Reusable error message component
 */
export function ErrorMessage({ message, className, variant = 'inline' }: ErrorMessageProps) {
  if (variant === 'block') {
    return (
      <div className={cn('p-4 bg-red-50 dark:bg-red-900/20 rounded-xl border border-red-200 dark:border-red-800', className)}>
        <div className="flex items-center gap-2">
          <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0" />
          <p className="text-sm text-red-600 dark:text-red-400 font-medium">{message}</p>
        </div>
      </div>
    );
  }

  return (
    <p className={cn('text-sm text-red-500 text-center', className)}>{message}</p>
  );
}
