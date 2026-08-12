package com.example.myOwnRealtorWebsite.config;

import com.example.myOwnRealtorWebsite.repository.userRepository;
import com.example.myOwnRealtorWebsite.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final CustomUserDetailsService customUserDetailsService;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable for simple API testing
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/properties/**").permitAll() // Public access

                    .requestMatchers("/",
                    "/error",
                    "/services",
                    "/profile",
                    "/api/agent/profile",
                    "/index",
                    "/reviews",
                    "/login",
                    "/css/**",
                    "/images/**",
                    "/fonts/**",
                    "/webjars/**",
                    "/listings/",
                    "/contact/**",
                    "/review-form/**",
                    "/review/**",
                    "/listings/**",
                    "/forgot-password",
                    "/reset-password",
                    "/register").permitAll()
                    .requestMatchers("/api/user/**").hasRole("ADMIN")  // Private access
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true) //force redirects here after login
                    .permitAll()
                )
               // .httpBasic(withDefaults()); // Use a simple login popup in browser
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }



    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsService() {
        UserDetails admin = User.withUsername(adminUsername)
                .password(passwordEncoder().encode(adminPassword))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }


    @Bean
    public DaoAuthenticationProvider dbAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider inMemoryAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(inMemoryUserDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(dbAuthProvider(), inMemoryAuthProvider()));
    }

}
