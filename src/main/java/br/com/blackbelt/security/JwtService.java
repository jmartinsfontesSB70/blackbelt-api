package br.com.blackbelt.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.security.core.Authentication;

@Service
public class JwtService {

    @Value("${blackbelt.jwt.secret}")
    private String secret;

    @Value("${blackbelt.jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String gerarToken(Authentication authentication) {

        Date agora = new Date();

        Date expiracao = new Date(
                agora.getTime() + expiration
        );

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(
                        "authorities",
                        authentication.getAuthorities()
                                .stream()
                                .map(authority -> authority.getAuthority())
                                .toList()
                )
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getSigningKey())
                .compact();
    }

}