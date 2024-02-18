package com.marketinginapp.startup.ms.user.security.jwt.impl;

import com.marketinginapp.startup.ms.user.exception.UnauthorizedException;
import com.marketinginapp.startup.ms.user.security.details.CustomUserDetail;
import com.marketinginapp.startup.ms.user.security.details.CustomUserDetailService;
import com.marketinginapp.startup.ms.user.security.jwt.JwtConfig;
import com.marketinginapp.startup.ms.user.security.jwt.JwtService;
import com.marketinginapp.startup.ms.user.utils.Constant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor

@Service
public class JwtServiceImpl extends JwtConfig implements JwtService {

    private final CustomUserDetailService userDetailService;

    @Override
    public Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Key getKey() {
        byte[] keys = Decoders.BASE64.decode(getSecret());
        return Keys.hmacShaKeyFor(keys);
    }

    @Override
    public String generateToken(CustomUserDetail customUserDetail) {
        List<String> roles = new ArrayList<>();

        customUserDetail.getAuthorities().forEach(role -> {
            roles.add(role.getAuthority());
        });

        Instant currentTime = Instant.now();
        return Jwts.builder()
                .setSubject(customUserDetail.getUsername())
                .claim("authorities", customUserDetail.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .claim("roles", roles)
                .claim("isEnable", customUserDetail.isEnabled())
                .setIssuedAt(Date.from(currentTime))
                .setExpiration(Date.from(currentTime.plusSeconds(getExpiration())))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String refreshToken(CustomUserDetail customUserDetail) {
        Instant currentTime = Instant.now();
        return Jwts.builder()
                .setSubject(customUserDetail.getUsername())
                .setIssuedAt(Date.from(currentTime))
                .setExpiration(Date.from(currentTime.plusSeconds(getExpiration())))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isValidToken(String token) {
        final String username = extractUsername(token);
        // TODO: reviser
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        return userDetails != null;
    }

    private String extractUsername(String token) {
        return extractClaimsTFunction(token, Claims::getSubject);
    }
    private <T> T extractClaimsTFunction(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException exception) {
            log.error(exception.getLocalizedMessage());
            throw new UnauthorizedException(String.format(Constant.EXCEPTION_TOKEN_EXPIRATION, exception.getMessage()));
        }catch (UnsupportedJwtException exception){
            log.error(exception.getLocalizedMessage());
            throw new UnauthorizedException(String.format(Constant.EXCEPTION_TOKEN_NOT_SUPPORTED, exception.getMessage()));
        }catch (MalformedJwtException | SignatureException exception) {
            log.error(exception.getLocalizedMessage());
            throw new UnauthorizedException(String.format(Constant.EXCEPTION_TOKEN_INVALID_FORMAT, exception.getMessage()));
        } catch (Exception exception) {
            log.error(exception.getLocalizedMessage());
            throw new UnauthorizedException(String.format(Constant.EXCEPTION_TOKEN_UNAUTHORIZED, exception.getMessage()));
        }
    }
}