package by.vstu.schedule.services;

import by.vstu.schedule.models.DTO.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@RequiredArgsConstructor
public class RestClientAuthorizationService implements AuthorizationService {

    private final RestClient restClient;

    @Cacheable(value = "TokenCache", key = "#username")
    @Override
    public Token authorize(String username, String password, String clientUsername, String clientPassword) {
        return this.restClient
                .post()
                .uri("/token?grant_type=password")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization",
                        "Basic ".concat(Base64.getEncoder().encodeToString(clientUsername.concat(":").concat(clientPassword).getBytes())))
                .body(new LinkedMultiValueMap<String, Object>() {{
                    add("username", username);
                    add("password", password);
                }})
                .retrieve()
                .body(Token.class);
    }
}
