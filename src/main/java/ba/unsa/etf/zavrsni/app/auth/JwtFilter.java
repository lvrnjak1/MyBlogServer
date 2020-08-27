package ba.unsa.etf.zavrsni.app.auth;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final JwtProcessor jwtProcessor;


    public JwtFilter(JwtProvider jwtProvider, AuthService authService, JwtProcessor jwtProcessor) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;
        this.jwtProcessor = jwtProcessor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(httpServletRequest.getHeader("Authorization"));
        var auth = jwtProcessor.process(token);
        if (auth != null) {
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String extractToken(String header) {
        return (StringUtils.hasText(header) && header.startsWith("Bearer ")) ? header.substring(7) : null;
    }
}