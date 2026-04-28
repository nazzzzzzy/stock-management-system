package com.stock.reportweb.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home(HttpSession session) {
        Object user = session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        return "index";
    }
}