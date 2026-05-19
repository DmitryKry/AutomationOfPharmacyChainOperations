package ru.IS_122.AutomationOfPharmacyChainOperations.controller;


import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.catalina.Session;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.*;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.MedicineService;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.PharmacyService;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.UserService;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    MedicineService medicineService;


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
    public String showPharmacyPage(@RequestParam(defaultValue = "0") int pagin,
                                   @RequestParam(defaultValue = "1") int page,
                                   Model model) {
        if (page < 1) {
            page = 1;
            pagin = 0;
        }
        List<Pharmacy> pharmacies = pharmacyService.getAllPharmacies()
                .stream()
                .limit(15 + pagin)
                .collect(Collectors.toList());
        model.addAttribute("pharmacies", pharmacies);
        model.addAttribute("pagin", pagin);
        model.addAttribute("page", page);
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
                                         @RequestParam(defaultValue = "false") boolean InfoErrorShow,
                                         @RequestParam(defaultValue = "false") boolean addInjectionMethods,
                                         @RequestParam(required = false) BigDecimal brand_id,
                                         @RequestParam(defaultValue = "") String selectBrand,
                                         @RequestParam(required = false) BigDecimal atc_id,
                                         @RequestParam(defaultValue = "") String selectAtc,
                                         @RequestParam(required = false) BigDecimal manufacturer_id,
                                         @RequestParam(defaultValue = "") String selectManufacturer,
                                         @RequestParam(required = false) BigDecimal country_id,
                                         @RequestParam(defaultValue = "") String selectCountry,
                                         @RequestParam(required = false) BigDecimal pharmacological_group_id,
                                         @RequestParam(defaultValue = "") String selectPharmacologicalGroup,
                                         @RequestParam(required = false) BigDecimal therapeutic_group_id,
                                         @RequestParam(defaultValue = "") String selectTherapeuticGroup,
                                         @RequestParam(required = false) BigDecimal prescription_form_id,
                                         @RequestParam(defaultValue = "") String selectPrescriptionForm,
                                         @RequestParam(required = false) BigDecimal package_type_id,
                                         @RequestParam(defaultValue = "") String selectPackageType,
                                         @RequestParam(required = false) BigDecimal dosage_form_id,
                                         @RequestParam(defaultValue = "") String selectDosageForm,
                                         Model model, Session session) {
        // Логирование для отладки
        System.out.println("Received brand_id: " + brand_id);
        System.out.println("Received selectBrand: " + selectBrand);
        model.addAttribute("addBrand", addBrand);
        model.addAttribute("addDosageForm", addDosageForm);
        model.addAttribute("addManufacturer", addManufacturer);
        model.addAttribute("addCountry", addCountry);
        model.addAttribute("InfoErrorShow", InfoErrorShow);
        model.addAttribute("addAtc", addAtc);
        model.addAttribute("addPharmacologicalGroup", addPharmacologicalGroup);
        model.addAttribute("addTherapeuticGroup", addTherapeuticGroup);
        model.addAttribute("addPrescriptionForm", addPrescriptionForm);
        model.addAttribute("addPackageType", addPackageType);
        model.addAttribute("createCountry", createCountry);
        model.addAttribute("selectBrand", selectBrand);
        model.addAttribute("brand_id", brand_id);
        model.addAttribute("selectAtc", selectAtc);
        model.addAttribute("atc_id", atc_id);
        model.addAttribute("selectManufacturer", selectManufacturer);
        model.addAttribute("manufacturer_id", manufacturer_id);
        model.addAttribute("selectCountry", selectCountry);
        model.addAttribute("country_id", country_id);
        model.addAttribute("selectPharmacologicalGroup", selectPharmacologicalGroup);
        model.addAttribute("pharmacological_group_id", pharmacological_group_id);
        model.addAttribute("selectTherapeuticGroup", selectTherapeuticGroup);
        model.addAttribute("therapeutic_group_id", therapeutic_group_id);
        model.addAttribute("selectPrescriptionForm", selectPrescriptionForm);
        model.addAttribute("prescription_form_id", prescription_form_id);
        model.addAttribute("selectPackageType", selectPackageType);
        model.addAttribute("package_type_id", package_type_id);
        model.addAttribute("selectPackageType", selectPackageType);
        model.addAttribute("package_type_id", package_type_id);
        model.addAttribute("selectDosageForm", selectDosageForm);
        model.addAttribute("dosage_form_id", dosage_form_id);

        if (addBrand){
            model.addAttribute("brands", medicineService.getBrands());
        }
        if (addAtc){
            model.addAttribute("atcList", medicineService.getAtcList());
        }
        if (addManufacturer){
            model.addAttribute("manufacturers", medicineService.getManufuctureList());
        }
        if (addCountry){
            model.addAttribute("countries", medicineService.getCountryList());
        }
        if (addPharmacologicalGroup){
            model.addAttribute("pharmacologicalGroups", medicineService.getPharmacologicalGroupList());
        }
        if (addTherapeuticGroup){
            model.addAttribute("therapeuticGroups", medicineService.getTherapeuticGroupList());
        }
        if (addPrescriptionForm){
            model.addAttribute("prescriptionForms", medicineService.getPrescriptionFormList());
        }
        if (addPackageType){
            model.addAttribute("packageTypes", medicineService.getTypePackagingList());
        }
        if (addDosageForm){
            model.addAttribute("dosageForms", medicineService.getDosageFormList());
        }


        return "medicineCreate";
    }

    @PostMapping("/medicineCreate")
    public String medicineCreatePage(@ModelAttribute Medicine medicine, Model model) {
        String error = medicineService.createMedicine(medicine);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @PostMapping("/medicineCreate/dosageFormCreate/addInjectionMethods")
    public String showInjectionMethodsPage(@RequestParam(defaultValue = "true") boolean addInjectionMethods,
                                    @RequestParam String name, Model model) {
        model.addAttribute("addInjectionMethods", addInjectionMethods);
        model.addAttribute("InjectionMethods", medicineService.findAdministrationRoute(name));
        return "medicineCreate";
    }

    @PostMapping("/medicineCreate/addBrand")
    public String showFindBrandPage(@RequestParam(defaultValue = "true") boolean addBrand,
                                    @RequestParam String brandName, Model model) {
        model.addAttribute("addBrand", addBrand);
        Brand brand = Brand.builder().name(brandName).build();
        model.addAttribute("brands", medicineService.findBrands(brand.getName()));
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/brandCreate")
    public String showBrandCreatePage(@RequestParam(defaultValue = "true") boolean brandCreate, Model model) {
        model.addAttribute("brandCreate", brandCreate);
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/brandCreate")
    public String BrandCreatePage(@RequestParam(defaultValue = "true") boolean createCountry,
                              @ModelAttribute Brand brand, Model model) {
        String error = medicineService.createBrands(brand);
        model.addAttribute("createCountry", createCountry);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @PostMapping("/medicineCreate/addAtc")
    public String showFindAtcCodePage(@RequestParam(defaultValue = "true") boolean addAtc,
                                    @RequestParam String atcCode, Model model) {
        model.addAttribute("addAtc", addAtc);
        AtcClassification atcClassification = AtcClassification.builder().code(atcCode).build();
        model.addAttribute("atcList", medicineService.findAtcCode(atcClassification.getCode()));
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/prescriptionFormCreate")
    public String showPrescriptionFormCreatePage(@RequestParam(defaultValue = "true") boolean createPrescriptionForm,
                                                  Model model) {
        model.addAttribute("createPrescriptionForm", createPrescriptionForm);
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/addPrescriptionForm")
    public String showPrescriptionFormsPage(@RequestParam(defaultValue = "true") boolean addPrescriptionForm,
                                                 @RequestParam String formName,
                                                 Model model) {
        model.addAttribute("addPrescriptionForm", addPrescriptionForm);
        model.addAttribute("prescriptionForms", medicineService.findPrescriptionForm(formName));
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/prescriptionFormCreate")
    public String PrescriptionFormCreatePage(@RequestParam(defaultValue = "true") boolean createPrescriptionForm,
                                                 @ModelAttribute PrescriptionForm prescriptionForm, Model model) {
        model.addAttribute("createPrescriptionForm", createPrescriptionForm);
        String error = medicineService.createPrescriptionForm(prescriptionForm);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }


    @GetMapping("medicineCreate/packageTypeCreate")
    public String showPackageTypeCreatePage(@RequestParam(defaultValue = "true") boolean createPackageType,
                                                 Model model) {
        model.addAttribute("createPackageType", createPackageType);
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/addPackageType")
    public String findPackageTypePage(@RequestParam(defaultValue = "true") boolean addPackageType,
                                            @RequestParam String packageTypeName, Model model) {
        model.addAttribute("addPackageType", addPackageType);
        model.addAttribute("packageTypes", medicineService.findTypePackaging(packageTypeName));
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/packageTypeCreate")
    public String showPackageTypeCreatePage(@RequestParam(defaultValue = "true") boolean createPackageType,
                                            @ModelAttribute TypePackaging packageType, Model model) {
        model.addAttribute("createPackageType", createPackageType);
        String error = medicineService.createTypePackaging(packageType);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }


    @PostMapping("medicineCreate/addPharmacologicalGroup")
    public String findPharmacologicalGroupCreatePage(@RequestParam(defaultValue = "true") boolean addPharmacologicalGroup,
                                                     @RequestParam String groupName,
                                            Model model) {
        model.addAttribute("addPharmacologicalGroup", addPharmacologicalGroup);
        model.addAttribute("pharmacologicalGroups", medicineService.findPharmacologicalGroup(groupName));
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/pharmacologicalGroupCreate")
    public String showPharmacologicalGroupCreatePage(@RequestParam(defaultValue = "true") boolean pharmacologicalGroupCreate,
                                                     Model model) {
        model.addAttribute("pharmacologicalGroupCreate", pharmacologicalGroupCreate);
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/pharmacologicalGroupCreate")
    public String PharmacologicalGroupCreatePage(@RequestParam(defaultValue = "true") boolean pharmacologicalGroupCreate,
                                                     @ModelAttribute PharmacologicalGroup pharmacologicalGroup,
                                                 Model model) {
        model.addAttribute("pharmacologicalGroupCreate", pharmacologicalGroupCreate);
        String error = medicineService.createPharmacologicalGroup(pharmacologicalGroup);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/therapeuticGroupCreate")
    public String showTherapeuticGroupCreatePage(@RequestParam(defaultValue = "true") boolean therapeuticGroupCreate,
                                                     Model model) {
        model.addAttribute("therapeuticGroupCreate", therapeuticGroupCreate);
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/addTherapeuticGroup")
    public String TherapeuticGroupCreatePage(@RequestParam(defaultValue = "true") boolean addTherapeuticGroup,
                                             @RequestParam String groupName,
                                             Model model) {
        model.addAttribute("addTherapeuticGroup", addTherapeuticGroup);
        model.addAttribute("therapeuticGroups", medicineService.findTherapeuticGroup(groupName));
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/therapeuticGroupCreate")
    public String TherapeuticGroupCreatePage(@RequestParam(defaultValue = "true") boolean therapeuticGroupCreate,
                                             @ModelAttribute TherapeuticGroup therapeuticGroup,
                                                 Model model) {
        model.addAttribute("therapeuticGroupCreate", therapeuticGroupCreate);
        String error = medicineService.createTherapeuticGroup(therapeuticGroup);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/addManufacturer")
    public String showManufacturerAddPage(@RequestParam(defaultValue = "true") boolean addManufacturer,
                                          @RequestParam String manufacturerName,
                                             Model model) {
        model.addAttribute("addManufacturer", addManufacturer);
        model.addAttribute("manufacturers", medicineService.findManufucture(manufacturerName));
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/manufacturerCreate")
    public String showManufacturerCreatePage(@RequestParam(defaultValue = "true") boolean manufacturerCreate,
                                                 Model model) {
        model.addAttribute("manufacturerCreate", manufacturerCreate);
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/manufacturerCreate")
    public String ManufacturerCreatePage(@RequestParam(defaultValue = "true") boolean manufacturerCreate,
                                         @ModelAttribute Manufacturer manufacturer,
                                             Model model) {
        model.addAttribute("manufacturerCreate", manufacturerCreate);
        String error = medicineService.createManufacturer(manufacturer);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/atcCreate")
    public String showAtcCreatePage(@RequestParam(defaultValue = "true") boolean atcCreate,
                                             Model model) {
        model.addAttribute("atcCreate", atcCreate);
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/atcCreate")
    public String AtcCodeCreatePage(@RequestParam(defaultValue = "true") boolean atcCreate,
                                    @ModelAttribute AtcClassification atcClassification,
                                    Model model) {
        String error = medicineService.createAtcCode(atcClassification);
        model.addAttribute("atcCreate", atcCreate);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/addDosageForm")
    public String findDosageFormPage(@RequestParam(defaultValue = "true") boolean addDosageForm,
                                    @RequestParam String dosageFormName,
                                    Model model) {
        model.addAttribute("dosageForms", medicineService.findDosageForm(dosageFormName));
        model.addAttribute("addDosageForm", addDosageForm);
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/dosageFormCreate")
    public String showDosageFormCreatePage(@RequestParam(defaultValue = "true") boolean dosageFormCreate,
                                           @RequestParam(defaultValue = "false") boolean addInjectionMethods,
                                           @RequestParam(required = false) BigDecimal InjectionMethods_id,
                                           @RequestParam(defaultValue = "") String selectInjectionMethods,
                                    Model model) {
        model.addAttribute("dosageFormCreate", dosageFormCreate);
        model.addAttribute("InjectionMethods_id", InjectionMethods_id);
        model.addAttribute("selectInjectionMethods", selectInjectionMethods);
        model.addAttribute("addInjectionMethods", addInjectionMethods);
        if (addInjectionMethods){
            model.addAttribute("InjectionMethods", medicineService.getAdministrationRoute());
        }
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/dosageFormCreate")
    public String DosageFormCreate(@RequestParam(defaultValue = "true") boolean addDosageForm,
                                                   @ModelAttribute DosageForm dosageForm,
                                                   Model model) {
        model.addAttribute("addDosageForm", addDosageForm);
        String error = medicineService.createDosageForm(dosageForm);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/dosageFormCreate/methodOfAdministrationCreate")
    public String showMethodOfAdministrationCreatePage(@RequestParam(defaultValue = "true") boolean methodOfAdministrationCreate,
                                      Model model) {
        model.addAttribute("methodOfAdministrationCreate", methodOfAdministrationCreate);
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/dosageFormCreate/methodOfAdministrationCreate")
    public String MethodOfAdministrationCreatePage(@RequestParam(defaultValue = "true") boolean methodOfAdministrationCreate,
                                                       @ModelAttribute AdministrationRoute administrationRoute,
                                                       Model model) {
        model.addAttribute("methodOfAdministrationCreate", methodOfAdministrationCreate);
        String error = medicineService.createAdministrationRoute(administrationRoute);
        model.addAttribute("methodOfAdministrationCreate", methodOfAdministrationCreate);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/createCountry")
    public String showCountryPage(@RequestParam(defaultValue = "true") boolean createCountry,
                                           Model model) {
        model.addAttribute("createCountry", createCountry);
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/createCountry")
    public String CountryPage(@RequestParam(defaultValue = "true") boolean createCountry,
                                  @RequestParam String countryName, Model model) {
        Country country = Country.builder().name(countryName).build();
        String error = medicineService.createCountry(country);
        model.addAttribute("createCountry", createCountry);
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/addCountry")
    public String showCountriesPage(@RequestParam(defaultValue = "true") boolean addCountry,
                                    @RequestParam String countryName,
                                    Model model) {
        model.addAttribute("countries", medicineService.findCountry(countryName));
        model.addAttribute("addCountry", addCountry);
        return "medicineCreate";
    }



}
