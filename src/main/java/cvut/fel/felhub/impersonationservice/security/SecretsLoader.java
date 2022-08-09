package cvut.fel.felhub.impersonationservice.security;

import cvut.fel.felhub.common.security.AbstractDockerPropertiesLoader;
import cvut.fel.felhub.impersonationservice.util.CredentialsParserUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

public class SecretsLoader extends AbstractDockerPropertiesLoader {

    @Override
    protected void loadProperties(ConfigurableEnvironment environment, SpringApplication application) {
        Properties properties = new Properties();

        final String camundaUsermapToken = loadDockerSecret("czm-camundafel-usermapapi-camunda-token");
        properties.put("camunda-usermap-token", camundaUsermapToken);

        final String mongoUri = loadDockerSecret("hub-mongo-impersonation_service_database-uri");
        properties.put("spring.data.mongodb.uri", mongoUri);

        final String adminsCredentials = loadDockerSecret("hub-impersonation_service-admins_credentials");
        var parsedCredentials = CredentialsParserUtil.parseCredentials(adminsCredentials);
        properties.put("admin-credentials", parsedCredentials);

        environment.getPropertySources().addFirst(new PropertiesPropertySource("impersonation-service-secrets", properties));
    }
}
