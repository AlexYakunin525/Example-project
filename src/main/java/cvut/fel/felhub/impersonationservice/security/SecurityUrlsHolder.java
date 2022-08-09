package cvut.fel.felhub.impersonationservice.security;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

abstract class SecurityUrlsHolder {

    private SecurityUrlsHolder() {
        //
    }

    static final RequestMatcher OAUTH_PROTECTED_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/impersonation/**")
    );

    static final RequestMatcher MANAGEMENT_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/management/**")
    );

    static final RequestMatcher PERMIT_ALL_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api-docs/**"),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/actuator/**")
    );
}
