/**
 * Authentication card wrapper component
 */

import type { ReactNode, ElementType } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { cn } from '@/lib/utils';
import { LanguageSwitcher } from '@/components/LanguageSwitcher';

interface AuthCardProps {
  /** Icon component to display */
  icon: ElementType;
  /** Card title */
  title: string;
  /** Card description */
  description: string;
  /** Card content */
  children: ReactNode;
  /** Icon background gradient */
  iconGradient?: string;
  /** Title color class */
  titleColor?: string;
  /** Additional CSS classes for card */
  className?: string;
  /** Background gradient for page */
  backgroundGradient?: string;
  /** Shadow color class for icon container */
  iconShadowColor?: string;
}

/**
 * Reusable authentication card wrapper for auth pages
 */
export function AuthCard({
  icon: Icon,
  title,
  description,
  children,
  iconGradient = 'from-indigo-500 to-purple-600',
  titleColor = 'text-indigo-600',
  className,
  backgroundGradient = 'from-slate-50 via-gray-50 to-slate-100',
  iconShadowColor = 'shadow-indigo-200',
}: AuthCardProps) {
  return (
    <div
      className={cn(
        'min-h-screen flex items-center justify-center p-6 relative overflow-hidden',
        `bg-gradient-to-br ${backgroundGradient}`,
        'dark:from-slate-950 dark:via-slate-900 dark:to-slate-950'
      )}
    >
      {/* Language switcher - top right position */}
      <div className="absolute top-4 right-4 z-20">
        <LanguageSwitcher />
      </div>
      
      <Card
        className={cn(
          'w-full max-w-md relative z-10 border-white/50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm shadow-xl dark:border-slate-800',
          className
        )}
      >
        <CardHeader className="text-center pt-10">
          <div
            className={cn(
              'mx-auto w-20 h-20 rounded-3xl mb-4 flex items-center justify-center shadow-lg dark:shadow-none',
              `bg-gradient-to-br ${iconGradient}`,
              iconShadowColor
            )}
          >
            <Icon size={40} className="text-white" />
          </div>
          <CardTitle className={cn('text-3xl font-bold mb-1', titleColor)}>
            {title}
          </CardTitle>
          <CardDescription className="text-gray-500 dark:text-gray-400 font-medium">
            {description}
          </CardDescription>
        </CardHeader>

        <CardContent className="p-8 space-y-6">{children}</CardContent>
      </Card>
    </div>
  );
}
