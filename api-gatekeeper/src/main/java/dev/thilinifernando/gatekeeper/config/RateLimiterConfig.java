//package dev.thilinifernando.gatekeeper.config;
//
//import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
//import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import reactor.core.publisher.Mono;
//
//@Configuration
//public class RateLimiterConfig {
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("user-service-route", r -> r
//                        .path("/users/**")
//                        .filters(f -> f
//                                .stripPrefix(1)
//                                .prefixPath("/api/v1/users")
//                        )
//                        .uri("http://localhost:8082"))
//                .route("posts_route", r -> r
//                        .path("/posts/**")
//                        .filters(f -> f.requestRateLimiter(c -> {
//                            c.setRateLimiter(redisRateLimiter());
//                            c.setKeyResolver(userKeyResolver());
//                        }))
//                        .uri("https://jsonplaceholder.typicode.com"))
//                .build();
//    }
//
//    @Bean
//    public RedisRateLimiter redisRateLimiter() {
//        return new RedisRateLimiter(1, 1);
//    }
//
//    @Bean
//    @Primary
//    public KeyResolver userKeyResolver() {
//        // This identifies the user by their Host Name (IP)
//        System.out.println("In the user key resolver");
//        return exchange -> {
//            System.out.println("GATEWAY DEBUG: Checking rate limit for: " + exchange.getRequest().getPath());
//            // Try to get IP, fallback to "anonymous" so Redis ALWAYS gets a key
//            String ip = exchange.getRequest().getRemoteAddress() != null
//                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
//                    : "anonymous";
//            return Mono.just(ip);
//        };
//    }
//
//    @Bean
//    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
//        return new ReactiveRedisTemplate<>(factory, RedisSerializationContext.string());
//    }
//}
