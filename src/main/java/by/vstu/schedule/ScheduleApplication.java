package by.vstu.schedule;

import by.vstu.schedule.security.BasicAuthorizationFilter;
import by.vstu.schedule.services.RestClientScheduleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
            @Value("${service.schedule.token}") String authToken,
            @Value("#{${classroomNumbers}}") Map<String, String[]> frame_classroomNumbers_Map) {
        return new RestClientScheduleService(RestClient.builder()
                .baseUrl(scheduleServiceBaseUri)
                .defaultHeader("Authorization", "Bearer " + authToken)
                .build(), frame_classroomNumbers_Map);
    }

    @Bean
    public BasicAuthorizationFilter basicAuthorizationFilter(@Value("${spring.security.user.name}") final String username, @Value("${spring.security.user.password}") final String password) {
        return new BasicAuthorizationFilter(username, password);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, BasicAuthorizationFilter basicAuthorizationFilter) throws Exception {
        return http
                .cors(cors -> corsConfigurer())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST).authenticated()
                        .requestMatchers(HttpMethod.PUT).authenticated()
                        .requestMatchers(HttpMethod.DELETE).authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(basicAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
}
