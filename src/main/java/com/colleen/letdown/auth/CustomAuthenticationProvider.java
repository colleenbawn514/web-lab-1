package com.colleen.letdown.auth;

import java.util.Collections;
import com.colleen.letdown.dao.UserDAO;
import com.colleen.letdown.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDAO userDAO;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();
        System.out.println("login = " + login + " password = " + password);
        User user = userDAO.findByLogin(login);
        if(user!=null && user.getPassword().equals(password)){
            return new UsernamePasswordAuthenticationToken(login, password, Collections.emptyList());
        } else {
            throw new BadCredentialsException("Incorrect login or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
