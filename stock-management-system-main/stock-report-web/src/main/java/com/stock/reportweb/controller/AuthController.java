package com.stock.reportweb.controller;

import com.stock.reportweb.dao.DB;
import com.stock.reportweb.dao.UserDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserDAO userDAO = new UserDAO();

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        try {
            if (userDAO.login(username, password)) {
                DB.clearTempDb();
                session.setAttribute("loggedInUser", username);
                return "redirect:/";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/login?error=true";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password) {
        try {
            if (userDAO.register(username, password)) {
                return "redirect:/login?registered=true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/register?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        DB.clearTempDb();
        session.invalidate();
        return "redirect:/login";
    }
}