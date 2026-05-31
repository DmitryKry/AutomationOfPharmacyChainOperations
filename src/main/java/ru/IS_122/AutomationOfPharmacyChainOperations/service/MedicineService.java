package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.*;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
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

    public Medicine createMedicine(Medicine medicine) {
        String sql = "call medicine_pkg.create_medicine(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;

            // ВЫХОДНОЙ ПАРАМЕТР ПЕРВЫЙ (p_error)
            cs.registerOutParameter(index++, Types.VARCHAR);  // 1 - p_error (OUT)
            cs.registerOutParameter(index++, Types.NUMERIC);  // 2 - id_medicine (OUT)

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
            BigDecimal idMedicine = cs.getBigDecimal(2);
            Medicine medicine1 = new Medicine();
            medicine1.setResult(error, idMedicine);
            return medicine1;
        });
    }

    public List<Medicine> getAllMedicine(){
        String sql = "SELECT * FROM medicine_pkg.getAllMedicines()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Medicine.class));
    }

    public List<Medicine> getMedicineByID(BigDecimal medicineID){
        String sql = "SELECT * FROM medicine_pkg.get_medicine(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Medicine.class), medicineID);
    }

    public Photos getPhotoMed(BigDecimal medicineId){
        String sql = "select * from medicine_pkg.get_photo_medicine(?)";
        List<Photos> photos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Photos.class), medicineId);
        if (photos.isEmpty())
            return null;
        else
            return photos.get(0);
    }

    public Photos setPhotoMed(BigDecimal medicineId, String photoUrl){
        String sql = "call medicine_pkg.set_photo_medicine(?, ?, ?, ?)";
        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setBigDecimal(1, medicineId);
            cs.setString(2, photoUrl);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.registerOutParameter(4, Types.NUMERIC);
            cs.execute();
            Photos photos = new Photos();
            photos.setPhotosResult(cs.getString(3), cs.getBigDecimal(4), photoUrl);
            return photos;
        });
    }

    public List<Photos> getPhotosMed(){
        String sql = "select * from medicine_pkg.get_photos_med()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Photos.class));
    }

    public void updatePhoto(BigDecimal medicineId, BigDecimal photoID) {
        String sql = "CALL medicine_pkg.update_photo(?, ?)";

        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setBigDecimal(1, photoID);      // первый параметр - photo_id
            cs.setBigDecimal(2, medicineId);   // второй параметр - medicine_id
            cs.execute();
            return null;
        });
    }

    public Medicine editMedicine(Medicine medicine) {
        String sql = "call medicine_pkg.update_medicine(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;

            // 1 - p_error (OUT параметр)
            cs.registerOutParameter(index++, Types.VARCHAR);

            // 2 - p_id (входной параметр - ID обновляемого препарата)
            cs.setObject(index++, medicine.getId(), Types.NUMERIC);

            // 3 - p_name
            cs.setString(index++, medicine.getName());

            // 4 - p_unit_of_measure
            cs.setObject(index++, medicine.getUnitOfMeasure(), Types.VARCHAR);

            // 5 - p_price
            cs.setObject(index++, medicine.getPrice(), Types.NUMERIC);

            // 6 - p_international_name
            cs.setString(index++, medicine.getInternationalName());

            // 7 - p_dosage_strength
            cs.setString(index++, medicine.getDosageStrength());

            // 8 - p_package_quantity
            cs.setObject(index++, medicine.getPackageQuantity(), Types.INTEGER);

            // 9 - p_prescription_required
            cs.setObject(index++, medicine.getPrescriptionRequired(), Types.BOOLEAN);

            // 10 - p_registration_number
            cs.setString(index++, medicine.getRegistrationNumber());

            // 11 - p_registration_date
            cs.setObject(index++, medicine.getRegistrationDate(), Types.DATE);

            // 12 - p_shelf_life_months
            cs.setObject(index++, medicine.getShelfLifeMonths(), Types.INTEGER);

            // 13 - p_storage_temperature_min
            cs.setObject(index++, medicine.getStorageTemperatureMin(), Types.INTEGER);

            // 14 - p_storage_temperature_max
            cs.setObject(index++, medicine.getStorageTemperatureMax(), Types.INTEGER);

            // 15 - p_requires_refrigeration
            cs.setObject(index++, medicine.getRequiresRefrigeration(), Types.BOOLEAN);

            // 16 - p_light_sensitive
            cs.setObject(index++, medicine.getLightSensitive(), Types.BOOLEAN);

            // 17 - p_requires_dark_storage
            cs.setObject(index++, medicine.getRequiresDarkStorage(), Types.BOOLEAN);

            // 18 - p_is_active
            cs.setObject(index++, medicine.getIsActive(), Types.BOOLEAN);

            // 19 - p_is_vital
            cs.setObject(index++, medicine.getIsVital(), Types.BOOLEAN);

            // 20 - p_is_available
            cs.setObject(index++, medicine.getIsAvailable(), Types.BOOLEAN);

            // 21 - p_pharmacological_group_id
            cs.setObject(index++, medicine.getPharmacologicalGroupId(), Types.NUMERIC);

            // 22 - p_therapeutic_group_id
            cs.setObject(index++, medicine.getTherapeuticGroupId(), Types.NUMERIC);

            // 23 - p_manufacturer_id
            cs.setObject(index++, medicine.getManufacturerId(), Types.NUMERIC);

            // 24 - p_country_id
            cs.setObject(index++, medicine.getCountryId(), Types.NUMERIC);

            // 25 - p_prescription_form_id
            cs.setObject(index++, medicine.getPrescriptionFormId(), Types.NUMERIC);

            // 26 - p_dosage_form_id
            cs.setObject(index++, medicine.getDosageFormId(), Types.NUMERIC);

            // 27 - p_brand_id
            cs.setObject(index++, medicine.getBrandId(), Types.NUMERIC);

            // 28 - p_atc_id
            cs.setObject(index++, medicine.getAtcId(), Types.NUMERIC);

            // 29 - p_package_type_id (последний параметр)
            cs.setObject(index, medicine.getPackageTypeId(), Types.NUMERIC);

            cs.execute();


            String error = cs.getString(1);
            Medicine medicine1 = new Medicine();
            medicine1.setResult(error, medicine.getId());
            return medicine1;
        });
    }

    public Medicine deleteMedicine(BigDecimal medicineId) {
        String sql = "call medicine_pkg.delete_medicine(?)";
        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setBigDecimal(1, medicineId);
            cs.execute();

            BigDecimal idMedicine = BigDecimal.ZERO;
            String error = "Объект успешно удалён";
            Medicine medicine1 = new Medicine();
            medicine1.setResult(error, idMedicine);
            return medicine1;
        });
    }


    public List<Manufacturer> getAllManufacturers() {
        String sql = "select * from medicine_pkg.get_all_manufacturers()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Manufacturer.class));
    }

    public List<Country> getAllCountries() {
        String sql = "select * from medicine_pkg.get_all_countries()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Country.class));
    }

    public List<PharmacologicalGroup> getAllPharmacologicalGroups() {
        String sql = "select * from medicine_pkg.get_all_pharmacologicalGroups()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PharmacologicalGroup.class));
    }

    public List<TherapeuticGroup> getAllTherapeuticGroups() {
        String sql = "select * from medicine_pkg.get_all_therapeuticGroups()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TherapeuticGroup.class));
    }

    public List<DosageForm> getAllDosageForms() {
        String sql = "select * from medicine_pkg.get_all_dosageForms()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DosageForm.class));
    }

    public List<TypePackaging> getAllPackageTypes() {
        String sql = "select * from medicine_pkg.get_all_packageTypes()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TypePackaging.class));
    }

    public List<Brand> getAllBrands() {
        String sql = "select * from medicine_pkg.get_all_brands()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Brand.class));
    }

    public List<Medicine> sortMedicine(Medicine filter) {
        String sql = "{call medicine_pkg.sort_medicine(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;

            // IN параметры
            cs.setObject(index++, filter.getName(), Types.VARCHAR);
            cs.setObject(index++, filter.getInternationalName(), Types.VARCHAR);
            cs.setObject(index++, filter.getDosageStrength(), Types.VARCHAR);
            cs.setObject(index++, filter.getPackageQuantityMin(), Types.INTEGER);
            cs.setObject(index++, filter.getPackageQuantityMax(), Types.INTEGER);
            cs.setObject(index++, filter.getPrescriptionRequired(), Types.BOOLEAN);
            cs.setObject(index++, filter.getPriceMin(), Types.NUMERIC);
            cs.setObject(index++, filter.getPriceMax(), Types.NUMERIC);
            cs.setObject(index++, filter.getManufacturerId(), Types.NUMERIC);
            cs.setObject(index++, filter.getCountryId(), Types.NUMERIC);
            cs.setObject(index++, filter.getPharmacologicalGroupId(), Types.NUMERIC);
            cs.setObject(index++, filter.getTherapeuticGroupId(), Types.NUMERIC);
            cs.setObject(index++, filter.getAtc_code(), Types.VARCHAR);
            cs.setObject(index++, filter.getDosageFormId(), Types.NUMERIC);
            cs.setObject(index++, filter.getPackageTypeId(), Types.NUMERIC);
            cs.setObject(index++, filter.getBrandId(), Types.NUMERIC);
            cs.setObject(index++, filter.getIsActive(), Types.BOOLEAN);

            // Регистрация REF_CURSOR для возвращаемого набора данных
            cs.registerOutParameter(index, Types.REF_CURSOR);

            cs.execute();

            // Получаем результат
            ResultSet rs = (ResultSet) cs.getObject(index);
            List<Medicine> medicines = new ArrayList<>();

            while (rs.next()) {
                Medicine medicine = new Medicine();

                // Основные поля
                medicine.setId(rs.getBigDecimal("id"));
                medicine.setName(rs.getString("name"));
                medicine.setInternationalName(rs.getString("international_name"));
                medicine.setDosageStrength(rs.getString("dosage_strength"));
                medicine.setPackageQuantity(rs.getInt("package_quantity"));
                medicine.setPrescriptionRequired(rs.getBoolean("prescription_required"));
                medicine.setRegistrationNumber(rs.getString("registration_number"));
                medicine.setRegistrationDate(rs.getDate("registration_date") != null ?
                        rs.getDate("registration_date").toLocalDate() : null);
                medicine.setShelfLifeMonths(rs.getInt("shelf_life_months"));
                medicine.setStorageTemperatureMin(rs.getInt("storage_temperature_min"));
                medicine.setStorageTemperatureMax(rs.getInt("storage_temperature_max"));
                medicine.setRequiresRefrigeration(rs.getBoolean("requires_refrigeration"));
                medicine.setLightSensitive(rs.getBoolean("light_sensitive"));
                medicine.setRequiresDarkStorage(rs.getBoolean("requires_dark_storage"));
                medicine.setIsActive(rs.getBoolean("is_active"));
                medicine.setIsVital(rs.getBoolean("is_vital"));
                medicine.setIsAvailable(rs.getBoolean("is_available"));
                medicine.setPrice(rs.getBigDecimal("price"));
                medicine.setUnitOfMeasure(rs.getString("unit_of_measure"));

                // ID справочников
                medicine.setPharmacologicalGroupId(rs.getBigDecimal("pharmacological_group_id"));
                medicine.setTherapeuticGroupId(rs.getBigDecimal("therapeutic_group_id"));
                medicine.setManufacturerId(rs.getBigDecimal("manufacturer_id"));
                medicine.setCountryId(rs.getBigDecimal("country_id"));
                medicine.setPrescriptionFormId(rs.getBigDecimal("prescription_form_id"));
                medicine.setDosageFormId(rs.getBigDecimal("dosage_form_id"));
                medicine.setBrandId(rs.getBigDecimal("brand_id"));
                medicine.setAtcId(rs.getBigDecimal("atc_id"));
                medicine.setPackageTypeId(rs.getBigDecimal("package_type_id"));

                // Transient поля
                medicine.setBrandName(rs.getString("brandname"));
                medicine.setCountryName(rs.getString("countryname"));
                medicine.setAtc_code(rs.getString("atc_code"));
                medicine.setManufacturer_name(rs.getString("manufacturer_name"));
                medicine.setPackage_type_name(rs.getString("package_type_name"));
                medicine.setPharmacological_group_name(rs.getString("pharmacological_group_name"));
                medicine.setTherapeutic_group_name(rs.getString("therapeutic_group_name"));
                medicine.setDosageFormName(rs.getString("dosageformname"));
                medicine.setPrescription_form_name(rs.getString("prescription_form_name"));

                medicines.add(medicine);
            }

            rs.close();
            return medicines;
        });
    }
}
