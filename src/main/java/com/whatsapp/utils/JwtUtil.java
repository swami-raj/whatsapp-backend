package com.whatsapp.utils;



import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.User;
import com.whatsapp.entity.UserToken;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.repository.ServiceAccessor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final String SECRET_KEY =
            "dzlbyqzefsnf3run8vtyi1wx94g80fn5qol5yly2hyaf6jkqad7gqouxmvl9n6oh5ua8wf5eoy8cubzobsbxghdiclmwpx1rn0aecc2d1eom4aeahs8ijttukm7irca0";

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetailResponse userDetailResponse) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("UserAuthDetails", userDetailResponse);
        claims.put("roleId", userDetailResponse.getRoleId());
          claims.put("companyId", userDetailResponse.getCompanyId());
        return generateToken(claims, userDetailResponse);
    }

    public boolean isTokenValid(String token, UserDetailResponse userDetailResponse) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetailResponse.getEmail())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(
            Map<String, Object> extraClaims, UserDetailResponse userDetailResponse) {
        return "Bearer "
                + Jwts.builder()
                        .setClaims(extraClaims)
                        .setSubject(userDetailResponse.getEmail())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .signWith(getSigningKey())
                        .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean validateToken(String token) {
        final String username = extractUsername(token);
        Optional<User> user =
                RepositoryAccessor.getUserRepository().findByEmailAndIsActive(username, true);
        return user.isPresent();
    }

    public UserDetailResponse getUserAuthDetailsFromToken(String token) {
        Claims claims = extractAllClaims(token);

        UserDetailResponse userDetailResponse =
                ServiceAccessor.getModelMapper()
                        .map(claims.get("UserAuthDetails"), UserDetailResponse.class);
        userDetailResponse.setRoleId(claims.get("roleId", Long.class));
        //    userDetailResponse.setCompanyId(claims.get("companyId", Long.class));
        return userDetailResponse;
    }

    public static boolean isValidToken(ResponseDto<?> response) {
        try {
            // Get authentication from Security Context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (!(authentication instanceof JwtAuthenticationToken)) {
                response.setCode(HttpStatus.UNAUTHORIZED.value());
                response.setMessage("Invalid authentication.");
                return false;
            }

            // Extract token from JwtAuthenticationToken
            String token = ((JwtAuthenticationToken) authentication).getToken().getTokenValue();
            System.out.println("Extracted Token: " + token);

            // Ensure token does not contain "Bearer " prefix
            String tokenWithoutBearer = token.replaceFirst("^Bearer ", "");

            // Check token in database
            Optional<UserToken> userTokenOptional =
                    RepositoryAccessor.getUserTokenRepository()
                            .findByTokenAndIsActive(tokenWithoutBearer, true);

            if (userTokenOptional.isEmpty()) {
                response.setCode(HttpStatus.UNAUTHORIZED.value());
                response.setMessage("Invalid or expired token.");
                return false;
            }

            return true;
        } catch (Exception e) {
            response.setCode(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("Token validation failed.");
            return false;
        }
    }
}
