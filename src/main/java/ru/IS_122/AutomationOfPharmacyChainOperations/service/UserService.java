package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
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

    public List<Workers> searchAdministrators(@RequestParam String fio) {
        String sql = "SELECT * FROM user_pkg.get_administrator(?)";
        return jdbcTemplate.query(sql, new Object[]{fio}, new BeanPropertyRowMapper<>(Workers.class));
    }

    public List<Workers> getAdministrators() {
        String sql = "SELECT * FROM user_pkg.get_administrators() as (id numeric, user_fio varchar)";
        return jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(Workers.class));
    }

    public List<UserOfPharmacy> searchUsers(@RequestParam String fio) {
        String sql = "SELECT * FROM user_pkg.get_users(?)";
        return jdbcTemplate.query(sql, new Object[]{fio}, new BeanPropertyRowMapper<>(UserOfPharmacy.class));
    }

    public List<Role> getRoles() {
        String sql = "SELECT * FROM user_pkg.get_roles()";
        return jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(Role.class));
    }

    public String workerCreate(Workers worker) {

        String sql = "CALL user_pkg.set_worker(?, ?, ?, ?, ?, ?, ?)";

        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setBigDecimal(1, worker.getId_of_user());
            cs.setBigDecimal(2, worker.getSalary());
            cs.setString(3, worker.getEducation());
            cs.setBigDecimal(4, worker.getId_of_role());
            cs.setString(5, worker.getInn());
            cs.setString(6, worker.getSnils());
            cs.registerOutParameter(7, Types.VARCHAR);
            cs.execute();
            return cs.getString(7);
        });

        if (errorMessage != null && !errorMessage.isEmpty()) {
            return errorMessage;
        }

        return null;
    }

    public void roleCreate(Role role) {

        String sql = "CALL user_pkg.set_role(?)";

        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, role.getName());
            return cs.execute();
        });
    }

    public List<UserOfPharmacy> getAdminPharmacy(BigDecimal idPharmacy) {
        String sql = "SELECT * FROM user_pkg.get_admin_pharmacy(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserOfPharmacy.class), idPharmacy);
    }

    public Photos getPhoto(BigDecimal photoID){
        String sql = "SELECT * FROM user_pkg.get_photo(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Photos.class), photoID).get(0);
    }

    public Photos findPhotoPath(String photoPath){
        String sql = "SELECT * FROM user_pkg.find_photo_path(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Photos.class), photoPath).get(0);
    }

    public UserOfPharmacy get_id_user(BigDecimal id){
        String sql = "SELECT * FROM user_pkg.get_id_user(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserOfPharmacy.class), id).get(0);
    }

    public void updateUser(UserOfPharmacy user) {
        String sql = "call user_pkg.update_user(?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;

            cs.setBigDecimal(index++, user.getId());           // 1 - p_id
            cs.setDate(index++, java.sql.Date.valueOf(user.getDataOfBirt()));    // 2 - p_name
            cs.setString(index++, user.getPhone());   // 3 - p_legal_name
            cs.setString(index++, user.getLogin());          // 4 - p_inn
            cs.setString(index++, user.getEmail());          // 5 - p_kpp
            cs.setString(index++, user.getFio());         // 6 - p_ogrn
            cs.setString(index++, user.getPassword());      // 7 - p_address
            cs.setString(index, user.getPassport());      // 8 - p_ID_CITY// 11 - id_of_worker (IN)


            cs.execute();
            return null;
        });

    }
}
