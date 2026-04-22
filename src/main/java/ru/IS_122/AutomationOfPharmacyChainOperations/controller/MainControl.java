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
import ru.IS_122.AutomationOfPharmacyChainOperations.model.Workers;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.PharmacyService;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

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
        UserOfPharmacy user = UserOfPharmacy.builder()
                .login(login)
                .password(password)
                .dataOfBirt(data_of_birt)
                .phone(phone)
                .passport(passport)
                .fio(fio)
                .email(email).build();
        String error = userService.UserCreate(user);
        if (error != null) {
            model.addAttribute("error", error);
            return "registration";
        } else {
            model.addAttribute("error", ErrorCode.REGISTRATION_SUCCESS.getCode());
            return "entrace";
        }
    }

    @GetMapping("/userPlace")
    public String showUserPlacePage(@RequestParam(defaultValue = "0") long addPharmacy,
                                    Model model) {
        model.addAttribute("addPharmacy", addPharmacy);
        return "userPlace";
    }

    @PostMapping("/userPlace")
    public String createPharmacy(@RequestParam String name,
                                 @RequestParam String legal_name,
                                 @RequestParam String inn,
                                 @RequestParam String kpp,
                                 @RequestParam String ogrn,
                                 @RequestParam String address,
                                 @RequestParam String city,
                                 @RequestParam String region,
                                 @RequestParam String postal_code,
                               Model model,
                               HttpSession session) {
        Pharmacy pharmacy = Pharmacy.builder()
                .name(name)
                .legal_name(legal_name)
                .inn(inn)
                .kpp(kpp)
                .ogrn(ogrn)
                .address(address)
                .city(city)
                .region(region)
                .postal_code(postal_code)
                .build();

        String error = pharmacyService.createPharmacy(pharmacy);
        if (error != null) {
            model.addAttribute("user", error);
            return "userPlace";
        } else {
            model.addAttribute("error", ErrorCode.AUTHORIZATION_FAILED.getCode());
            return "entrace";
        }
    }

    @GetMapping("/pharmacyCreate")
    public String showPharmacyCreatePage(@RequestParam(defaultValue = "false") boolean addAdministrator,
                                         @RequestParam(defaultValue = "") String fio, Model model) {
        model.addAttribute("addAdministrator", addAdministrator);
        if (fio != null && fio.length() >= 2) {
            model.addAttribute("workers", userService.searchWorkers(fio));
        } else {
            model.addAttribute("workers", new ArrayList<>());
        }
        return "pharmacyCreate";
    }

    @GetMapping("/workers/search")
    @ResponseBody
    public List<Workers> searchWorkers(@RequestParam String fio) {
        try {
            if (fio == null || fio.length() < 2) {
                return Collections.emptyList();
            }
            return userService.searchWorkers(fio);
        } catch (Exception e) {
            e.printStackTrace(); // Это выведет реальную ошибку в консоль IDEA
            return new ArrayList<>();
        }
    }

}
