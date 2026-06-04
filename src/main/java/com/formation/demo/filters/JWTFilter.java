package com.formation.demo.filters;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.formation.demo.services.JWTUtils;
import com.formation.demo.services.UserDetailsServiceCustom;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final UserDetailsServiceCustom userDetailsCustom;
    private final JWTUtils jUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // "Bearer " = 7 chars — on valide le format complet avant substring
        if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
            String token = authHeader.substring(7).trim();
            if (!token.isEmpty()) {
                try {
                    String username = jUtils.extractUsername(token);
                    if (username != null
                            && SecurityContextHolder.getContext().getAuthentication() == null) {
                        try {
                            UserDetails userDetails = userDetailsCustom.loadUserByUsername(username);
                            if (jUtils.validateToken(token, userDetails)) {
                                UsernamePasswordAuthenticationToken authToken =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails, null, userDetails.getAuthorities());
                                authToken.setDetails(
                                        new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authToken);
                            }
                        } catch (Exception e) {
                            // Utilisateur introuvable ou token invalide — on laisse passer anonymement
                        }
                    }
                } catch (Exception e) {
                    // Token malformé ou expiré — on laisse passer sans authentification
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
