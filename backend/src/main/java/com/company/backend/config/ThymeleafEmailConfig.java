package com.company.backend.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

/**
 * Configuration Thymeleaf dédiée aux templates d'emails.
 * <p>
 * Les templates sont situés dans /resources/templates/email/.
 * Cette configuration est séparée de Thymeleaf web car l'application
 * est une API REST et n'utilise pas de vues HTML.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Configuration
public class ThymeleafEmailConfig {

    private final ApplicationContext applicationContext;

    /**
     * Constructeur avec injection du contexte applicatif.
     *
     * @param applicationContext le contexte Spring
     */
    public ThymeleafEmailConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Crée le résolveur de templates pour les emails.
     *
     * @return le résolveur de templates configuré
     */
    @Bean
    public SpringResourceTemplateResolver emailTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("classpath:/templates/email/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCacheable(true);
        resolver.setOrder(1);
        resolver.setCheckExistence(true);
        return resolver;
    }

    /**
     * Crée le moteur de templates pour les emails.
     *
     * @param emailTemplateResolver le résolveur de templates
     * @return le moteur de templates configuré
     */
    @Bean(name = "emailTemplateEngine")
    public SpringTemplateEngine emailTemplateEngine(SpringResourceTemplateResolver emailTemplateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(emailTemplateResolver);
        engine.setEnableSpringELCompiler(true);
        return engine;
    }
}