package com.lamp.lantern.plugins.core.authentication;

import com.lamp.lantern.plugins.api.config.AuthenticationConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author laohu
 */
public class LanternAuthFlow {

    private AuthenticationConfig authenticationConfig;


    public String getToken(HttpServletRequest request) {
        if (Objects.equals(authenticationConfig.getTokenSpot(), "header")) {
            return request.getHeader(authenticationConfig.getTokenName());
        }
        if (Objects.equals(authenticationConfig.getTokenSpot(), "cookie")) {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(authenticationConfig.getTokenName())) {
                    return cookie.getValue();
                }
            }
            return null;
        }
        throw new RuntimeException("Wrong token location configuration");
    }

    public String getResource(ServletRequest request) {
        return null;
    }

    public void failed(ServletResponse response) {

    }

    public boolean notAuthentication(String resource) {
        return false;
    }

    public boolean userAuthentication(String resource) {
        return false;
    }
}
