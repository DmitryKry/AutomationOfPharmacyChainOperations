package ru.IS_122.AutomationOfPharmacyChainOperations.controller;


import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.Pharmacy;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.UserOfPharmacy;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.PharmacyService;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/pharmacy")
@AllArgsConstructor
public class MainControl {
    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private UserService userService;


    @GetMapping("/admin")
    public List<Pharmacy> findAllPharmacies(){
        return pharmacyService.getAllPharmacies();
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "entrace";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String login,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        UserOfPharmacy user = userService.findOfUser(login, password);
        if (user != null) {
            model.addAttribute("user", user);
            return "userPlace";
        } else {
            model.addAttribute("error", "Неверный логин или пароль");
            return "entrace";
        }
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistration(@RequestParam String login,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        UserOfPharmacy user = userService.findOfUser(login, password);
        if (user != null) {
            model.addAttribute("user", user);
            return "userPlace";
        } else {
            model.addAttribute("error", "Неверный логин или пароль");
            return "entrace";
        }
    }
}
