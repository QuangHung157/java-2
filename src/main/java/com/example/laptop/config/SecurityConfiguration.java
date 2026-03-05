package com.example.laptop.config;

import jakarta.servlet.DispatcherType;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // ✅ Spring Session Remember-me service (auto remember theo ý bạn)
        @Bean
        public SpringSessionRememberMeServices rememberMeServices() {
                SpringSessionRememberMeServices services = new SpringSessionRememberMeServices();
                services.setAlwaysRemember(true);
                return services;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                // CSRF vẫn bật cho toàn bộ web

                                .csrf(csrf -> csrf.ignoringRequestMatchers(
                                                "/api/chat",
                                                "/payment/vnpay/ipn"))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/payment/vnpay/return", "/payment/vnpay/ipn")
                                                .permitAll()

                                )

                                .authorizeHttpRequests(auth -> auth
                                                // forward/include cho JSP
                                                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE)
                                                .permitAll()

                                                // static resources
                                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                                .permitAll()
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/client/**")
                                                .permitAll()

                                                // public pages
                                                .requestMatchers(
                                                                "/", "/products", "/product/**",
                                                                "/login", "/register",
                                                                "/access-denied", "/error")
                                                .permitAll()

                                                // chatbot API (public)
                                                .requestMatchers("/api/chat").permitAll()

                                                // admin
                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                                // user flows cần login
                                                .requestMatchers(
                                                                "/add-product-to-cart/**",
                                                                "/cart/**",
                                                                "/checkout",
                                                                "/place-order",
                                                                "/thanks")
                                                .authenticated()

                                                .anyRequest().authenticated())

                                // session management
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                                .invalidSessionUrl("/login?expired")
                                                .sessionFixation(fix -> fix.migrateSession())
                                                .maximumSessions(1)
                                                .maxSessionsPreventsLogin(false))

                                // remember-me
                                .rememberMe(r -> r.rememberMeServices(rememberMeServices()))

                                // login
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .successHandler(customSuccessHandler())
                                                .failureUrl("/login?error=true")
                                                .permitAll())

                                // logout
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .deleteCookies("JSESSIONID", "SESSION")
                                                .permitAll())

                                .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));

                return http.build();
        }

        @Bean
        public AuthenticationSuccessHandler customSuccessHandler() {
                return new CustomSuccessHandler();
        }
}
