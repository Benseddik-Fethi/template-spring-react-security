import { useTranslation } from 'react-i18next';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { useLanguage } from '@/hooks/useLanguage';

/**
 * Language switcher component with flags
 * Allows users to switch between French and English
 */
export function LanguageSwitcher() {
  const { t } = useTranslation('common');
  const { currentLanguage, changeLanguage, isLoading } = useLanguage();

  const languages = [
    { code: 'fr', label: t('language.french'), flag: '🇫🇷' },
    { code: 'en', label: t('language.english'), flag: '🇬🇧' },
  ];

  const currentLang = languages.find((l) => l.code === currentLanguage) || languages[0];

  return (
    <Select
      value={currentLanguage}
      onValueChange={changeLanguage}
      disabled={isLoading}
    >
      <SelectTrigger className="w-[140px]" aria-label={t('language.selectLanguage')}>
        <SelectValue>
          <span className="flex items-center gap-2">
            <span>{currentLang.flag}</span>
            <span>{currentLang.label}</span>
          </span>
        </SelectValue>
      </SelectTrigger>
      <SelectContent>
        {languages.map((language) => (
          <SelectItem key={language.code} value={language.code}>
            <span className="flex items-center gap-2">
              <span>{language.flag}</span>
              <span>{language.label}</span>
            </span>
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
}
