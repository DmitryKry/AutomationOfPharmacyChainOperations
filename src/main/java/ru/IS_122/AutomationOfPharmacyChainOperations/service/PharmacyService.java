package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.City;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.Pharmacy;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.Photos;


import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
@Service
public class PharmacyService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Pharmacy> getAllPharmacies() {
        String sql = "SELECT * FROM pharmacy_pkg.get_all_pharmacies_table()";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Pharmacy.class));
    }

    public Pharmacy createPharmacy(Pharmacy pharmacy, BigDecimal idAdmin) {
        String sql = "CALL pharmacy_pkg.create_new_pharmacy(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, pharmacy.getName());
            cs.setString(2, pharmacy.getLegal_name());
            cs.setString(3, pharmacy.getInn());
            cs.setString(4, pharmacy.getKpp());
            cs.setString(5, pharmacy.getOgrn());
            cs.setString(6, pharmacy.getAddress());
            cs.setObject(7, pharmacy.getID_CITY());
            cs.setString(8, pharmacy.getRegion());
            cs.setString(9, pharmacy.getPostal_code());
            cs.setObject(10, idAdmin);

            cs.registerOutParameter(11, Types.VARCHAR);
            cs.registerOutParameter(12, Types.NUMERIC);

            cs.execute();

            String error = cs.getString(11);
            BigDecimal id = cs.getBigDecimal(12);
            Pharmacy p = new Pharmacy();
            p.setPharmacyResult(error, id);
            return p;
        });
    }

    public List<City> getAllCities() {
        String sql = "SELECT * FROM pharmacy_pkg.get_all_city()";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(City.class));
    }

    public List<City> findCities(String city) {
        String sql = "SELECT * FROM pharmacy_pkg.get_all_city(?)";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(City.class), city);
    }

    public String createCity(City city) {
        String sql = "CALL pharmacy_pkg.create_new_city(?, ?, ?)";

        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, city.getName());
            cs.setString(2, city.getRegion());
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }

    public List<Pharmacy> getPharmacyById(BigDecimal id) {
        String sql = "SELECT * FROM pharmacy_pkg.get_pharmacies(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Pharmacy.class), id);
    }

    public Photos getPhotoPharmacy(BigDecimal pharmacyId){
        String sql = "select * from pharmacy_pkg.get_photo_pharmacy(?)";
        List<Photos> photos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Photos.class), pharmacyId);
        if (photos.isEmpty())
            return null;
        else
            return photos.get(0);
    }

    public Photos setPhotoPharmacy(BigDecimal pharmacyId, String photoUrl){
        String sql = "call pharmacy_pkg.set_photo_pharmacy(?, ?, ?, ?)";
        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setBigDecimal(1, pharmacyId);
            cs.setString(2, photoUrl);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.registerOutParameter(4, Types.NUMERIC);
            cs.execute();
            Photos photos = new Photos();
            photos.setPhotosResult(cs.getString(3), cs.getBigDecimal(4), photoUrl);
            return photos;
        });
    }

    public List<Photos> getPhotosPharmacy(){
        String sql = "select * from pharmacy_pkg.get_photos_pharmacy()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Photos.class));
    }

    public void updatePhoto(BigDecimal pharmacyId, BigDecimal photoID){
        String sql = "call pharmacy_pkg.update_photo(?, ?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setBigDecimal(1, photoID);      // первый параметр - photo_id
            cs.setBigDecimal(2, pharmacyId);   // второй параметр - pharmacyId
            cs.execute();
            return null;
        });
    }

    public Pharmacy updatePharmacy(Pharmacy pharmacy, BigDecimal idAdmin) {
        String sql = "CALL pharmacy_pkg.update_pharmacy(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;

            cs.setObject(index++, pharmacy.getId());           // 1 - p_id
            cs.setString(index++, pharmacy.getName());         // 2 - p_name
            cs.setString(index++, pharmacy.getLegal_name());   // 3 - p_legal_name
            cs.setString(index++, pharmacy.getInn());          // 4 - p_inn
            cs.setString(index++, pharmacy.getKpp());          // 5 - p_kpp
            cs.setString(index++, pharmacy.getOgrn());         // 6 - p_ogrn
            cs.setString(index++, pharmacy.getAddress());      // 7 - p_address
            cs.setObject(index++, pharmacy.getID_CITY());      // 8 - p_ID_CITY
            cs.setString(index++, pharmacy.getRegion());       // 9 - p_region
            cs.setString(index++, pharmacy.getPostal_code());  // 10 - p_postal_code
            cs.setObject(index++, idAdmin);                    // 11 - id_of_worker (IN)

            cs.registerOutParameter(index, Types.VARCHAR);     // 12 - p_error

            cs.execute();

            String error = cs.getString(12);

            Pharmacy p = new Pharmacy();
            p.setPharmacyResult(error, pharmacy.getId());
            return p;
        });
    }

    public Pharmacy deletePharmacy(BigDecimal pharmacyId) {
        String sql = "CALL pharmacy_pkg.delete_pharmacy(?)";
        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setBigDecimal(1, pharmacyId);

            cs.execute();

            String error = "Объект успешно удалён";
            BigDecimal id = BigDecimal.ZERO;
            Pharmacy p = new Pharmacy();
            p.setPharmacyResult(error, id);
            return p;
        });
    }

    public List<Pharmacy> sortPharmacies(String name,String region, String city, String adress){
        String sql = "select * from pharmacy_pkg.sort_pharmacy(?, ?, ?, ?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Pharmacy.class), name, city, adress, region);
    }
}
