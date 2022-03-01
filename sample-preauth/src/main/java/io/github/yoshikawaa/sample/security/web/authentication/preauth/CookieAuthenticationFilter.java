package io.github.yoshikawaa.sample.security.web.authentication.preauth;

import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.Assert;

import lombok.Setter;

public class CookieAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Setter
    private String cookieName;

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        // return null to reject.
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        // return empty to not reject.
        return "";
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(cookieName, "cookieName is required.");
    }
}
