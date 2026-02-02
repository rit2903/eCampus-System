package com.ecampus.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.util.Map;

public class RoleAwareSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler {

    private final Map<String, String> defaultSuccessUrls;

    public RoleAwareSuccessHandler(Map<String, String> defaultSuccessUrls) {
        this.defaultSuccessUrls = defaultSuccessUrls;
    }

    @Override
    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        // If there was a saved request → use it
        String saved = super.determineTargetUrl(request, response);
        if (saved != null && !saved.equals("/") && !saved.isEmpty()) {
            return saved;
        }

        // Otherwise, resolve based on role
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority(); // e.g. ROLE1
            if (defaultSuccessUrls.containsKey(role)) {
                return defaultSuccessUrls.get(role);
            }
        }

        // Final fallback (safe default)
        return "/";
    }
}
