package cvut.fel.felhub.impersonationservice.security;

import cvut.fel.felhub.common.security.faketoken.AbstractFakeTokenSecurityConfig;
import cvut.fel.felhub.common.security.faketoken.FakeTokenAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@Profile({"local & !oauth", "dev & !oauth", "test & !oauth"})
@Configuration
public class FakeSecurityConfig extends AbstractFakeTokenSecurityConfig implements WebMvcConfigurer {
    public FakeSecurityConfig() {
        super(SecurityUrlsHolder.OAUTH_PROTECTED_URLS);
    }

    @Value("#{${admin-credentials}}")
    private Map<String, String> adminCredentials;

    @Override
    public void configure(HttpSecurity security) throws Exception {
        security
                .addFilterBefore(fakeTokenAuthFilter(), AnonymousAuthenticationFilter.class)
                .addFilterAfter(new ImpersonationHeaderFilter(), FakeTokenAuthFilter.class)
                .authorizeRequests()
                .requestMatchers(SecurityUrlsHolder.OAUTH_PROTECTED_URLS).authenticated()
                .requestMatchers(SecurityUrlsHolder.MANAGEMENT_API_URLS).hasRole("hub-management-api")
                .requestMatchers(SecurityUrlsHolder.PERMIT_ALL_URLS).permitAll()
                .anyRequest().denyAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api-docs").allowedOrigins("*").allowedHeaders("*").allowedMethods("GET");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();

        for (Map.Entry<String, String> adminCredential : adminCredentials.entrySet()) {
            if (adminCredential.getKey().isBlank() || adminCredential.getValue().isBlank()) {
                throw new IllegalArgumentException("Invalid format of management credentials");
            }

            UserDetails managementClientUserDetails = User.withUsername(adminCredential.getKey())
                    .password(passwordEncoder().encode(adminCredential.getValue()))
                    .roles("hub-management-api")
                    .build();

            detailsManager.createUser(managementClientUserDetails);
        }

        return detailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
