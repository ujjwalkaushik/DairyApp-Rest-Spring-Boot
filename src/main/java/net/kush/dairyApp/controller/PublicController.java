package net.kush.dairyApp.controller;

import net.kush.dairyApp.entity.User;
import net.kush.dairyApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/create-user")
    public  void createUser(@RequestBody User user) {
        userService.saveEntry(user);
    }
}
