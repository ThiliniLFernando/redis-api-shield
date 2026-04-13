package dev.thilinifernando.adjustmentservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
public class AdjustmentController {

    @GetMapping("/")
    public String getAllUsers() {
        return "Get all users";
    }

    @GetMapping("/{userId}")
    public String getUser(@PathVariable("userId") String userId) {
        return "User " + userId;
    }
}
