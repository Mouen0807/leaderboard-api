package com.example.demo.config;

import com.example.demo.services.JwtService;
import com.example.demo.services.RedisCacheService;

import com.example.demo.services.RoleService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;
    private final RedisCacheService redisCacheService;
    private final RoleService roleService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        jwt = authHeader.substring(7);

        String tokenId = jwtService.extractId(jwt);
            
        // checking in redis cache for token revokation
        if(redisCacheService.isTokenRevoked(tokenId)){
            filterChain.doFilter(request, response);
            return;
        }

        String userId = jwtService.extractSubject(jwt);

        if (userId!= null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<UserDetails> optUserDetails = Optional.ofNullable(this.myUserDetailsService.loadUserByUsername(userId));

            if(optUserDetails.isEmpty()){
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtService.isTokenValid(jwt, optUserDetails.get())) {
                List<String> permissions = jwtService.extractPermissions(jwt);
                Collection<GrantedAuthority> userAuthorities =  permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        userAuthorities
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // authenticate customer in spring boot
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
