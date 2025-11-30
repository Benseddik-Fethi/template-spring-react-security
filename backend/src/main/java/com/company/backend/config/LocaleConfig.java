package com.company.backend.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * Configuration de l'internationalisation (i18n) de l'application.
 * <p>
 * Configure le résolveur de locale basé sur l'en-tête Accept-Language
 * et le source des messages pour les traductions.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Configuration
public class LocaleConfig {

    /**
     * Configure le résolveur de locale basé sur l'en-tête Accept-Language.
     * La locale par défaut est le français (fr).
     *
     * @return le résolveur de locale configuré
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.FRENCH);
        return resolver;
    }

    /**
     * Configure le source des messages pour l'internationalisation.
     * Les fichiers de messages sont situés dans le dossier i18n/messages.
     *
     * @return le source des messages configuré
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
