package ba.unsa.etf.zavrsni.app.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtProcessor {
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    public JwtProcessor(JwtProvider jwtProvider, AuthService authService) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;
    }


    public UsernamePasswordAuthenticationToken process(String token) {
        try {
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null && jwtProvider.isValid(token)) {
                String username = jwtProvider.extractUsername(token);
                UserDetails user = authService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                return auth;
            }
        } catch (Exception ignored) {
            System.out.println("Invalid token!");
        }
        return null;
    }
}
