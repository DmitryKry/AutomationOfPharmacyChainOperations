package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.*;


import java.math.BigDecimal;
import java.math.BigInteger;
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
        String sql = "SELECT * FROM medicine_pkg.get_dosage_form_List()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DosageForm.class));
    }

    public List<DosageForm> findDosageForm(String p_name){
        String sql = "SELECT * FROM medicine_pkg.find_dosage_form(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DosageForm.class), p_name);
    }

    public String createDosageForm(DosageForm dosageForm){
        String sql = "call medicine_pkg.create_dosage_form(?, ?, ?, ?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, dosageForm.getName());
            cs.setBoolean(2, dosageForm.getIs_solid());
            cs.setBoolean(3, dosageForm.getIs_liquid());
            cs.setLong(4, dosageForm.getAdministration_route_id());
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.execute();
            return cs.getString(5);
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

    public String createAdministrationRoute(AdministrationRoute administrationRoute){
        String sql = "call medicine_pkg.create_administration_route(?, ?, ?)";
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, administrationRoute.getName());
            cs.setString(2, administrationRoute.getDescription());
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }

    public String createMedicine(Medicine medicine) {
        String sql = "call medicine_pkg.create_medicine(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;

            // ВЫХОДНОЙ ПАРАМЕТР ПЕРВЫЙ (p_error)
            cs.registerOutParameter(index++, Types.VARCHAR);  // 1 - p_error (OUT)

            // Входные параметры в том же порядке, что в процедуре после OUT
            cs.setString(index++, medicine.getName());                                    // 2 - p_name
            cs.setObject(index++, medicine.getUnitOfMeasure(), Types.VARCHAR);            // 29 - unit_of_measure
            cs.setObject(index++, medicine.getPrice(), Types.NUMERIC);                    // 30 - unit_of_measure
            cs.setString(index++, medicine.getInternationalName());                       // 3 - p_international_name
            cs.setString(index++, medicine.getDosageStrength());                          // 4 - p_dosage_strength
            cs.setObject(index++, medicine.getPackageQuantity(), Types.INTEGER);          // 5 - p_package_quantity
            cs.setObject(index++, medicine.getPrescriptionRequired(), Types.BOOLEAN);     // 6 - p_prescription_required

            cs.setString(index++, medicine.getRegistrationNumber());                      // 7 - p_registration_number
            cs.setObject(index++, medicine.getRegistrationDate(), Types.DATE);            // 8 - p_registration_date

            cs.setObject(index++, medicine.getShelfLifeMonths(), Types.INTEGER);          // 9 - p_shelf_life_months
            cs.setObject(index++, medicine.getStorageTemperatureMin(), Types.INTEGER);    // 10 - p_storage_temperature_min
            cs.setObject(index++, medicine.getStorageTemperatureMax(), Types.INTEGER);    // 11 - p_storage_temperature_max
            cs.setObject(index++, medicine.getRequiresRefrigeration(), Types.BOOLEAN);    // 12 - p_requires_refrigeration
            cs.setObject(index++, medicine.getLightSensitive(), Types.BOOLEAN);           // 13 - p_light_sensitive
            cs.setObject(index++, medicine.getRequiresDarkStorage(), Types.BOOLEAN);      // 14 - p_requires_dark_storage

            cs.setObject(index++, medicine.getIsActive(), Types.BOOLEAN);                 // 15 - p_is_active
            cs.setObject(index++, medicine.getIsVital(), Types.BOOLEAN);                  // 16 - p_is_vital
            cs.setObject(index++, medicine.getIsAvailable(), Types.BOOLEAN);              // 17 - p_is_available


            cs.setObject(index++, medicine.getPharmacologicalGroupId(), Types.NUMERIC);   // 19 - p_pharmacological_group_id
            cs.setObject(index++, medicine.getTherapeuticGroupId(), Types.NUMERIC);       // 20 - p_therapeutic_group_id
            cs.setObject(index++, medicine.getManufacturerId(), Types.NUMERIC);           // 21 - p_manufacturer_id
            cs.setObject(index++, medicine.getCountryId(), Types.NUMERIC);                // 22 - p_country_id
            cs.setObject(index++, medicine.getPrescriptionFormId(), Types.NUMERIC);       // 23 - p_prescription_form_id
            cs.setObject(index++, medicine.getDosageFormId(), Types.NUMERIC);             // 24 - p_dosage_form_id// 25 - p_unit_of_measure_id (ЕСТЬ В ВАШЕЙ ПРОЦЕДУРЕ)
            cs.setObject(index++, medicine.getBrandId(), Types.NUMERIC);                  // 26 - p_brand_id
            cs.setObject(index++, medicine.getAtcId(), Types.NUMERIC);                    // 27 - p_atc_id
            cs.setObject(index, medicine.getPackageTypeId(), Types.NUMERIC);            // 28 - p_package_type_id

            cs.execute();

            // Получаем результат из первого (и единственного) выходного параметра
            String error = cs.getString(1);
            return error;
        });

        return errorMessage;
    }

    public List<Medicine> getAllMedicine(){
        String sql = "SELECT * FROM medicine_pkg.getAllMedicines()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Medicine.class));
    }

    public List<Medicine> getMedicineByID(BigDecimal medicineID){
        String sql = "SELECT * FROM medicine_pkg.get_medicine(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Medicine.class), medicineID);
    }

    public void updatePhotoUrl(BigDecimal medicineId, String photoUrl){
        String sql = "UPDATE medicine_pkg.set_photo_medicine(?, ?)";
        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Medicine.class), medicineId, photoUrl);
    }
}
