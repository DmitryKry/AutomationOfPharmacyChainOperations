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
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/pharmacy")
@AllArgsConstructor
public class MainControl {

    public enum ErrorCode {
        NO_ERROR(0),
        AUTHORIZATION_FAILED(1),
        REGISTRATION_SUCCESS(2);

        private final int code;

        ErrorCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

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
            model.addAttribute("error", ErrorCode.AUTHORIZATION_FAILED.getCode());
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
                                      @RequestParam LocalDate data_of_birt,
                                      @RequestParam String phone,
                                      @RequestParam String passport,
                                      @RequestParam String fio,
                                      @RequestParam String email,
                               Model model,
                               HttpSession session) {
        String error = userService.UserCreate(login, password, data_of_birt, phone, passport, fio, email);
        if (error != null) {
            model.addAttribute("error", error);
            return "registration";
        } else {
            model.addAttribute("error", ErrorCode.REGISTRATION_SUCCESS.getCode());
            return "entrace";
        }
    }
}
