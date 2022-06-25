package com.springboot.museum.security;

import com.springboot.museum.exception.AppException;
import com.springboot.museum.exception.BadRequestException;
import com.springboot.museum.payload.ApiResponse;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;


    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        String token = Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        return token;
    }

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            throw new AppException("Invalid JWT signature");
        }catch (MalformedJwtException ex){
            throw new AppException("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            throw new AppException("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            throw new AppException("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            throw new AppException("JWt claims string is empty.");
        }
    }
}
