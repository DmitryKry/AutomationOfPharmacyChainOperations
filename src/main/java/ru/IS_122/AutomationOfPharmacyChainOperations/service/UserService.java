package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.UserOfPharmacy;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.Workers;

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

    public String UserCreate(UserOfPharmacy user) {

        String sql = "CALL user_pkg.set_of_user(?, ?, ?, ?, ?, ?, ?, ?)";

        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, user.getLogin());
            cs.setDate(2, Date.valueOf(user.getDataOfBirt()));
            cs.setString(3, user.getPhone());
            cs.setString(4, user.getPassport());
            cs.setString(5, user.getFio());
            cs.setString(6, user.getEmail());
            cs.setString(7, user.getPassword());
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.execute();
            return cs.getString(8);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }

    public List<Workers> searchWorkers(@RequestParam String fio) {
        String sql = "SELECT * FROM user_pkg.get_administrator(?)";
        return jdbcTemplate.query(sql, new Object[]{fio}, new BeanPropertyRowMapper<>(Workers.class));
    }
}
