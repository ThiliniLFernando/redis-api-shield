package dev.thilinifernando.gatekeeper.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service-route", r -> r
                        .path("/users/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .prefixPath("/api/v1/users")
                        )
                        .uri("http://localhost:8082")).build();
    }
}
