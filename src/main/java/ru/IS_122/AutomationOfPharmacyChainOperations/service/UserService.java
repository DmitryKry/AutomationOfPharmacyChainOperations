package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.UserOfPharmacy;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public UserOfPharmacy findOfUser(String login, String password){
        String sql = "SELECT * FROM user_pkg.get_of_user(?, ?)";

        List<UserOfPharmacy> users = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(UserOfPharmacy.class),
                login, password
        );
        return users.get(0);
    }
}
