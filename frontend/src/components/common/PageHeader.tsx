/**
 * Page header component for consistent page titles
 */

import { cn } from '@/lib/utils';

interface PageHeaderProps {
  /** Main title text */
  title: string;
  /** Optional description text */
  description?: string;
  /** Additional CSS classes */
  className?: string;
}

/**
 * Reusable page header component with title and optional description
 */
export function PageHeader({ title, description, className }: PageHeaderProps) {
  return (
    <div className={cn('mb-6', className)}>
      <h1 className="text-3xl font-bold text-gray-800 dark:text-white">{title}</h1>
      {description && (
        <p className="text-gray-500 dark:text-gray-400 mt-1">{description}</p>
      )}
    </div>
  );
}
