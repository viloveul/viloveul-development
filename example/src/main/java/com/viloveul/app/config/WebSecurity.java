package com.viloveul.app.config;

import com.viloveul.context.auth.dto.DetailAuthentication;
import com.viloveul.context.request.filter.AdvancedRequestFilter;
import com.viloveul.context.request.filter.TokenRequestFilter;
import com.viloveul.context.util.encryption.Tokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration(
    proxyBeanMethods = false
)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity {

    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Environment environment, Tokenizer tokenizer) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/authentication/**").permitAll()
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore(
                    new TokenRequestFilter(environment, token -> {
                        DetailAuthentication authentication = tokenizer.parse(
                            token, DetailAuthentication.class, Tokenizer.USE_JWT
                        );
                        authentication.initialize();
                        return authentication;
                    }),
                    UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                    new AdvancedRequestFilter(),
                    TokenRequestFilter.class
                )
            .formLogin().disable()
            ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        if (this.passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }
        return this.passwordEncoder;
    }
}
