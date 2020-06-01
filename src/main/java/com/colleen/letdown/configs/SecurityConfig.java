package com.colleen.letdown.configs;

import com.colleen.letdown.auth.CustomAuthenticationProvider;
import com.colleen.letdown.dao.TokenDAO;
import com.colleen.letdown.entities.Token;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Autowired
    private TokenDAO tokenDAO;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/api/create");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        PreAuthTokenHeaderFilter filter = new PreAuthTokenHeaderFilter("api-token");

        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            Token token = tokenDAO.findByToken(principal);
            System.out.println(principal);
            if (token == null )
            {
                System.out.println(principal+"was not found");
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }
            if (token.getExpirationDate().compareTo(new Timestamp(System.currentTimeMillis()))<0)
            {
                System.out.println(principal+"was expiration");
                tokenDAO.delete(token);
                throw new BadCredentialsException("The API key was expiration.");
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        http.antMatcher("/api/**")
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .addFilter(filter)
                .authorizeRequests()
                .anyRequest()
                .authenticated();
        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write(new JSONObject()
                            .put("timestamp", LocalDateTime.now())
                            .put("message", "Access denied")
                            .put("description", e.getMessage())
                            .toString());
                });
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(authProvider);
    }
}