package ru.IS_122.AutomationOfPharmacyChainOperations.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.UriComponentsBuilder;
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
import ru.IS_122.AutomationOfPharmacyChainOperations.service.OrderService;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.PharmacyService;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    private MedicineService medicineService;
    @Autowired
    private OrderService orderService;


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
            model.addAttribute("pagin", 0);
            model.addAttribute("page", 0);
            List<Pharmacy> pharmacies = pharmacyService.getAllPharmacies().stream()
                    .limit(15)
                    .collect(Collectors.toList());
            List<Photos> photos = pharmacyService.getPhotosPharmacy();
            model.addAttribute("pharmacies", pharmacies);
            model.addAttribute("photos", photos);
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
                                   @RequestParam(required = false) BigDecimal orderID,
                                   @RequestParam(required = false) BigDecimal userID,
                                   Model model) {
        if (page < 1) {
            page = 1;
            pagin = 0;
        }
        List<Pharmacy> pharmacies = pharmacyService.getAllPharmacies()
                .stream()
                .limit(15 + pagin)
                .collect(Collectors.toList());

        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("pharmacies", pharmacies);
        model.addAttribute("orderID", orderID);
        model.addAttribute("photos", pharmacyService.getPhotosPharmacy());
        model.addAttribute("pagin", pagin);
        model.addAttribute("page", page);
        return "userPlace";
    }

    @PostMapping("/pharmacy")
    public String showPharmacySortPage(@RequestParam(defaultValue = "0") int pagin,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String REGION,
                                       @RequestParam(required = false) BigDecimal orderID,
                                       @RequestParam(required = false) String CITY,
                                       @RequestParam(required = false) String adress,
                                       @RequestParam(required = false) String grade,
                                       @RequestParam(required = false) String coverage,
                                       @RequestParam(required = false) BigDecimal userID,
                                       @RequestParam(defaultValue = "false") Boolean InfoErrorShow,
                                   Model model) {
        if (page < 1) {
            page = 1;
            pagin = 0;
        }
        List<Pharmacy> pharmacies = new ArrayList<>();
        model.addAttribute("orderID", orderID);
        if (grade == null && coverage.isEmpty()) {
            pharmacies = pharmacyService.sortPharmacies(name, REGION, CITY, adress)
                    .stream()
                    .limit(15 + pagin)
                    .collect(Collectors.toList());
        }
        if (pharmacies.isEmpty()) {
            pharmacies = pharmacyService.getAllPharmacies().stream()
                    .limit(15 + pagin)
                    .collect(Collectors.toList());
            model.addAttribute("InfoError", "Ничего не найдено");
            model.addAttribute("InfoErrorShow", true);
        }
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("pharmacies", pharmacies);
        model.addAttribute("photos", pharmacyService.getPhotosPharmacy());
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
                                         @RequestParam(required = false) BigDecimal idCity,
                                         @RequestParam(required = false) BigDecimal pharmacyID,
                                         @RequestParam(required = false) BigDecimal photoID,
                                         @RequestParam(defaultValue = "false") boolean edit,
                                         @RequestParam(required = false) String InfoError,
                                         @RequestParam(required = false) Boolean InfoErrorShow,
                                         @RequestParam(required = false) BigDecimal userID,
                                         HttpSession session, HttpServletResponse response,
                                         Model model) {
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("addCity", addCity);
        model.addAttribute("createCity", createCity);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("idCity", idCity);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("selectCityRegion", selectCityRegion);
        model.addAttribute("selectCity", selectCity);
        model.addAttribute("edit", edit);
        model.addAttribute("InfoError", InfoError);
        model.addAttribute("InfoErrorShow", InfoErrorShow);
        model.addAttribute("user", userService.get_id_user(userID));
        BigDecimal p_ID = (BigDecimal) session.getAttribute("photoID");
        if (pharmacyService.getPhotosPharmacy().stream().filter(photos -> photos.getId().equals(p_ID) && photos.getEntityId() != null)
                .findFirst().orElse(null) != null) {
            photoID = null;
        }
        else {
            photoID = p_ID == null ? photoID : p_ID;
        }
        //pharmacyID = BigDecimal.valueOf(70);
        //photoID = BigDecimal.valueOf(83);
        if (pharmacyID != null && pharmacyID.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = pharmacyService.getPhotoPharmacy(pharmacyID);
            //Photos photos = userService.getPhoto(photoID);
            model.addAttribute("pharmacy", pharmacyService.getPharmacyById(pharmacyID).get(0));
            if (photoID == null)
                model.addAttribute("photos", photos);
        }
        if (photoID != null && photoID.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = userService.getPhoto(photoID) == null ? new Photos() : userService.getPhoto(photoID);
            model.addAttribute("photos", photos);
        }
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
                                     @RequestParam(required = false) BigDecimal photoID,
                                     @RequestParam(required = false) BigDecimal idCity,
                                     @RequestParam(required = false) BigDecimal idAdmin,
                                     @RequestParam(required = false) BigDecimal userID,
            HttpSession session, Model model) {
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
        Pharmacy pharmacy1 = pharmacyService.createPharmacy(pharmacy, idAdmin);
        photoID = (BigDecimal) session.getAttribute("photoID");
        if (photoID != null && photoID.compareTo(BigDecimal.ZERO) > 0){
            pharmacyService.updatePhoto(pharmacy1.getId(), photoID);
        }
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("idCity", idCity);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("selectCityRegion", selectCityRegion);
        model.addAttribute("selectCity", selectCity);
        if (pharmacy1.getErrorMessage() != null){
            model.addAttribute("InfoError", pharmacy1.getErrorMessage());
        }
        else{
            model.addAttribute("InfoError", "Объект создан успешно");
        }
        model.addAttribute("InfoErrorShow", true);
        return "pharmacyCreate";
    }

    @GetMapping("/pharmacyEdit")
    public String showPharmacyEditPage(@RequestParam(defaultValue = "false") boolean addAdministrator,
                                         @RequestParam(defaultValue = "false") boolean addCity,
                                         @RequestParam(defaultValue = "false") boolean createCity,
                                         @RequestParam(defaultValue = "") String selectAdmin,
                                         @RequestParam(defaultValue = "") String selectCityRegion,
                                         @RequestParam(defaultValue = "") String selectCity,
                                         @RequestParam(required = false) BigDecimal idAdmin,
                                         @RequestParam(required = false) BigDecimal idCity,
                                         @RequestParam(required = false) BigDecimal pharmacyID,
                                         @RequestParam(required = false) BigDecimal photoID,
                                         @RequestParam(defaultValue = "false") boolean edit,
                                         @RequestParam(required = false) boolean delete,
                                         @RequestParam(required = false) boolean deletePharmacy,
                                         @RequestParam(required = false) BigDecimal userID,
                                         HttpSession session, HttpServletResponse response,
                                         Model model) {
        Pharmacy pharmacy = new Pharmacy();
        if (pharmacyID != null && pharmacyID.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = pharmacyService.getPhotoPharmacy(pharmacyID);
            //Photos photos = userService.getPhoto(photoID);
            pharmacy = pharmacyService.getPharmacyById(pharmacyID).get(0);
            model.addAttribute("pharmacy", pharmacy);
            if (photoID == null)
                model.addAttribute("photos", photos);
        }
        UserOfPharmacy admin = userService.getAdminPharmacy(pharmacy.getId()).get(0);
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("addCity", addCity);
        model.addAttribute("createCity", createCity);
        model.addAttribute("idAdmin", admin.getId());
        model.addAttribute("idCity", idCity);
        model.addAttribute("selectAdmin", admin.getFio());
        model.addAttribute("selectCityRegion", selectCityRegion);
        model.addAttribute("selectCity", selectCity);
        model.addAttribute("edit", edit);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("delete", delete);
        model.addAttribute("addAdministrators", userService.getAdministrators());
        model.addAttribute("cities", pharmacyService.getAllCities());
        BigDecimal p_ID = (BigDecimal) session.getAttribute("photoID");

        if (deletePharmacy && pharmacyID != null){
            Pharmacy pharmacy1 = pharmacyService.deletePharmacy(pharmacyID);
            model.addAttribute("InfoError", pharmacy1.getErrorMessage());
            model.addAttribute("InfoErrorShow", true);
        }
        if (pharmacyService.getPhotosPharmacy().stream().filter(photos -> photos.getId().equals(p_ID) && photos.getEntityId() != null)
                .findFirst().orElse(null) != null) {
            photoID = null;
        }
        //pharmacyID = BigDecimal.valueOf(70);
        //photoID = BigDecimal.valueOf(83);

        if (photoID != null && photoID.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = userService.getPhoto(photoID) == null ? new Photos() : userService.getPhoto(photoID);
            model.addAttribute("photos", photos);
        }


        return "pharmacyEdit";
    }

    @PostMapping("/pharmacyEdit")
    public String pharmacyEditPage(Pharmacy form,
                                     @RequestParam(defaultValue = "false") boolean addAdministrator,
                                     @RequestParam(defaultValue = "") String selectAdmin,
                                     @RequestParam(defaultValue = "") String selectCityRegion,
                                     @RequestParam(defaultValue = "") String selectCity,
                                     @RequestParam(required = false) BigDecimal photoID,
                                     @RequestParam(required = false) BigDecimal idCity,
                                     @RequestParam(required = false) BigDecimal idAdmin,
                                     @RequestParam(required = false) BigDecimal userID,
                                     HttpSession session, Model model) {
        Pharmacy pharmacy = Pharmacy.builder()
                .id(form.getId())
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
        Pharmacy pharmacy1 = pharmacyService.updatePharmacy(pharmacy, idAdmin);
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("idCity", idCity);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("selectCityRegion", selectCityRegion);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("selectCity", selectCity);
        if (pharmacy1.getErrorMessage() != null){
            model.addAttribute("InfoError", pharmacy1.getErrorMessage());
        }
        else{
            model.addAttribute("InfoError", "Объект редактирован успешно");
        }
        model.addAttribute("InfoErrorShow", true);
        return "pharmacyEdit";
    }


    @PostMapping("/addAdministrator")
    public String pharmacyCreatePage(@RequestParam String addAdministratorName,
                                     @RequestParam(defaultValue = "") String selectAdmin,
                                     @RequestParam(defaultValue = "") String selectCityRegion,
                                     @RequestParam(defaultValue = "") String selectCity,
                                     @RequestParam(required = false) BigDecimal idAdmin,
                                     @RequestParam(required = false) BigDecimal idCity,
                                     @RequestParam(required = false) BigDecimal photoID,
                                     @RequestParam(defaultValue = "true") boolean addAdministrator,
                                     @RequestParam(required = false) BigDecimal userID,
                                     @RequestParam(defaultValue = "pharmacyCreate") String source, Model model) {
        if (!addAdministratorName.isEmpty())
            model.addAttribute("addAdministrators", userService.searchAdministrators(addAdministratorName));
        else
            model.addAttribute("addAdministrators", userService.getAdministrators());
        if (photoID != null && photoID.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = userService.getPhoto(photoID) == null ? new Photos() : userService.getPhoto(photoID);
            model.addAttribute("photos", photos);
        }
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("idCity", idCity);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("selectCityRegion", selectCityRegion);
        model.addAttribute("selectCity", selectCity);
        if ("pharmacyEdit".equals(source)){
            return "pharmacyEdit";
        }
        return "pharmacyCreate";
    }

    @PostMapping("/addCity")
    public String pharmacyCreatePageAddCity(@RequestParam String city,
                                            @RequestParam(defaultValue = "") String selectAdmin,
                                            @RequestParam(defaultValue = "") String selectCityRegion,
                                            @RequestParam(defaultValue = "") String selectCity,
                                            @RequestParam(required = false) BigDecimal idAdmin,
                                            @RequestParam(required = false) BigDecimal idCity,
                                            @RequestParam(required = false) BigDecimal photoID,
                                     @RequestParam(defaultValue = "false") boolean addAdministrator,
                                            @RequestParam(required = false) BigDecimal userID,
                                            @RequestParam(defaultValue = "true") boolean addCity,
                                            @RequestParam(defaultValue = "pharmacyCreate") String source, Model model) {
        if (!city.isEmpty()) {
            List<City> cities = pharmacyService.findCities(city);
            model.addAttribute("cities", cities);
        }
        else
            model.addAttribute("cities", pharmacyService.getAllCities());
        if (photoID != null && photoID.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = userService.getPhoto(photoID) == null ? new Photos() : userService.getPhoto(photoID);
            model.addAttribute("photos", photos);
        }
        model.addAttribute("idAdmin", idAdmin);
        model.addAttribute("idCity", idCity);
        model.addAttribute("selectAdmin", selectAdmin);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("selectCityRegion", selectCityRegion);
        model.addAttribute("selectCity", selectCity);
        model.addAttribute("addCity", addCity);
        if ("pharmacyEdit".equals(source)){
            return "pharmacyEdit";
        }
        return "pharmacyCreate";
    }


    @PostMapping("/createCity")
    public String pharmacyCreatePageCreateCity(City city,
                                               @RequestParam(defaultValue = "false") boolean createCity,
                                               @RequestParam(defaultValue = "false") boolean addCity,
                                               @RequestParam(defaultValue = "false") boolean addAdministrator,
                                               @RequestParam(required = false) BigDecimal userID,
                                               @RequestParam String source, Model model) {
        String error = pharmacyService.createCity(city);
        model.addAttribute("addAdministrator", createCity);
        model.addAttribute("addAdministrator", addAdministrator);
        model.addAttribute("addAdministrator", addCity);
        model.addAttribute("user", userService.get_id_user(userID));
        if (error != null){
            model.addAttribute("InfoError", error);
        }
        else{
            model.addAttribute("InfoError", "Объект создан успешно");
        }
        model.addAttribute("InfoErrorShow", true);
        if ("pharmacyEdit".equals(source)){
            return "pharmacyEdit";
        }
        return "pharmacyCreate";
    }

    @GetMapping("/pharmacyView")
    public String pharmacyView(@RequestParam BigDecimal idPharmacy, @RequestParam(required = false) BigDecimal userID,
                               Model model) {
        Pharmacy pharmacy = new Pharmacy();
        if (idPharmacy != null && idPharmacy.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = pharmacyService.getPhotoPharmacy(idPharmacy);
            //Photos photos = userService.getPhoto(photoID);
            pharmacy = pharmacyService.getPharmacyById(idPharmacy).get(0);
            model.addAttribute("pharmacy", pharmacy);
        }
        model.addAttribute("pharmacy", pharmacyService.getPharmacyById(idPharmacy).get(0));
        Photos photos = pharmacyService.getPhotoPharmacy(idPharmacy);
        UserOfPharmacy admin = userService.getAdminPharmacy(pharmacy.getId()).get(0);
        model.addAttribute("idAdmin", admin.getId());
        model.addAttribute("selectAdmin", admin.getFio());
        model.addAttribute("photos", pharmacyService.getPhotoPharmacy(idPharmacy));
        model.addAttribute("admin", userService.getAdminPharmacy(idPharmacy).get(0));
        Workers workers = userService.getWorkerId(userID);
        model.addAttribute("worker", workers);
        model.addAttribute("user", userService.get_id_user(userID));
        PharmacyManage pharmacyManage = pharmacyService.getPharmacyManageAll(idPharmacy).get(0);
        model.addAttribute("pharmacyManage", pharmacyManage);

        return "pharmacyView";
    }


    @GetMapping("/workerCreate")
    public String showWorkerCreatePage(@RequestParam(defaultValue = "false") boolean addRole,
                                       @RequestParam(defaultValue = "false") boolean addUser,
                                       @RequestParam(defaultValue = "false") boolean createRole,
                                       @RequestParam(defaultValue = "") String fio,
                                       @RequestParam(required = false) BigDecimal idPharmacy,
                                       @RequestParam(required = false) BigDecimal idUser,
                                       @RequestParam(required = false) BigDecimal idRole,
                                       @RequestParam(defaultValue = "") String selectRole,
                                       @RequestParam(required = false) BigDecimal userID,
                                       @RequestParam(defaultValue = "") String selectUser, Model model) {
        model.addAttribute("users", userService.searchUsers(fio));
        model.addAttribute("roles", userService.getRoles());
        model.addAttribute("addRole", addRole);
        model.addAttribute("addUser", addUser);
        model.addAttribute("createRole", createRole);
        model.addAttribute("idUser", idUser);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("idRole", idRole);
        if (idPharmacy != null && idPharmacy.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("pharmacy", pharmacyService.getPharmacyById(idPharmacy).get(0));
        }
        model.addAttribute("selectRole", selectRole);
        model.addAttribute("selectUser", selectUser);
        return "workersCreate";
    }

    @PostMapping("/workerCreate/searchUsers")
    public String showWorkerCreatePageTakeUsersSearchUsers(@RequestParam(defaultValue = "false") boolean addRole,
                                                @RequestParam String searchUser,
                                       @RequestParam(defaultValue = "true") boolean addUser,
                                                           @RequestParam(required = false) BigDecimal userID,
                                                           @RequestParam(required = false) BigDecimal idPharmacy,
                                       @RequestParam(defaultValue = "") String fio, Model model) {
        model.addAttribute("users", userService.searchUsers(searchUser));
        if (idPharmacy != null && idPharmacy.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("pharmacy", pharmacyService.getPharmacyById(idPharmacy).get(0));
        }
        model.addAttribute("addRole", addRole);
        model.addAttribute("addUser", addUser);
        model.addAttribute("user", userService.get_id_user(userID));
        return "workersCreate";
    }

    @PostMapping("/workerCreate")
    public String showWorkerCreatePage(@RequestParam(defaultValue = "false") boolean addRole,
                                                @RequestParam(defaultValue = "false") boolean addUser,
                                       @RequestParam(required = false) BigDecimal idPharmacy,
                                       @RequestParam BigDecimal id_of_user,
                                       @RequestParam BigDecimal salary,
                                       @RequestParam String education,
                                       @RequestParam BigDecimal id_of_role,
                                       @RequestParam String INN,
                                       @RequestParam(required = false) BigDecimal userID,
                                       @RequestParam String snils, Model model) {
        Workers worker = Workers.builder()
                .id_of_user(id_of_user)
                .salary(salary)
                .education(education)
                .id_of_role(id_of_role)
                .inn(INN)
                .snils(snils)
                .build();
        Workers workers = userService.workerCreate(worker);
        if (workers.getErrorMessage() != null && id_of_role.equals(3)){
            workers.setErrorMessage(pharmacyService.createNewManage(idPharmacy, workers.getId()));
        }
        if (idPharmacy != null && idPharmacy.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("pharmacy", pharmacyService.getPharmacyById(idPharmacy).get(0));
        }
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("addRole", addRole);
        model.addAttribute("addUser", addUser);
        model.addAttribute("InfoError", workers.getErrorMessage() == null ? "Работник создан успешно" : workers.getErrorMessage());
        model.addAttribute("InfoErrorShow", true);
        return "workersCreate";
    }

    @PostMapping("/workerCreate/createRole")
    public String showWorkerCreatePageCreateRole(@RequestParam(defaultValue = "true") boolean addRole,
                                                 @RequestParam(defaultValue = "false") boolean createRole,
                                                @RequestParam(defaultValue = "true") boolean addUser,
                                                 @RequestParam(required = false) BigDecimal userID,
                                                @RequestParam(defaultValue = "") String roleName, Model model) {
        Role role = Role.builder().name(roleName).build();
        userService.roleCreate(role);
        model.addAttribute("addRole", addRole);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("createRole", createRole);
        model.addAttribute("addUser", addUser);
        return "workersCreate";
    }

    @GetMapping("/medicinePlace")
    public String showMedicinePlacePage(@RequestParam(defaultValue = "0") int pagin,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "false") boolean addBrand,
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
                                        @RequestParam(required = false) BigDecimal userID,
                                        @RequestParam(defaultValue = "false") boolean edit,
                                        @RequestParam(required = false) BigDecimal idMedicine,
                                        @RequestParam(required = false) BigDecimal photoID,
                                        @RequestParam(required = false) String InfoError,
                                        @RequestParam(defaultValue = "false") boolean needScroll,
                                        Model model) {
        if (page < 1) {
            page = 1;
            pagin = 0;
        }
        List<Medicine> medicines = medicineService.getAllMedicine()
                .stream()
                .limit(15 + pagin)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("addBrand", addBrand);
        model.addAttribute("needScroll", needScroll);
        model.addAttribute("addDosageForm", addDosageForm);
        model.addAttribute("addManufacturer", addManufacturer);
        model.addAttribute("addCountry", addCountry);
        model.addAttribute("InfoError", InfoError);
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
        model.addAttribute("manufacturers", medicineService.getAllManufacturers());
        model.addAttribute("brands", medicineService.getAllBrands());
        model.addAttribute("packageTypes", medicineService.getAllPackageTypes());
        model.addAttribute("dosageForms", medicineService.getAllDosageForms());
        model.addAttribute("therapeuticGroups", medicineService.getAllTherapeuticGroups());
        model.addAttribute("pharmacologicalGroups", medicineService.getAllPharmacologicalGroups());
        model.addAttribute("countries", medicineService.getAllCountries());

        model.addAttribute("medicines", medicines);
        model.addAttribute("pagin", pagin);
        model.addAttribute("photos", photos);
        model.addAttribute("page", page);
        return "medicinePlace";
    }

    @PostMapping("/medicinePlace")
    public String showMedicineSortPage(@RequestParam(defaultValue = "0") int pagin,
                                       @RequestParam(defaultValue = "1") int page,
                                       @ModelAttribute Medicine medicine,
                                       @RequestParam(required = false) BigDecimal userID,
                                       Model model) {
        if (page < 1) {
            page = 1;
            pagin = 0;
        }
        List<Medicine> medicines;
        medicines = medicineService.sortMedicine(medicine)
                .stream()
                .limit(15 + pagin)
                .collect(Collectors.toList());
        if (medicines.isEmpty()) {
            medicines = medicineService.getAllMedicine().stream()
                    .limit(15 + pagin)
                    .collect(Collectors.toList());
            model.addAttribute("InfoError", "Ничего не найдено");
            model.addAttribute("InfoErrorShow", true);
        }
        model.addAttribute("user", userService.get_id_user(userID));
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("medicines", medicines);
        model.addAttribute("photos", photos);
        model.addAttribute("pagin", pagin);
        model.addAttribute("page", page);
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
                                         @RequestParam(required = false) BigDecimal userID,
                                         @RequestParam(defaultValue = "false") boolean edit,
                                         @RequestParam(required = false) BigDecimal idMedicine,
                                         @RequestParam(required = false) BigDecimal photoID,
                                         @RequestParam(required = false) String InfoError,
                                         Model model, HttpSession session) {
        System.out.println("Received brand_id: " + brand_id);
        System.out.println("Received selectBrand: " + selectBrand);
        model.addAttribute("addBrand", addBrand);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("addDosageForm", addDosageForm);
        model.addAttribute("addManufacturer", addManufacturer);
        model.addAttribute("addCountry", addCountry);
        model.addAttribute("InfoError", InfoError);
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
        model.addAttribute("edit", edit);
        BigDecimal p_ID = (BigDecimal) session.getAttribute("photoID");
        if (pharmacyService.getPhotosPharmacy().stream().filter(photos -> photos.getId().equals(p_ID) && photos.getEntityId() != null)
                .findFirst().orElse(null) != null) {
            photoID = null;
        }
        else {
            photoID = p_ID == null ? photoID : p_ID;
        }
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = medicineService.getPhotoMed(idMedicine);
            if (photoID == null)
                model.addAttribute("photos", photos);
        }
        if (photoID != null && photoID.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = userService.getPhoto(photoID) == null ? new Photos() : userService.getPhoto(photoID);
            model.addAttribute("photos", photos);
        }
        if (idMedicine != null) {
            model.addAttribute("photos", medicineService.getPhotoMed(idMedicine));
        }
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }
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
    public String medicineCreatePage(@ModelAttribute Medicine medicine,
                                     @RequestParam(required = false) BigDecimal userID,
                                     @RequestParam(required = false) BigDecimal photoID, HttpSession session, Model model) {
        Medicine medicine1 = medicineService.createMedicine(medicine);
        model.addAttribute("InfoErrorShow", true);
        photoID = (BigDecimal) session.getAttribute("photoID");
        model.addAttribute("user", userService.get_id_user(userID));
        if (photoID != null && photoID.compareTo(BigDecimal.ZERO) > 0){
            medicineService.updatePhoto(medicine1.getId(), photoID);
        }
        if (medicine1.getErrorMessage() != null) {
            model.addAttribute("InfoError", medicine1.getErrorMessage());
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @GetMapping("/medicineEdit")
    public String showMedicineEditPage(@RequestParam(defaultValue = "false") boolean addBrand,
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
                                       @RequestParam(required = false) BigDecimal userID,
                                         @RequestParam(defaultValue = "false") boolean edit,
                                         @RequestParam(required = false) BigDecimal idMedicine,
                                         @RequestParam(required = false) BigDecimal photoID,
                                         @RequestParam(required = false) boolean delete,
                                         @RequestParam(required = false) boolean deleteMedicine,
                                         Model model, HttpSession session) {
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
        model.addAttribute("edit", edit);
        model.addAttribute("delete", delete);
        model.addAttribute("user", userService.get_id_user(userID));
        if (deleteMedicine && idMedicine != null){
            Medicine medicine = medicineService.deleteMedicine(idMedicine);
            model.addAttribute("InfoError", medicine.getErrorMessage());
            model.addAttribute("InfoErrorShow", true);
        }
        BigDecimal p_ID = (BigDecimal) session.getAttribute("photoID");
        if (pharmacyService.getPhotosPharmacy().stream().filter(photos -> photos.getId().equals(p_ID) && photos.getEntityId() != null)
                .findFirst().orElse(null) != null) {
            photoID = null;
        }
        else {
            photoID = p_ID == null ? photoID : p_ID;
        }
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = medicineService.getPhotoMed(idMedicine);
            if (photoID == null)
                model.addAttribute("photos", photos);
        }
        if (photoID != null && photoID.compareTo(BigDecimal.ZERO) > 0){
            Photos photos = userService.getPhoto(photoID) == null ? new Photos() : userService.getPhoto(photoID);
            model.addAttribute("photos", photos);
        }
        if (idMedicine != null) {
            model.addAttribute("photos", medicineService.getPhotoMed(idMedicine));
        }
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }
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


        return "medicineEdit";
    }

    @PostMapping("/medicineEdit")
    public String medicineEditPage(@ModelAttribute Medicine medicine,
                                   @RequestParam(required = false) BigDecimal userID,
                                     @RequestParam(required = false) BigDecimal photoID, HttpSession session, Model model) {
        Medicine medicine1 = medicineService.editMedicine(medicine);
        model.addAttribute("InfoErrorShow", true);
        model.addAttribute("user", userService.get_id_user(userID));
        if (medicine1.getErrorMessage() != null) {
            model.addAttribute("InfoError", medicine1.getErrorMessage());
        }
        else {
            model.addAttribute("InfoError", "Объект редактирован успешно!");
        }
        return "medicineCreate";
    }

    @PostMapping("/medicineCreate/dosageFormCreate/addInjectionMethods")
    public String showInjectionMethodsPage(@RequestParam(defaultValue = "true") boolean addInjectionMethods,
                                           @RequestParam(required = false) BigDecimal userID,
                                    @RequestParam String name, Model model) {
        model.addAttribute("addInjectionMethods", addInjectionMethods);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("InjectionMethods", medicineService.findAdministrationRoute(name));
        return "medicineCreate";
    }

    @PostMapping("/addBrand")
    public String showFindBrandPage(@RequestParam(defaultValue = "true") boolean addBrand,
                                    @RequestParam String brandName,
                                    @RequestParam String source,
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
                                    @RequestParam(required = false) BigDecimal userID,
                                    @RequestParam(defaultValue = "false") boolean edit,
                                    @RequestParam(required = false) BigDecimal idMedicine,
                                    @RequestParam(required = false) BigDecimal photoID,
                                    @RequestParam(required = false) boolean delete,
                                    @RequestParam(required = false) boolean deleteMedicine,
                                    Model model) {
        model.addAttribute("addBrand", addBrand);
        Brand brand = Brand.builder().name(brandName).build();
        model.addAttribute("brands", medicineService.findBrands(brand.getName()));
        model.addAttribute("pagin", 0);
        model.addAttribute("page", 0);
        List<Medicine> medicines = medicineService.getAllMedicine().stream()
                .limit(15)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("medicines", medicines);
        model.addAttribute("photos", photos);
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
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }
        if ("medicineEdit".equals(source)) {
            return "medicineEdit";
        }
        if ("medicinePlace".equals(source)) {
            return "medicinePlace";
        }
        return "medicineCreate";
    }

    @GetMapping("/medicineCreate/brandCreate")
    public String showBrandCreatePage(@RequestParam(defaultValue = "true") boolean brandCreate,
                                      @RequestParam(required = false) BigDecimal userID,
                                      @RequestParam(defaultValue = "false") boolean edit, Model model) {
        model.addAttribute("brandCreate", brandCreate);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("edit", edit);
        return "medicineCreate";
    }

    @PostMapping("/medicineCreate/brandCreate")
    public String BrandCreatePage(@RequestParam(defaultValue = "true") boolean createCountry,
                                  @RequestParam(required = false) BigDecimal userID,
                              @ModelAttribute Brand brand, Model model) {
        String error = medicineService.createBrands(brand);
        model.addAttribute("createCountry", createCountry);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @PostMapping("/addAtc")
    public String showFindAtcCodePage(@RequestParam(defaultValue = "true") boolean addAtc,
                                    @RequestParam String atcCode,
                                      @RequestParam String source,
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
                                      @RequestParam(required = false) BigDecimal userID,
                                      @RequestParam(defaultValue = "false") boolean edit,
                                      @RequestParam(required = false) BigDecimal idMedicine,
                                      @RequestParam(required = false) BigDecimal photoID,
                                      @RequestParam(required = false) boolean delete,
                                      @RequestParam(required = false) boolean deleteMedicine,
                                      Model model) {
        model.addAttribute("addAtc", addAtc);
        AtcClassification atcClassification = AtcClassification.builder().code(atcCode).build();
        model.addAttribute("atcList", medicineService.findAtcCode(atcClassification.getCode()));
        model.addAttribute("pagin", 0);
        model.addAttribute("page", 0);
        model.addAttribute("user", userService.get_id_user(userID));
        List<Medicine> medicines = medicineService.getAllMedicine().stream()
                .limit(15)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("medicines", medicines);
        model.addAttribute("photos", photos);
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
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }
        if ("medicineEdit".equals(source)) {
            return "medicineEdit";
        }
        if ("medicinePlace".equals(source)) {
            return "medicinePlace";
        }
        return "medicineCreate";
    }

    @GetMapping("/medicineCreate/prescriptionFormCreate")
    public String showPrescriptionFormCreatePage(@RequestParam(defaultValue = "true") boolean createPrescriptionForm,
                                                 @RequestParam(required = false) BigDecimal userID,
                                                  Model model) {
        model.addAttribute("createPrescriptionForm", createPrescriptionForm);
        model.addAttribute("user", userService.get_id_user(userID));
        return "medicineCreate";
    }

    @PostMapping("/addPrescriptionForm")
    public String showPrescriptionFormsPage(@RequestParam(defaultValue = "true") boolean addPrescriptionForm,
                                                 @RequestParam String formName,
                                            @RequestParam String source,
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
                                            @RequestParam(required = false) BigDecimal userID,
                                            @RequestParam(defaultValue = "false") boolean edit,
                                            @RequestParam(required = false) BigDecimal idMedicine,
                                            @RequestParam(required = false) BigDecimal photoID,
                                            @RequestParam(required = false) boolean delete,
                                            @RequestParam(required = false) boolean deleteMedicine,
                                                 Model model) {
        model.addAttribute("addPrescriptionForm", addPrescriptionForm);
        model.addAttribute("prescriptionForms", medicineService.findPrescriptionForm(formName));
        model.addAttribute("pagin", 0);
        model.addAttribute("page", 0);
        List<Medicine> medicines = medicineService.getAllMedicine().stream()
                .limit(15)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("medicines", medicines);
        model.addAttribute("photos", photos);
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
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }
        if ("medicineEdit".equals(source)) {
            return "medicineEdit";
        }
        if ("medicinePlace".equals(source)) {
            return "medicinePlace";
        }
        return "medicineCreate";
    }

    @PostMapping("/medicineCreate/prescriptionFormCreate")
    public String PrescriptionFormCreatePage(@RequestParam(defaultValue = "true") boolean createPrescriptionForm,
                                             @RequestParam(required = false) BigDecimal userID,
                                                 @ModelAttribute PrescriptionForm prescriptionForm, Model model) {
        model.addAttribute("createPrescriptionForm", createPrescriptionForm);
        String error = medicineService.createPrescriptionForm(prescriptionForm);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }


    @GetMapping("/medicineCreate/packageTypeCreate")
    public String showPackageTypeCreatePage(@RequestParam(defaultValue = "true") boolean createPackageType,
                                            @RequestParam(required = false) BigDecimal userID,
                                                 Model model) {
        model.addAttribute("createPackageType", createPackageType);
        model.addAttribute("user", userService.get_id_user(userID));
        return "medicineCreate";
    }

    @PostMapping("/addPackageType")
    public String findPackageTypePage(@RequestParam(defaultValue = "true") boolean addPackageType,
                                            @RequestParam String packageTypeName, @RequestParam String source,
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
                                      @RequestParam(required = false) BigDecimal userID,
                                      @RequestParam(defaultValue = "false") boolean edit,
                                      @RequestParam(required = false) BigDecimal idMedicine,
                                      @RequestParam(required = false) BigDecimal photoID,
                                      @RequestParam(required = false) boolean delete,
                                      @RequestParam(required = false) boolean deleteMedicine,
                                      Model model) {
        model.addAttribute("addPackageType", addPackageType);
        model.addAttribute("packageTypes", medicineService.findTypePackaging(packageTypeName));
        model.addAttribute("pagin", 0);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("page", 0);
        List<Medicine> medicines = medicineService.getAllMedicine().stream()
                .limit(15)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("medicines", medicines);
        model.addAttribute("photos", photos);
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
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }
        if ("medicineEdit".equals(source)) {
            return "medicineEdit";
        }
        if ("medicinePlace".equals(source)) {
            return "medicinePlace";
        }
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/packageTypeCreate")
    public String showPackageTypeCreatePage(@RequestParam(defaultValue = "true") boolean createPackageType,
                                            @RequestParam(required = false) BigDecimal userID,
                                            @ModelAttribute TypePackaging packageType, Model model) {
        model.addAttribute("createPackageType", createPackageType);
        String error = medicineService.createTypePackaging(packageType);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }


    @PostMapping("/addPharmacologicalGroup")
    public String findPharmacologicalGroupCreatePage(@RequestParam(defaultValue = "true") boolean addPharmacologicalGroup,
                                                     @RequestParam String groupName, @RequestParam String source,
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
                                                     @RequestParam(defaultValue = "false") boolean edit,
                                                     @RequestParam(required = false) BigDecimal userID,
                                                     @RequestParam(required = false) BigDecimal idMedicine,
                                                     @RequestParam(required = false) BigDecimal photoID,
                                                     @RequestParam(required = false) boolean delete,
                                                     @RequestParam(required = false) boolean deleteMedicine,
                                            Model model) {
        model.addAttribute("addPharmacologicalGroup", addPharmacologicalGroup);
        model.addAttribute("pharmacologicalGroups", medicineService.findPharmacologicalGroup(groupName));
        model.addAttribute("pagin", 0);
        model.addAttribute("page", 0);
        List<Medicine> medicines = medicineService.getAllMedicine().stream()
                .limit(15)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("medicines", medicines);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("photos", photos);
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
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }
        if ("medicineEdit".equals(source)) {
            return "medicineEdit";
        }
        if ("medicinePlace".equals(source)) {
            return "medicinePlace";
        }
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/pharmacologicalGroupCreate")
    public String showPharmacologicalGroupCreatePage(@RequestParam(defaultValue = "true") boolean pharmacologicalGroupCreate,
                                                     @RequestParam(required = false) BigDecimal userID,
                                                     Model model) {
        model.addAttribute("pharmacologicalGroupCreate", pharmacologicalGroupCreate);
        model.addAttribute("user", userService.get_id_user(userID));
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/pharmacologicalGroupCreate")
    public String PharmacologicalGroupCreatePage(@RequestParam(defaultValue = "true") boolean pharmacologicalGroupCreate,
                                                 @RequestParam(required = false) BigDecimal userID,
                                                     @ModelAttribute PharmacologicalGroup pharmacologicalGroup,
                                                 Model model) {
        model.addAttribute("pharmacologicalGroupCreate", pharmacologicalGroupCreate);
        model.addAttribute("user", userService.get_id_user(userID));
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
                                                 @RequestParam(required = false) BigDecimal userID,
                                                     Model model) {
        model.addAttribute("therapeuticGroupCreate", therapeuticGroupCreate);
        model.addAttribute("user", userService.get_id_user(userID));
        return "medicineCreate";
    }

    @PostMapping("/addTherapeuticGroup")
    public String TherapeuticGroupCreatePage(@RequestParam(defaultValue = "true") boolean addTherapeuticGroup,
                                             @RequestParam String groupName, @RequestParam String source,
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
                                             @RequestParam(required = false) BigDecimal userID,
                                             @RequestParam(defaultValue = "false") boolean edit,
                                             @RequestParam(required = false) BigDecimal idMedicine,
                                             @RequestParam(required = false) BigDecimal photoID,
                                             @RequestParam(required = false) boolean delete,
                                             @RequestParam(required = false) boolean deleteMedicine,
                                             Model model) {
        model.addAttribute("addTherapeuticGroup", addTherapeuticGroup);
        model.addAttribute("therapeuticGroups", medicineService.findTherapeuticGroup(groupName));
        model.addAttribute("pagin", 0);
        model.addAttribute("page", 0);
        model.addAttribute("user", userService.get_id_user(userID));
        List<Medicine> medicines = medicineService.getAllMedicine().stream()
                .limit(15)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("medicines", medicines);
        model.addAttribute("photos", photos);
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
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }

        if ("medicineEdit".equals(source)) {
            return "medicineEdit";
        }
        if ("medicinePlace".equals(source)) {
            return "medicinePlace";
        }
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/therapeuticGroupCreate")
    public String TherapeuticGroupCreatePage(@RequestParam(defaultValue = "true") boolean therapeuticGroupCreate,
                                             @RequestParam(required = false) BigDecimal userID,
                                             @ModelAttribute TherapeuticGroup therapeuticGroup,
                                                 Model model) {
        model.addAttribute("therapeuticGroupCreate", therapeuticGroupCreate);
        String error = medicineService.createTherapeuticGroup(therapeuticGroup);
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("InfoErrorShow", true);
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @PostMapping("/addManufacturer")
    public String showManufacturerAddPage(@RequestParam(defaultValue = "true") boolean addManufacturer,
                                          @RequestParam String manufacturerName, @RequestParam String source,
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
                                          @RequestParam(required = false) BigDecimal userID,
                                          @RequestParam(defaultValue = "false") boolean edit,
                                          @RequestParam(required = false) BigDecimal idMedicine,
                                          @RequestParam(required = false) BigDecimal photoID,
                                          @RequestParam(required = false) boolean delete,
                                          @RequestParam(required = false) boolean deleteMedicine,
                                             Model model) {
        model.addAttribute("addManufacturer", addManufacturer);
        model.addAttribute("manufacturers", medicineService.findManufucture(manufacturerName));
        model.addAttribute("pagin", 0);
        model.addAttribute("page", 0);
        model.addAttribute("user", userService.get_id_user(userID));
        List<Medicine> medicines = medicineService.getAllMedicine().stream()
                .limit(15)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("medicines", medicines);
        model.addAttribute("photos", photos);
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
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }
        if ("medicineEdit".equals(source)) {
            return "medicineEdit";
        }
        if ("medicinePlace".equals(source)) {
            return "medicinePlace";
        }
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/manufacturerCreate")
    public String showManufacturerCreatePage(@RequestParam(defaultValue = "true") boolean manufacturerCreate,
                                             @RequestParam(required = false) BigDecimal userID,
                                                 Model model) {
        model.addAttribute("manufacturerCreate", manufacturerCreate);
        model.addAttribute("user", userService.get_id_user(userID));
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/manufacturerCreate")
    public String ManufacturerCreatePage(@RequestParam(defaultValue = "true") boolean manufacturerCreate,
                                         @RequestParam(required = false) BigDecimal userID,
                                         @ModelAttribute Manufacturer manufacturer,
                                             Model model) {
        model.addAttribute("manufacturerCreate", manufacturerCreate);
        model.addAttribute("user", userService.get_id_user(userID));
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
                                    @RequestParam(required = false) BigDecimal userID,
                                             Model model) {
        model.addAttribute("atcCreate", atcCreate);
        model.addAttribute("user", userService.get_id_user(userID));
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/atcCreate")
    public String AtcCodeCreatePage(@RequestParam(defaultValue = "true") boolean atcCreate,
                                    @ModelAttribute AtcClassification atcClassification,
                                    @RequestParam(required = false) BigDecimal userID,
                                    Model model) {
        String error = medicineService.createAtcCode(atcClassification);
        model.addAttribute("user", userService.get_id_user(userID));
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

    @PostMapping("/addDosageForm")
    public String findDosageFormPage(@RequestParam(defaultValue = "true") boolean addDosageForm,
                                    @RequestParam String dosageFormName, @RequestParam String source,
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
                                     @RequestParam(defaultValue = "false") boolean edit,
                                     @RequestParam(required = false) BigDecimal userID,
                                     @RequestParam(required = false) BigDecimal idMedicine,
                                     @RequestParam(required = false) BigDecimal photoID,
                                     @RequestParam(required = false) boolean delete,
                                     @RequestParam(required = false) boolean deleteMedicine,
                                    Model model) {
        model.addAttribute("dosageForms", medicineService.findDosageForm(dosageFormName));
        model.addAttribute("addDosageForm", addDosageForm);
        model.addAttribute("pagin", 0);
        model.addAttribute("page", 0);
        model.addAttribute("user", userService.get_id_user(userID));
        List<Medicine> medicines = medicineService.getAllMedicine().stream()
                .limit(15)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("medicines", medicines);
        model.addAttribute("photos", photos);
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
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }
        if ("medicineEdit".equals(source)) {
            return "medicineEdit";
        }
        if ("medicinePlace".equals(source)) {
            return "medicinePlace";
        }
        return "medicineCreate";
    }

    @GetMapping("medicineCreate/dosageFormCreate")
    public String showDosageFormCreatePage(@RequestParam(defaultValue = "true") boolean dosageFormCreate,
                                           @RequestParam(defaultValue = "false") boolean addInjectionMethods,
                                           @RequestParam(required = false) BigDecimal InjectionMethods_id,
                                           @RequestParam(required = false) BigDecimal userID,
                                           @RequestParam(defaultValue = "") String selectInjectionMethods,
                                    Model model) {
        model.addAttribute("dosageFormCreate", dosageFormCreate);
        model.addAttribute("InjectionMethods_id", InjectionMethods_id);
        model.addAttribute("user", userService.get_id_user(userID));
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
                                   @RequestParam(required = false) BigDecimal userID,
                                                   Model model) {
        model.addAttribute("addDosageForm", addDosageForm);
        String error = medicineService.createDosageForm(dosageForm);
        model.addAttribute("InfoErrorShow", true);
        model.addAttribute("user", userService.get_id_user(userID));
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
                                                       @RequestParam(required = false) BigDecimal userID,
                                      Model model) {
        model.addAttribute("methodOfAdministrationCreate", methodOfAdministrationCreate);
        model.addAttribute("user", userService.get_id_user(userID));
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/dosageFormCreate/methodOfAdministrationCreate")
    public String MethodOfAdministrationCreatePage(@RequestParam(defaultValue = "true") boolean methodOfAdministrationCreate,
                                                   @RequestParam(required = false) BigDecimal userID,
                                                       @ModelAttribute AdministrationRoute administrationRoute,
                                                       Model model) {
        model.addAttribute("methodOfAdministrationCreate", methodOfAdministrationCreate);
        model.addAttribute("user", userService.get_id_user(userID));
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
                                  @RequestParam(required = false) BigDecimal userID,
                                           Model model) {
        model.addAttribute("createCountry", createCountry);
        model.addAttribute("user", userService.get_id_user(userID));
        return "medicineCreate";
    }

    @PostMapping("medicineCreate/createCountry")
    public String CountryPage(@RequestParam(defaultValue = "true") boolean createCountry,
                              @RequestParam(required = false) BigDecimal userID,
                                  @RequestParam String countryName, Model model) {
        Country country = Country.builder().name(countryName).build();
        String error = medicineService.createCountry(country);
        model.addAttribute("createCountry", createCountry);
        model.addAttribute("InfoErrorShow", true);
        model.addAttribute("user", userService.get_id_user(userID));
        if (error != null) {
            model.addAttribute("InfoError", error);
        }
        else {
            model.addAttribute("InfoError", "Объект успешно создан!");
        }
        return "medicineCreate";
    }

    @PostMapping("/addCountry")
    public String showCountriesPage(@RequestParam(defaultValue = "true") boolean addCountry,
                                    @RequestParam String countryName, @RequestParam String source,
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
                                    @RequestParam(required = false) BigDecimal userID,
                                    @RequestParam(defaultValue = "false") boolean edit,
                                    @RequestParam(required = false) BigDecimal idMedicine,
                                    @RequestParam(required = false) BigDecimal photoID,
                                    @RequestParam(required = false) boolean delete,
                                    @RequestParam(required = false) boolean deleteMedicine,
                                    Model model) {
        model.addAttribute("countries", medicineService.findCountry(countryName));
        model.addAttribute("addCountry", addCountry);
        model.addAttribute("pagin", 0);
        model.addAttribute("page", 0);
        List<Medicine> medicines = medicineService.getAllMedicine().stream()
                .limit(15)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("medicines", medicines);
        model.addAttribute("photos", photos);
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
        if (idMedicine != null && idMedicine.compareTo(BigDecimal.ZERO) > 0){
            model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        }
        if ("medicineEdit".equals(source)) {
            return "medicineEdit";
        }
        if ("medicinePlace".equals(source)) {
            return "medicinePlace";
        }
        return "medicineCreate";
    }

    @GetMapping("medicineView")
    public String showMedicineView(BigDecimal idMedicine, @RequestParam(required = false) BigDecimal userID, Model model) {
        model.addAttribute("medicine", medicineService.getMedicineByID(idMedicine).get(0));
        model.addAttribute("photos", medicineService.getPhotoMed(idMedicine));
        UserOfPharmacy user = userService.get_id_user(userID);
        model.addAttribute("user", userService.get_id_user(userID));
        return "medicineView";
    }

    @PostMapping("/uploadPhotoMedicine")
    public String uploadPhotoMedicine(@RequestParam("photo") MultipartFile file, @RequestParam(required = false) BigDecimal userID,
                              @RequestParam(required = false) BigDecimal medicineId, @RequestParam(required = false) String source,
                                      Model model,
                              HttpSession session) {
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
        String fileName = file.getOriginalFilename();
        model.addAttribute("user", userService.get_id_user(userID));
        try {
            File destination = new File(uploadDir + fileName);
            file.transferTo(destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String photoUrl = "/images/" + fileName;

        Photos photos = medicineService.setPhotoMed(medicineId, photoUrl);
        session.setAttribute("photoID", photos.getId());

        Medicine medicine = new Medicine();
        if (medicineId != null)
            medicine = medicineService.getMedicineByID(medicineId).get(0);

        String redirectUrl = UriComponentsBuilder.fromPath("/api/pharmacy/" + source)
                .queryParam("edit", true)
                .queryParam("idMedicine", medicine.getId())
                .queryParam("brand_id", medicine.getBrandId())
                .queryParam("selectBrand", medicine.getBrandName())
                .queryParam("atc_id", medicine.getAtcId())
                .queryParam("selectAtc", medicine.getAtc_code())
                .queryParam("manufacturer_id", medicine.getManufacturerId())
                .queryParam("selectManufacturer", medicine.getManufacturer_name())
                .queryParam("country_id", medicine.getCountryId())
                .queryParam("selectCountry", medicine.getCountryName())
                .queryParam("pharmacological_group_id", medicine.getPharmacologicalGroupId())
                .queryParam("selectPharmacologicalGroup", medicine.getPharmacological_group_name())
                .queryParam("therapeutic_group_id", medicine.getTherapeuticGroupId())
                .queryParam("selectTherapeuticGroup", medicine.getTherapeutic_group_name())
                .queryParam("prescription_form_id", medicine.getPrescriptionFormId())
                .queryParam("selectPrescriptionForm", medicine.getPrescription_form_name())
                .queryParam("package_type_id", medicine.getPackageTypeId())
                .queryParam("selectPackageType", medicine.getPackage_type_name())
                .queryParam("dosage_form_id", medicine.getDosageFormId())
                .queryParam("selectDosageForm", medicine.getDosageFormName())
                .toUriString();
        return "redirect:" + redirectUrl;
    }

    @PostMapping("/uploadPhotoPharmacy")
    public String uploadPhotoPharmacy(@RequestParam("photo") MultipartFile file, @RequestParam(required = false) BigDecimal userID,
                                      @RequestParam(required = false) BigDecimal pharmacyID,
                                      @RequestParam(required = false) String source, Model model,
                              HttpSession session) {
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
        String fileName = file.getOriginalFilename();
        model.addAttribute("user", userService.get_id_user(userID));
        try {
            File destination = new File(uploadDir + fileName);
            file.transferTo(destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String photoUrl = "/images/" + fileName;

        Photos photos = pharmacyService.setPhotoPharmacy(pharmacyID, photoUrl);
        session.setAttribute("photoID", photos.getId());
        Pharmacy pharmacy = new Pharmacy();
        if (pharmacyID != null)
            pharmacy = pharmacyService.getPharmacyById(pharmacyID).get(0);
        String redirectUrl = UriComponentsBuilder.fromPath("/api/pharmacy/" + source)
                .queryParam("edit", true)
                .queryParam("userID", userID)
                .queryParam("idPharmacy", pharmacy.getId())
                .queryParam("pharmacyID", pharmacy.getId())
                .queryParam("idAdmin", pharmacyID != null ?
                        userService.getAdminPharmacy(pharmacyID).get(0).getId() : null)
                .queryParam("idCity", pharmacy.getID_CITY())
                .queryParam("selectAdmin", pharmacyID != null ?
                        userService.getAdminPharmacy(pharmacyID).get(0).getFio() : null)
                .queryParam("selectCityRegion", pharmacy.getRegion())
                .queryParam("selectCity", pharmacy.getCityName())
                .queryParam("photoID", photos.getId())
                .toUriString();
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/userView")
    public String userView(@RequestParam(required = false) BigDecimal userID, @RequestParam(defaultValue = "false") Boolean exitUser,

            Model model,
            HttpSession session) {
        UserOfPharmacy user = userService.get_id_user(userID);
        model.addAttribute("user", user);
        model.addAttribute("exitUser", exitUser);
        return "userView";
    }

    @GetMapping("/userEdit")
    public String userEditShow(@RequestParam(required = false) BigDecimal userID, @RequestParam(defaultValue = "false") Boolean deleteUser, Model model){
        UserOfPharmacy user = userService.get_id_user(userID);
        model.addAttribute("user", user);
        model.addAttribute("deleteUser", deleteUser);
        return "userEdit";
    }

    @PostMapping("/userEdit")
    public String userEdit(@ModelAttribute UserOfPharmacy beforeUser, @RequestParam(required = false) BigDecimal userID, Model model){
        userService.updateUser(beforeUser);
        UserOfPharmacy afterUser = userService.get_id_user(beforeUser.getId());
        model.addAttribute("user", afterUser);
        model.addAttribute("InfoErrorShow", true);
        model.addAttribute("InfoError", "Объект редактирован");
        return "userEdit";
    }

    @GetMapping("/userEdit/deleteUser")
    public String userViewDelete(@RequestParam(required = false) BigDecimal userID,
                           Model model,
                           HttpSession session) {
        UserOfPharmacy user = userService.get_id_user(userID);
        model.addAttribute("user", user);
        return "entrace";
    }

    @GetMapping("/medicineView/order")
    public String getMedicineUser(@RequestParam BigDecimal userID, @RequestParam BigDecimal medicineID, Model model, HttpSession session) {
        orderService.createOrderMedicine(userID, medicineID);
        model.addAttribute("pagin", 0);
        model.addAttribute("page", 0);
        List<Medicine> medicines = medicineService.getAllMedicine().stream()
                .limit(15)
                .collect(Collectors.toList());
        List<Photos> photos = medicineService.getPhotosMed();
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("medicines", medicines);
        model.addAttribute("photos", photos);
        return "medicinePlace";
    }

    @GetMapping("/orders")
    public String getOrders(@RequestParam BigDecimal userID, @RequestParam(required = false) BigDecimal medicineID,
                            @RequestParam(required = false) BigDecimal idPharmacy,
                            @RequestParam(defaultValue = "1") BigDecimal value, @RequestParam(required = false) BigDecimal count,
                            @RequestParam(defaultValue = "false") boolean finalTrue, @RequestParam(required = false) BigDecimal orderID,
                            @RequestParam(required = false) BigDecimal orderMedicine, @RequestParam(required = false) boolean pay,
                            Model model, HttpSession session) {
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("value", value);
        model.addAttribute("idPharmacy", idPharmacy);
        UserOfPharmacy user = userService.get_id_user(userID);
        model.addAttribute("user", user);
        if (orderID != null)
            if (pay){
                orderService.updateOrderPay(orderID);
                model.addAttribute("orderID", orderID);
                model.addAttribute("InfoError", "Заказ принят!");
                model.addAttribute("InfoErrorShow", true);
                model.addAttribute("order", orderService.getOrderId(orderID));
            }
            model.addAttribute("orderID", orderID);
        if (finalTrue){
            Order order = orderService.getOrderId(orderID);
            if (order.getIdOfPharmacy() == null)
                orderID = orderService.endOrder(userID, idPharmacy);
            model.addAttribute("orderID", orderID);
            model.addAttribute("InfoError", "Заказ сформирован!");
            model.addAttribute("order", order);
        }
        if (idPharmacy != null)
            model.addAttribute("photos", pharmacyService.getPhotoPharmacy(idPharmacy));
        if (medicineID != null) {
            model.addAttribute("photos", medicineService.getPhotoMed(medicineID));
        }
        if (count != null){
            if (count.compareTo(BigDecimal.ONE) < 0) {
                orderService.deleteMedicineOrder(orderMedicine);
            }
            else{
                orderService.update_count(userID, medicineID, count);
            }
            model.addAttribute("count", count);
        }
        model.addAttribute("finalTrue", finalTrue);
        if (orderID != null){
            List<OrderMedicine> orderMedicines = orderService.getMedOrderPharmacy(orderID);
            model.addAttribute("orderMedicines", orderMedicines);
            List<Medicine> medicines = orderService.getMedOrdersIdOrder(orderID);
            model.addAttribute("medicines", medicines);
        }
        else {
            model.addAttribute("orderMedicines", orderService.getMedOrder(userID));
            List<Medicine> medicines = orderService.getMedOrders(userID);
            model.addAttribute("medicines", medicines);
        }
        return "orders";
    }

    @GetMapping("/ordersList")
    public String getOrdersList(@RequestParam BigDecimal userID, @RequestParam(required = false) BigDecimal medicineID,
                            @RequestParam(required = false) BigDecimal idPharmacy,
                            @RequestParam(defaultValue = "1") BigDecimal value, @RequestParam(required = false) BigDecimal count,
                            @RequestParam(defaultValue = "false") boolean finalTrue, @RequestParam(required = false) BigDecimal orderID,
                            @RequestParam(required = false) BigDecimal orderMedicine,
                            Model model, HttpSession session) {
        model.addAttribute("user", userService.get_id_user(userID));
        model.addAttribute("value", value);
        UserOfPharmacy user = userService.get_id_user(userID);
        model.addAttribute("user", user);
        List<Order> orders;
        if (user.getRole().compareTo(BigDecimal.valueOf(3)) == 0) {
            orders = orderService.getOrderAdmin(userID);
        } else {
            orders = orderService.getAllOrdersUser(userID);
        }
        model.addAttribute("ordersList", orders);
        if (idPharmacy != null){
            Photos photos = pharmacyService.getPhotoPharmacy(idPharmacy);
            model.addAttribute("photos", pharmacyService.getPhotoPharmacy(idPharmacy));
        }
        return "ordersList";
    }

}
