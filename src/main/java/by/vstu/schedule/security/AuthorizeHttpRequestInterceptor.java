package by.vstu.schedule.security;

import by.vstu.schedule.services.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthorizeHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final AuthorizationService authorizationService;

    private final String username;

    private final String password;

    private final String clientUsername;

    private final String clientPassword;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().setBearerAuth(authorizationService.authorize(username, password, clientUsername, clientPassword).access_token());
        return execution.execute(request, body);
    }
}
