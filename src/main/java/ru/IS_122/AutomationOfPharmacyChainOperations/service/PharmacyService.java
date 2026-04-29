package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.City;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.Pharmacy;


import java.sql.CallableStatement;
import java.sql.Date;
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

    public String createPharmacy(Pharmacy pharmacy) {
        String sql = "CALL user_pkg.create_new_pharmacy(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, pharmacy.getName());
            cs.setString(2, pharmacy.getLegal_name());
            cs.setString(3, pharmacy.getInn());
            cs.setString(4, pharmacy.getKpp());
            cs.setString(5, pharmacy.getOgrn());
            cs.setString(6, pharmacy.getAddress());
            cs.setBigDecimal(7, pharmacy.getID_CITY());
            cs.setString(8, pharmacy.getRegion());
            cs.setString(9, pharmacy.getPostal_code());
            cs.registerOutParameter(10, Types.VARCHAR);
            cs.execute();
            return cs.getString(10);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
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
}
