package com.alxt.pfmservice.utils;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class UserUtils {

    public static  String getUserId(JwtAuthenticationToken auth) {
        return auth.getToken().getSubject();
    }
}
