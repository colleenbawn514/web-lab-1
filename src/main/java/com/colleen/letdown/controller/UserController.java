package com.colleen.letdown.controller;

import com.colleen.letdown.dao.UserDAO;
import com.colleen.letdown.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@Validated
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @GetMapping("/principal")
    public Principal getPrincipal(Principal principal){
        return principal;
    }

    @ResponseBody
    @GetMapping("/")
    public List<User> index(){
        List<User> users = StreamSupport
                .stream(userDAO.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return users;
    }
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            System.out.println("Create Failed");
        }
        if(userDAO.findByLogin(user.getLogin())!=null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userDAO.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
