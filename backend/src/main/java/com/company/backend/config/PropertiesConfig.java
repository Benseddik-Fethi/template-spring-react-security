package com.company.backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration pour activer les classes de propriétés.
 * <p>
 * Active les classes annotées avec @ConfigurationProperties pour
 * permettre l'injection des propriétés depuis application.yml.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Configuration
@EnableConfigurationProperties({
        JwtProperties.class,
        SecurityProperties.class
})
public class PropertiesConfig {
}