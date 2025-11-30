import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

// Import translation files
import frCommon from './locales/fr/common.json';
import frAuth from './locales/fr/auth.json';
import frValidation from './locales/fr/validation.json';
import frErrors from './locales/fr/errors.json';
import frPages from './locales/fr/pages.json';

import enCommon from './locales/en/common.json';
import enAuth from './locales/en/auth.json';
import enValidation from './locales/en/validation.json';
import enErrors from './locales/en/errors.json';
import enPages from './locales/en/pages.json';

const resources = {
  fr: {
    common: frCommon,
    auth: frAuth,
    validation: frValidation,
    errors: frErrors,
    pages: frPages,
  },
  en: {
    common: enCommon,
    auth: enAuth,
    validation: enValidation,
    errors: enErrors,
    pages: enPages,
  },
};

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources,
    fallbackLng: 'fr',
    defaultNS: 'common',
    ns: ['common', 'auth', 'validation', 'errors', 'pages'],
    detection: {
      order: ['localStorage', 'navigator'],
      lookupLocalStorage: 'i18nextLng',
      caches: ['localStorage'],
    },
    interpolation: {
      escapeValue: false,
    },
  });

export default i18n;
