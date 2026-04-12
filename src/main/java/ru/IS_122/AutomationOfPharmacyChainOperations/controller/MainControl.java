package ru.IS_122.AutomationOfPharmacyChainOperations.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.Pharmacy;
import ru.IS_122.AutomationOfPharmacyChainOperations.service.PharmacyService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
public class MainControl {
    @Autowired
    private PharmacyService pharmacyService;

    @GetMapping("/admin")
    public List<Pharmacy> findAllPharmacies(){
        return pharmacyService.getAllPharmacies();
    }
}
