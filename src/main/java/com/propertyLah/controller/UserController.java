package com.propertyLah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.propertyLah.model.Property;
import com.propertyLah.model.User;
import com.propertyLah.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @GetMapping("/user/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/user/registration")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        logger.info("Attempting to register user with email: " + user.getEmail());
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult.rejectValue("email", "error.user", "An account already exists for this email.");
            return "registration";
        }
        user.setProperties(new ArrayList<>());
        userService.saveUser(user);
        return "redirect:/user/login";
    }

    @GetMapping("/user/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/user/profile")
    public String showProfilePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        logger.info("Attempting to load profile for user with email: " + user.getEmail());
        model.addAttribute("user", user);
        return "profile";
    }
    
    @PostMapping("/user/changePassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        // Get the authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            model.addAttribute("error", "You are not logged in.");
            return "login";
        }

        User user = userService.findUserByEmail(auth.getName());
        if (user == null) {
            model.addAttribute("error", "Could not find user with email: " + auth.getName());
            return "login";
        }

        // Check if current password is correct
        if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Current password is incorrect.");
            model.addAttribute("user", user);
            return "profile";
        }

        // Check if new password and confirmation match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password and confirmation do not match.");
            model.addAttribute("user", user);
            return "profile";
        }

        // Change the password and save the user
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userService.updateUser(user);

        model.addAttribute("message", "Password changed successfully.");
        model.addAttribute("user", user);  
        return "profile";
    }

    @GetMapping("/user/properties")
    public String showUserProperties(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());  
        List<Property> properties = user.getProperties(); 
        model.addAttribute("properties", properties);
        return "user_properties"; 
    }
}




