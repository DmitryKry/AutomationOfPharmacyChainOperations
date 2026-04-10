package ru.IS_122.AutomationOfPharmacyChainOperations.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.Pharmacy;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
public class MainControl {
    public List<Pharmacy> findAllPharmacies(){
        List<Pharmacy> pharmacies = new ArrayList<Pharmacy>();


        return pharmacies;
    }
}
