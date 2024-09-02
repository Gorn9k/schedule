package by.vstu.schedule;

import by.vstu.schedule.security.AuthorizeHttpRequestInterceptor;
import by.vstu.schedule.security.BasicAuthorizationFilter;
import by.vstu.schedule.services.AuthorizationService;
import by.vstu.schedule.services.RestClientAuthorizationService;
import by.vstu.schedule.services.RestClientScheduleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.util.Map;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class ScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class, args);
    }

    @Bean
    public RestClientScheduleService restClientScheduleService(
            @Value("${service.schedule.uri}") String scheduleServiceBaseUri,
            @Value("${service.authorization.admin.username}") String username, @Value("${service.authorization.admin.password}") String password,
            @Value("${clientUsername}") String clientUsername, @Value("${clientPassword}") String clientPassword,
            @Value("#{${classroomNumbers}}") Map<String, String[]> frame_classroomNumbers_Map,
            AuthorizationService authorizationService) {
        return new RestClientScheduleService(RestClient.builder()
                .baseUrl(scheduleServiceBaseUri)
                .requestInterceptor(new AuthorizeHttpRequestInterceptor(authorizationService, username, password, clientUsername, clientPassword))
                .build(), frame_classroomNumbers_Map);
    }

    @Bean
    public RestClientAuthorizationService restClientAuthorizationService(
            @Value("${service.authorization.uri}") String authorizationServiceBaseUri) {
        return new RestClientAuthorizationService(RestClient.builder()
                .baseUrl(authorizationServiceBaseUri)
                .build());
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public BasicAuthorizationFilter basicAuthorizationFilter(@Value("${spring.security.user.name}") final String username, @Value("${spring.security.user.password}") final String password) {
        return new BasicAuthorizationFilter(username, password);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   HiddenHttpMethodFilter hiddenHttpMethodFilter,
                                                   BasicAuthorizationFilter basicAuthorizationFilter
    ) throws Exception {
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST).authenticated()
                        .requestMatchers(HttpMethod.PUT).authenticated()
                        .requestMatchers(HttpMethod.DELETE).authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(basicAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(hiddenHttpMethodFilter, BasicAuthorizationFilter.class)
                .build();
    }
}
