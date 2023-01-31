package com.github.mdeluise.pinboard.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class JwtWebUtil {
    private final JwtTokenUtil jwtTokenUtil;
    private final String jwtCookieName;
    private final UserDetailsService userDetailsService;


    @Autowired
    public JwtWebUtil(JwtTokenUtil jwtTokenUtil, @Value("jwt.cookie.name") String jwtCookieName,
                      UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtCookieName = jwtCookieName;
        this.userDetailsService = userDetailsService;
    }


    public ResponseCookie generateJwtCookie(UserDetails userDetails) {
        String jwt = jwtTokenUtil.generateAccessToken(userDetails).token();
        return ResponseCookie.from(jwtCookieName, jwt)
                             .path("/api")
                             .maxAge(24 * 60 * 60)
                             .httpOnly(true)
                             .build();
    }


    public ResponseCookie generateCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName, null).path("/api").build();
    }


    public String getJwtTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }


    public String getAccessToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }


    public JwtTokenInfo refreshToken(String token) {
        if (!jwtTokenUtil.isValid(token)) {
            throw new JWTVerificationException("Provided token is not valid");
        }
        UserDetails user = userDetailsService.loadUserByUsername(jwtTokenUtil.getSubject(token));
        if (user == null) {
            throw new EntityNotFoundException("username", jwtTokenUtil.getSubject(token));
        }
        return jwtTokenUtil.generateAccessToken(user);
    }
}
