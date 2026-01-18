package com.example.laptop.config;

import jakarta.servlet.DispatcherType;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
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

        // ✅ Spring Session Remember-me service
        @Bean
        public SpringSessionRememberMeServices rememberMeServices() {
                SpringSessionRememberMeServices services = new SpringSessionRememberMeServices();
                services.setAlwaysRemember(true);
                return services;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                // ✅ CSRF bật (đúng production)
                                .csrf(Customizer.withDefaults())
                                // nếu bạn muốn test Postman/curl cho /api/chat thì mở dòng dưới:
                                // .csrf(csrf -> csrf.ignoringRequestMatchers("/api/chat"))

                                .authorizeHttpRequests(auth -> auth
                                                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE)
                                                .permitAll()
                                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                                .permitAll()
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/client/**")
                                                .permitAll()

                                                .requestMatchers("/", "/products", "/product/**",
                                                                "/login", "/register",
                                                                "/access-denied", "/error")
                                                .permitAll()

                                                // ✅ AI chatbot API: cho guest dùng
                                                .requestMatchers("/api/chat").permitAll()

                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                                .requestMatchers("/add-product-to-cart/**", "/cart/**",
                                                                "/checkout", "/place-order", "/thanks")
                                                .authenticated()

                                                .anyRequest().authenticated())

                                // ✅ Session management (gộp 1 block cho sạch)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                                .invalidSessionUrl("/login?expired")
                                                .sessionFixation(fix -> fix.migrateSession())
                                                .maximumSessions(1)
                                                .maxSessionsPreventsLogin(false))

                                .rememberMe(r -> r.rememberMeServices(rememberMeServices()))

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .successHandler(customSuccessHandler())
                                                .failureUrl("/login?error=true")
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())

                                .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));

                return http.build();
        }

        @Bean
        public AuthenticationSuccessHandler customSuccessHandler() {
                return new CustomSuccessHandler();
        }
}
