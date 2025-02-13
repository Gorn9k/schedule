package by.vstu.schedule.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class BasicAuthorizationFilter extends OncePerRequestFilter {

    private final OrRequestMatcher requestMatcher = new OrRequestMatcher(
            new AntPathRequestMatcher("/**", "POST"),
            new AntPathRequestMatcher("/**", "DELETE"),
            new AntPathRequestMatcher("/**", "PUT")
    );

    private final String username;

    private final String password;

    public BasicAuthorizationFilter(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Basic ") || isNotValidCredentials(authHeader)) {
                if (authHeader != null && isNotValidCredentials(authHeader)) {
                    response.setStatus(403);
                    String jsonResponse = "{\"incorrectLoginOrPasswordError\": \"Неверно введены логин или пароль\"}";
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(jsonResponse);
                } else {
                    response.setStatus(401);
                    String jsonResponse = "Отсутствует токен доступа";
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(jsonResponse);
                }
                return;
            }
            User user = new User(username, password, List.of());
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isNotValidCredentials(String authHeader) {
        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedBytes);

        String[] parts = credentials.split(":", 2);
        String username = parts[0];
        String password = parts[1];
        return !this.username.equals(username) || !this.password.equals(password);
    }
}
