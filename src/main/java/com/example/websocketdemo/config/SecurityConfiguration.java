package com.example.websocketdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
//@ComponentScan("com.example.websocketdemo")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//    @Autowired
//    SecurityConfiguration() {
//    }

    // Main thing. WL he just used XML format to write all these things
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
//                .and()
//                .headers()
//                .frameOptions().sameOrigin()
                .disable()
                .authorizeRequests()
                .antMatchers(
                        "/secured/**/**",
                        "/secured/success",
                        "/secured/socket").permitAll()
                .anyRequest().permitAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Enable HTTPS
//        http.requiresChannel().anyRequest().requiresSecure();
    }
}
