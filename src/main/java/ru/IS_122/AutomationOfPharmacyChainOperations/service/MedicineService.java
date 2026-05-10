package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.*;


import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.Types;
import java.util.List;
@Service
public class MedicineService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Brand> getBrands(){
        String sql = "SELECT * FROM medicine_pkg.getBrands()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Brand.class));
    }

    public List<Brand> findBrands(String pName){
        String sql = "SELECT * FROM medicine_pkg.findBrands(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Brand.class), pName);
    }

    public String createBrands(Brand brand){
        String sql = "call medicine_pkg.createBrand(?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, brand.getName());
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.execute();
            return cs.getString(2);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }

    public List<AtcClassification> getAtcList(){
        String sql = "SELECT * FROM medicine_pkg.getAtcList()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AtcClassification.class));
    }

    public List<AtcClassification> findAtcCode(String p_code){
        String sql = "SELECT * FROM medicine_pkg.findAtcCode(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AtcClassification.class), p_code);
    }

    public String createAtcCode(AtcClassification atcClassification){
        String sql = "call medicine_pkg.createAtcCode(?, ?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, atcClassification.getCode());
            cs.setString(2, atcClassification.getDescription());
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }


    public List<Manufacturer> getManufuctureList(){
        String sql = "SELECT * FROM medicine_pkg.getManufuctureList()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Manufacturer.class));
    }

    public List<Manufacturer> findManufucture(String p_name){
        String sql = "SELECT * FROM medicine_pkg.findManufucture(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Manufacturer.class), p_name);
    }

    public String createManufacturer(Manufacturer manufacturer ){
        String sql = "call medicine_pkg.createManufucture(?, ?, ?, ?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, manufacturer.getName());
            cs.setString(2, manufacturer.getAddress());
            cs.setString(3, manufacturer.getPhone());
            cs.setString(4, manufacturer.getWebsite());
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.execute();
            return cs.getString(5);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }


    public List<Country> getCountryList(){
        String sql = "SELECT * FROM medicine_pkg.getСountriesList()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Country.class));
    }

    public List<Country> findCountry(String p_name){
        String sql = "SELECT * FROM medicine_pkg.findCountry(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Country.class), p_name);
    }

    public String createCountry(Country country){
        String sql = "call medicine_pkg.createCountry(?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, country.getName());
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.execute();
            return cs.getString(2);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }



    public List<PharmacologicalGroup> getPharmacologicalGroupList(){
        String sql = "SELECT * FROM medicine_pkg.get_pharmacological_group_List()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PharmacologicalGroup.class));
    }

    public List<PharmacologicalGroup> findPharmacologicalGroup(String p_name){
        String sql = "SELECT * FROM medicine_pkg.find_pharmacological_group(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PharmacologicalGroup.class), p_name);
    }

    public String createPharmacologicalGroup(PharmacologicalGroup pharmacologicalGroup){
        String sql = "call medicine_pkg.create_pharmacological_group(?, ?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, pharmacologicalGroup.getName());
            cs.setString(2, pharmacologicalGroup.getDescription());
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }


    public List<TherapeuticGroup> getTherapeuticGroupList(){
        String sql = "SELECT * FROM medicine_pkg.get_therapeutic_group_List()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TherapeuticGroup.class));
    }

    public List<TherapeuticGroup> findTherapeuticGroup(String p_name){
        String sql = "SELECT * FROM medicine_pkg.find_therapeutic_group(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TherapeuticGroup.class), p_name);
    }

    public String createTherapeuticGroup(TherapeuticGroup therapeuticGroup){
        String sql = "call medicine_pkg.create_therapeutic_group(?, ?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, therapeuticGroup.getName());
            cs.setString(2, therapeuticGroup.getDescription());
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }


    public List<PrescriptionForm> getPrescriptionFormList(){
        String sql = "SELECT * FROM medicine_pkg.get_prescription_form_List()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PrescriptionForm.class));
    }

    public List<PrescriptionForm> findPrescriptionForm(String p_name){
        String sql = "SELECT * FROM medicine_pkg.find_prescription_form(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PrescriptionForm.class), p_name);
    }

    public String createPrescriptionForm(PrescriptionForm prescriptionForm){
        String sql = "call medicine_pkg.create_prescription_form(?, ?, ?, ?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, prescriptionForm.getName());
            cs.setString(2, prescriptionForm.getDescription());
            cs.setBoolean(3, prescriptionForm.isRequires_special_blank());
            cs.setString(4, prescriptionForm.getStorage_restriction());
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.execute();
            return cs.getString(5);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }


    public List<TypePackaging> getTypePackagingList(){
        String sql = "SELECT * FROM medicine_pkg.get_type_packaging_List()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TypePackaging.class));
    }

    public List<TypePackaging> findTypePackaging(String p_name){
        String sql = "SELECT * FROM medicine_pkg.find_type_packaging(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TypePackaging.class), p_name);
    }

    public String createTypePackaging(TypePackaging typePackaging){
        String sql = "call medicine_pkg.create_type_packaging(?, ?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, typePackaging.getName());
            cs.setString(2, typePackaging.getDescription());
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }

    public List<DosageForm> getDosageFormList(){
        String sql = "SELECT * FROM medicine_pkg.get_type_packaging_List()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DosageForm.class));
    }

    public List<DosageForm> findDosageForm(String p_name){
        String sql = "SELECT * FROM medicine_pkg.find_type_packaging(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DosageForm.class), p_name);
    }

    public String createDosageForm(TypePackaging typePackaging){
        String sql = "call medicine_pkg.create_type_packaging(?, ?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, typePackaging.getName());
            cs.setString(2, typePackaging.getDescription());
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }


    public List<AdministrationRoute> getAdministrationRoute(){
        String sql = "SELECT * FROM medicine_pkg.get_administration_route_List()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AdministrationRoute.class));
    }

    public List<AdministrationRoute> findAdministrationRoute(String p_name){
        String sql = "SELECT * FROM medicine_pkg.find_administration_route(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AdministrationRoute.class), p_name);
    }

    public String createAdministrationRoute(TypePackaging typePackaging){
        String sql = "call medicine_pkg.create_administration_route(?, ?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, typePackaging.getName());
            cs.setString(2, typePackaging.getDescription());
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }
}
