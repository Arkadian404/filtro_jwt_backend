package com.ark.security.config;



import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    //private final LogoutService logoutHandler;
    //private final CookiesHandler cookiesHandler;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public CorsConfiguration corsConfiguration(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:4200");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> corsConfiguration()))
                .authorizeHttpRequests(ahr-> ahr.requestMatchers("/api/v1/auth/**").permitAll()

//                        .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGEMENT.name())
//                        .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.getPermission(), MANAGEMENT_READ.getPermission())
//                        .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.getPermission(), MANAGEMENT_CREATE.getPermission())
//                        .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.getPermission(), MANAGEMENT_UPDATE.getPermission())
//                        .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.getPermission(), MANAGEMENT_DELETE.getPermission())

//                        .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
//                        .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.getPermission())
//                        .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.getPermission())
//                        .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.getPermission())
//                        .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.getPermission())

                        .anyRequest().authenticated())
                .sessionManagement(sm-> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(eh-> eh.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                //.addFilterBefore(cookiesHandler, UsernamePasswordAuthenticationFilter.class)
//                .logout(l -> l.logoutUrl("/api/v1/auth/logout")
//                                .addLogoutHandler(logoutHandler)
//                        .logoutSuccessHandler((rq, rs, a) -> SecurityContextHolder.clearContext())
//                        );
        return http.build();
    }
}
