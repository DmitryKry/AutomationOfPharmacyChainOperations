package ru.IS_122.AutomationOfPharmacyChainOperations.controller;


import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.*;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.PharmacyService;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.UserService;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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
/*
    @PostMapping("/userPlace")
    public String createPharmacy(Pharmacy form,
                               Model model,
                               HttpSession session) {
        Pharmacy pharmacy = Pharmacy.builder()
                .name(form.getName())
                .legal_name(form.getLegal_name())
                .inn(form.getInn())
                .kpp(form.getKpp())
                .ogrn(form.getOgrn())
                .address(form.getAddress())
                .ID_CITY(form.getID_CITY())
                .region(form.getRegion())
                .postal_code(form.getPostal_code())
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
*/
    @GetMapping("/pharmacy")
    public String showPharmacyPage(Model model) {
        return "userPlace";
    }

    @GetMapping("/pharmacyCreate")
    public String showPharmacyCreatePage(@RequestParam(defaultValue = "false") boolean addAdministrator,
                                         @RequestParam(defaultValue = "false") boolean addCity,
                                         @RequestParam(defaultValue = "false") boolean createCity,
                                         @RequestParam(defaultValue = "") String selectAdmin,
                                         @RequestParam(defaultValue = "") String selectCityRegion,
                                         @RequestParam(defaultValue = "") String selectCity,
                                         @RequestParam(required = false) BigDecimal idAdmin,
                                         @RequestParam(required = false) BigDecimal idCity,Model model) {
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("addCity", addCity);
        model.addAttribute("createCity", createCity);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("idCity", idCity);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("selectCityRegion", selectCityRegion);
        model.addAttribute("selectCity", selectCity);
        model.addAttribute("addAdministrators", userService.getAdministrators());
        model.addAttribute("cities", pharmacyService.getAllCities());
        return "pharmacyCreate";
    }

    @PostMapping("/pharmacyCreate")
    public String pharmacyCreatePage(Pharmacy form,
                                     @RequestParam(defaultValue = "false") boolean addAdministrator,
                                     @RequestParam(defaultValue = "") String selectAdmin,
                                     @RequestParam(defaultValue = "") String selectCityRegion,
                                     @RequestParam(defaultValue = "") String selectCity,
                                     @RequestParam(required = false) BigDecimal idCity,
                                     @RequestParam(required = false) BigDecimal idAdmin, Model model) {
        Pharmacy pharmacy = Pharmacy.builder()
                .name(form.getName())
                .legal_name(form.getLegal_name())
                .inn(form.getInn())
                .kpp(form.getKpp())
                .ogrn(form.getOgrn())
                .address(form.getAddress())
                .ID_CITY(form.getID_CITY())
                .region(form.getRegion())
                .postal_code(form.getPostal_code())
                .build();
        pharmacyService.createPharmacy(pharmacy, idAdmin);
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("idCity", idCity);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("selectCityRegion", selectCityRegion);
        model.addAttribute("selectCity", selectCity);
        return "pharmacyCreate";
    }

    @PostMapping("/pharmacyCreate/addAdministrator")
    public String pharmacyCreatePage(@RequestParam String addAdministratorName,
                                     @RequestParam(defaultValue = "") String selectAdmin,
                                     @RequestParam(defaultValue = "") String selectCityRegion,
                                     @RequestParam(defaultValue = "") String selectCity,
                                     @RequestParam(required = false) BigDecimal idAdmin,
                                     @RequestParam(required = false) BigDecimal idCity,
                                     @RequestParam(defaultValue = "true") boolean addAdministrator, Model model) {
        if (!addAdministratorName.isEmpty())
            model.addAttribute("addAdministrators", userService.searchAdministrators(addAdministratorName));
        else
            model.addAttribute("addAdministrators", userService.getAdministrators());
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("idCity", idCity);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("selectCityRegion", selectCityRegion);
        model.addAttribute("selectCity", selectCity);
        model.addAttribute("addAdministrator", addAdministrator);
        return "pharmacyCreate";
    }

    @PostMapping("/pharmacyCreate/addCity")
    public String pharmacyCreatePageAddCity(@RequestParam String addCity,
                                            @RequestParam(defaultValue = "") String selectAdmin,
                                            @RequestParam(defaultValue = "") String selectCityRegion,
                                            @RequestParam(defaultValue = "") String selectCity,
                                            @RequestParam(required = false) BigDecimal idAdmin,
                                            @RequestParam(required = false) BigDecimal idCity,
                                     @RequestParam(defaultValue = "false") boolean addAdministrator, Model model) {
        if (!addCity.isEmpty())
            model.addAttribute("cities", pharmacyService.findCities(addCity));
        else
            model.addAttribute("cities", pharmacyService.getAllCities());
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("idCity", idCity);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("selectCityRegion", selectCityRegion);
        model.addAttribute("selectCity", selectCity);
        model.addAttribute("addAdministrator", addAdministrator);
        return "pharmacyCreate";
    }


    @PostMapping("/pharmacyCreate/createCity")
    public String pharmacyCreatePageCreateCity(City city,
                                               @RequestParam(defaultValue = "false") boolean createCity,
                                               @RequestParam(defaultValue = "false") boolean addCity,
                                               @RequestParam(defaultValue = "false") boolean addAdministrator, Model model) {
        pharmacyService.createCity(city);
        model.addAttribute("addAdministrator", createCity);
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("addAdministrator", addCity);
        return "pharmacyCreate";
    }

    @GetMapping("/workerCreate")
    public String showWorkerCreatePage(@RequestParam(defaultValue = "false") boolean addRole,
                                       @RequestParam(defaultValue = "false") boolean addUser,
                                       @RequestParam(defaultValue = "false") boolean createRole,
                                       @RequestParam(defaultValue = "") String fio,
                                       @RequestParam(required = false) BigDecimal idUser,
                                       @RequestParam(required = false) BigDecimal idRole,
                                       @RequestParam(defaultValue = "") String selectRole,
                                       @RequestParam(defaultValue = "") String selectUser, Model model) {
        model.addAttribute("users", userService.searchUsers(fio));
        model.addAttribute("roles", userService.getRoles());
        model.addAttribute("addRole", addRole);
        model.addAttribute("addUser", addUser);
        model.addAttribute("createRole", createRole);
        model.addAttribute("idUser", idUser);
        model.addAttribute("idRole", idRole);
        model.addAttribute("selectRole", selectRole);
        model.addAttribute("selectUser", selectUser);
        return "workersCreate";
    }

    @PostMapping("/workerCreate/searchUsers")
    public String showWorkerCreatePageTakeUsersSearchUsers(@RequestParam(defaultValue = "false") boolean addRole,
                                                @RequestParam String searchUser,
                                       @RequestParam(defaultValue = "true") boolean addUser,
                                       @RequestParam(defaultValue = "") String fio, Model model) {
        model.addAttribute("users", userService.searchUsers(searchUser));
        model.addAttribute("addRole", addRole);
        model.addAttribute("addUser", addUser);
        return "workersCreate";
    }

    @PostMapping("/workerCreate")
    public String showWorkerCreatePage(@RequestParam(defaultValue = "false") boolean addRole,
                                                @RequestParam(defaultValue = "false") boolean addUser,
                                       @RequestParam BigDecimal id_of_user,
                                       @RequestParam BigDecimal salary,
                                       @RequestParam String education,
                                       @RequestParam BigDecimal id_of_role,
                                       @RequestParam String INN,
                                       @RequestParam String snils, Model model) {
        Workers worker = Workers.builder()
                .id_of_user(id_of_user)
                .salary(salary)
                .education(education)
                .id_of_role(id_of_role)
                .inn(INN)
                .snils(snils)
                .build();
        userService.workerCreate(worker);
        model.addAttribute("addRole", addRole);
        model.addAttribute("addUser", addUser);
        return "workersCreate";
    }

    @PostMapping("/workerCreate/createRole")
    public String showWorkerCreatePageCreateRole(@RequestParam(defaultValue = "true") boolean addRole,
                                                 @RequestParam(defaultValue = "false") boolean createRole,
                                                @RequestParam(defaultValue = "true") boolean addUser,
                                                @RequestParam(defaultValue = "") String roleName, Model model) {
        Role role = Role.builder().name(roleName).build();
        userService.roleCreate(role);
        model.addAttribute("addRole", addRole);
        model.addAttribute("createRole", createRole);
        model.addAttribute("addUser", addUser);
        return "workersCreate";
    }

    @GetMapping("/medicinePlace")
    public String showMedicinePlacePage(Model model) {
        return "medicinePlace";
    }

    @GetMapping("/medicineCreate")
    public String showMedicineCreatePage(@RequestParam(defaultValue = "false") boolean addBrand,
                                         @RequestParam(defaultValue = "false") boolean addDosageForm,
                                         @RequestParam(defaultValue = "false") boolean addManufacturer,
                                         @RequestParam(defaultValue = "false") boolean addCountry,
                                         @RequestParam(defaultValue = "false") boolean addAtc,
                                         @RequestParam(defaultValue = "false") boolean addPharmacologicalGroup,
                                         @RequestParam(defaultValue = "false") boolean addTherapeuticGroup,
                                         @RequestParam(defaultValue = "false") boolean addPrescriptionForm,
                                         @RequestParam(defaultValue = "false") boolean addPackageType,
                                         @RequestParam(defaultValue = "false") boolean createCountry,

                                         Model model) {
        model.addAttribute("addBrand", addBrand);
        model.addAttribute("addDosageForm", addDosageForm);
        model.addAttribute("addManufacturer", addManufacturer);
        model.addAttribute("addCountry", addCountry);
        model.addAttribute("addAtc", addAtc);
        model.addAttribute("addPharmacologicalGroup", addPharmacologicalGroup);
        model.addAttribute("addTherapeuticGroup", addTherapeuticGroup);
        model.addAttribute("addPrescriptionForm", addPrescriptionForm);
        model.addAttribute("addPackageType", addPackageType);
        model.addAttribute("createCountry", createCountry);

        return "medicineCreate";
    }

    @GetMapping("medicineCreate/prescriptionFormCreate")
    public String showPrescriptionFormCreatePage(@RequestParam(defaultValue = "true") boolean createPrescriptionForm,
                                                  Model model) {
        model.addAttribute("createPrescriptionForm", createPrescriptionForm);
        return "medicineCreate";
    }


    @GetMapping("medicineCreate/packageTypeCreate")
    public String showPackageTypeCreatePage(@RequestParam(defaultValue = "true") boolean createPackageType,
                                                 Model model) {
        model.addAttribute("createPackageType", createPackageType);
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/pharmacologicalGroupCreate")
    public String showPharmacologicalGroupCreatePage(@RequestParam(defaultValue = "true") boolean pharmacologicalGroupCreate,
                                            Model model) {
        model.addAttribute("pharmacologicalGroupCreate", pharmacologicalGroupCreate);
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/therapeuticGroupCreate")
    public String showTherapeuticGroupCreatePage(@RequestParam(defaultValue = "true") boolean therapeuticGroupCreate,
                                                     Model model) {
        model.addAttribute("therapeuticGroupCreate", therapeuticGroupCreate);
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/manufacturerCreate")
    public String showManufacturerCreatePage(@RequestParam(defaultValue = "true") boolean manufacturerCreate,
                                                 Model model) {
        model.addAttribute("manufacturerCreate", manufacturerCreate);
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/atcCreate")
    public String showAtcCreatePage(@RequestParam(defaultValue = "true") boolean atcCreate,
                                             Model model) {
        model.addAttribute("atcCreate", atcCreate);
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/dosageFormCreate")
    public String showDosageFormCreatePage(@RequestParam(defaultValue = "true") boolean dosageFormCreate,
                                    Model model) {
        model.addAttribute("dosageFormCreate", dosageFormCreate);
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/dosageFormCreate/methodOfAdministrationCreate")
    public String showMethodOfAdministrationCreatePage(@RequestParam(defaultValue = "true") boolean methodOfAdministrationCreate,
                                      Model model) {
        model.addAttribute("methodOfAdministrationCreate", methodOfAdministrationCreate);
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/brandCreate")
    public String showBrandCreatePage(@RequestParam(defaultValue = "true") boolean brandCreate,
                                           Model model) {
        model.addAttribute("brandCreate", brandCreate);
        return "medicineCreate";
    }


}
