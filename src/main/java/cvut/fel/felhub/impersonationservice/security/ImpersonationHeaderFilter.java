package cvut.fel.felhub.impersonationservice.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ImpersonationHeaderFilter implements Filter {

    private static final String IMPERSONATION_HEADER_NAME = "Impersonation";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if(SecurityUrlsHolder.OAUTH_PROTECTED_URLS.matches(req)) {
            checkImpersonationHeaders(req, res, chain);  //check there is no impersonation header
        } else {
            chain.doFilter(request, response); //continue with the request by passing it to next-level filter
        }
    }

    private void checkImpersonationHeaders(HttpServletRequest request, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        if(request.getHeader(IMPERSONATION_HEADER_NAME) != null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);  //prevent the request from proceeding
        } else {
            chain.doFilter(request, res); //move on with the request
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //do nothing
    }
}
