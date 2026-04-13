package ru.IS_122.AutomationOfPharmacyChainOperations.controller;


import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public String inputUser(@RequestParam(required = false) String login,
                          @RequestParam(required = false) String password,
                          Model model,
                          HttpSession session) {
        if (login != null){
            UserOfPharmacy user = userService.findOfUser(login, password);
            // 2. Обработка результата
            if (user != null) {
                model.addAttribute("user", user);
                return "userPlace"; // страница после успешного входа
            } else {
                model.addAttribute("error", "Неверный логин или пароль");
                return "entrace"; // вернуться на страницу входа с ошибкой
            }
        }
        return "entrace";
    }
}
