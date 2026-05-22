package com.example.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class InternalAuthGatewayFilterFactory
        extends AbstractGatewayFilterFactory<InternalAuthGatewayFilterFactory.Config> {

    public static final String INTERNAL_AUTH_HEADER = "X-Gateway-Auth";
    private static final String ISSUER = "gateway";
    private static final String SUBJECT = "internal";

    private final SecretKey signingKey;
    private final long ttlSeconds;

    public InternalAuthGatewayFilterFactory(
            @Value("${gateway.internal.jwt.secret}") String secret,
            @Value("${gateway.internal.jwt.ttl-seconds:60}") long ttlSeconds) {
        super(Config.class);
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ttlSeconds = ttlSeconds;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String audience = config.getAudience() != null
                    ? config.getAudience()
                    : exchange.getRequest().getURI().getHost();

            String jwt = signJwt(audience);

            ServerHttpRequest mutated = exchange.getRequest().mutate()
                    .headers(h -> {
                        h.remove(INTERNAL_AUTH_HEADER);
                        h.set(INTERNAL_AUTH_HEADER, "Bearer " + jwt);
                    })
                    .build();

            return chain.filter(exchange.mutate().request(mutated).build());
        };
    }

    private String signJwt(String audience) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .issuer(ISSUER)
                .subject(SUBJECT)
                .audience().add(audience).and()
                .issuedAt(new Date(now))
                .expiration(new Date(now + ttlSeconds * 1000))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public static class Config {
        private String audience;

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }
    }
}
