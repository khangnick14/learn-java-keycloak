package com.nick.ms_nick_keycloak.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    @GetMapping("/nick")
    public ResponseEntity<String> getNick() {
        return ResponseEntity.ok("This endpoint is public accessed");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.ok("This endpoint is only accessed by ADMIN");
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("This endpoint is only accessed by USER");
    }

}

