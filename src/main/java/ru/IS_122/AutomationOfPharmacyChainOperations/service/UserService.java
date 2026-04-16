package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.UserOfPharmacy;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserOfPharmacy findOfUser(String login, String password) {
        String sql = "SELECT * FROM user_pkg.get_of_user(?, ?)";

        List<UserOfPharmacy> users = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(UserOfPharmacy.class),
                login, password
        );
        return users.isEmpty() ? null : users.get(0);
    }

    public String UserCreate(String login,
                             String password,
                             LocalDate data_of_birt,
                             String phone,
                             String passport,
                             String fio,
                             String email) {

        String sql = "CALL user_pkg.set_of_user(?, ?, ?, ?, ?, ?, ?, ?)";

        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, login);
            cs.setDate(2, Date.valueOf(data_of_birt));
            cs.setString(3, phone);
            cs.setString(4, passport);
            cs.setString(5, fio);
            cs.setString(6, email);
            cs.setString(7, password);
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.execute();
            return cs.getString(8);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }
}
