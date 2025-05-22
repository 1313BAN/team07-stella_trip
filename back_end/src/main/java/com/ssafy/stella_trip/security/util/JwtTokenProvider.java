package com.ssafy.stella_trip.security.util;

import com.ssafy.stella_trip.user.dto.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenProvider {

    private final RedisTemplate<String, Object> redisTemplate;

    private final String secret;
    private final int accessExpirationTime;
    private final int refreshExpirationTime;
    private final SecretKey key;
    private static final String BLACKLIST_PREFIX = "token-blacklist:";

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token.expiration}") int accessExpirationTime,
            @Value("${jwt.refresh-token.expiration}") int refreshExpirationTime,
            RedisTemplate<String, Object> redisTemplate
    ) throws NoSuchAlgorithmException {
        this.secret = secret;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        // 해싱으로 32바이트 고정
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
        this.key = Keys.hmacShaKeyFor(hashBytes);
        this.redisTemplate = redisTemplate;
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
                .verifyWith(key)
                .build();
    }

    /**
     * access token 만드는 메서드
     * @param email 이메일
     * @param role 역할
     * @return jwt string 반환
     */
    public String generateAccessToken(int userId, String email, UserRole role) {
        return Jwts.builder()
                .subject(email)
                .claim("roles", List.of(role.name())) // 하나지만 리스트로 담는 이유: Spring Security가 Collection 기반이기 때문
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpirationTime * 1000L)) // 1시간 후 만료
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * refresh token 만드는 메서드
     * @param email 이메일
     * @return jwt string 반환
     */
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationTime * 1000L)) // 1시간 후 만료
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * token에서 email 뽑기
     * @param token token string
     * @return 이메일
     */
    public String getEmailFromToken(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * token에서 user role 뽑기
     * @param token token string
     * @return UserRole
     */
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.get("roles", List.class);
    }

    public int getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.get("userId", Integer.class);
    }

    /**
     * 토큰에서 만료 일자 가져오기
     * @param token 토큰
     * @return 만료 일자
     */
    public Date getExpirationDateFromToken(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    /**
     * session 유효 검증 (시간 및 parsing 가능한지)
     * @param token 토큰
     * @return 유효하면 true
     */
    public boolean validateToken(String token) {
        try {
            // Redis에 블랙리스트로 등록된 토큰인지 확인
            if (isTokenBlacklisted(token)) {
                return false;
            }
            getJwtParser().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * 토큰이 블랙리스트에 등록되어 있는지 확인
     * @param token 검증할 토큰
     * @return 블랙리스트에 있으면 true
     */
    private boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }

    /**
     * 토큰을 블랙리스트에 추가 (로그아웃 등에서 사용)
     * @param token 블랙리스트에 추가할 토큰
     */
    public void addToBlacklist(String token) {
        try {
            if(!validateToken(token)){
                return;
            }

            // 토큰의 남은 유효 시간 계산
            Date expiration = getExpirationDateFromToken(token);
            long ttl = expiration.getTime() - System.currentTimeMillis();

            // 만료 시간이 양수일 경우에만 Redis에 저장
            if (ttl > 0) {
                redisTemplate.opsForValue().set(
                        BLACKLIST_PREFIX + token,
                        "blacklisted",
                        ttl,
                        TimeUnit.MILLISECONDS
                );
            }
        } catch (JwtException e) {
            // 이미 만료된 토큰이거나 유효하지 않은 토큰인 경우 무시
        }
    }
}
