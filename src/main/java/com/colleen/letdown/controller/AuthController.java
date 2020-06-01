package com.colleen.letdown.controller;

import com.colleen.letdown.dao.TokenDAO;
import com.colleen.letdown.dao.UserDAO;
import com.colleen.letdown.entities.Token;
import com.colleen.letdown.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.UUID;

@Controller
@Validated
public class AuthController {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TokenDAO tokenDAO;

    @ResponseBody
    @PostMapping("/auth")
    public String getToken(@RequestParam String login, @RequestParam String password){
        User user = userDAO.findByLogin(login);
        if(user==null || !user.getPassword().equals(password)) {
            throw new BadCredentialsException("Incorrect login or password");
        }
        Token token = new Token();
        token.setExpirationDate(new Timestamp(System.currentTimeMillis()+60*1000));
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        tokenDAO.save(token);

        return token.getToken();
    }
    @ResponseBody
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String token){
        Token token1 = tokenDAO.findByToken(token);
        if(token1!=null) {
            tokenDAO.delete(token1);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
