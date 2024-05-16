package com.collabera.security;


import com.collabera.model.AuthUser;
import com.collabera.utils.Constants;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.collabera.utils.StringUtils.nonNull;

@Component
public class TokenProvider extends AbstractTokenProvider {

    public String generateToken(UserDetails userDetails) {
        if (nonNull(userDetails)) {
            claims.put(Constants.CLAIM_KEY_USERNAME, userDetails.getUsername());
            final Date createdDate = new Date();
            claims.put(Constants.CLAIM_KEY_CREATED, createdDate);
            return doGenerateToken(claims, accessTokenExpiration);
        }
        return null;
    }

    public String generateRefreshToken(UserDetails userDetails) {
        if (nonNull(userDetails)) {
            claims.put(Constants.CLAIM_KEY_USERNAME, userDetails.getUsername());
            final Date createdDate = new Date();
            claims.put(Constants.CLAIM_KEY_CREATED, createdDate);
            return doGenerateToken(claims, refreshTokenExpiration);
        }
        return null;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        if (nonNull(userDetails) && nonNull(token)) {
            AuthUser user = (AuthUser) userDetails;
            final String username = getUsernameFromToken(token);
            final Date created = getCreatedDateFromToken(token);
            return (username.equals(user.getUsername()) && !isTokenExpired(token));
        }
        return null;
    }
}