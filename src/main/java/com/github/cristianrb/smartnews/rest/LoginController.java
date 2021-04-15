package com.github.cristianrb.smartnews.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("login")
public class LoginController {

    @PostMapping
    public void authenticate(Principal principal) {
        System.out.println("Authenticated: " + principal);
    }


}
