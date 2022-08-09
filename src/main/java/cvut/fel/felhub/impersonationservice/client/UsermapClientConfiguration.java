package cvut.fel.felhub.impersonationservice.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UsermapClientConfiguration {

    @Bean
    RequestInterceptor authorizationTokenForwarder() {
        return new UsermapFeignAuthorizationTokenForwarder();
    }

    public static class UsermapFeignAuthorizationTokenForwarder implements RequestInterceptor {
        private static final String AUTHORIZATION_HEADER = "Authorization";

        @Value("${camunda-usermap-token}")
        private String camundaToken;

        @Override
        public void apply(RequestTemplate template) {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                template.header(AUTHORIZATION_HEADER, "Bearer " + camundaToken);
            }
        }
    }
}
