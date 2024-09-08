package com.root.controller;

import com.root.resource.UserResource;
import com.root.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResource> addUser(@RequestBody UserResource userResource) {
        return ResponseEntity.ok(userService.addUser(userResource));
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResource> getUser(@PathVariable("id") @Min(value = 1, message = "User Id Cannot be -ve") Long id){
        return ResponseEntity.ok(userService.getUser(id));
    }
}
