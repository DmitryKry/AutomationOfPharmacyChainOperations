package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final VigenereCipherService cipherService;

    public UserService(JdbcTemplate jdbcTemplate, VigenereCipherService cipherService) {

        this.jdbcTemplate = jdbcTemplate;
        this.cipherService = cipherService;
    }

    public UserOfPharmacy findOfUser(String login, String password) {
        String sql = "SELECT * FROM user_pkg.get_of_user(?, ?)";

        String encodedLogin = cipherService.encrypt(login);
        String encodedPassword = cipherService.encrypt(password);

        List<UserOfPharmacy> users = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(UserOfPharmacy.class),
                encodedLogin, encodedPassword
        );

        for (UserOfPharmacy user : users) {
            user.setLogin(cipherService.decrypt(user.getLogin()));
            user.setPhone(cipherService.decrypt(user.getPhone()));
            user.setFio(cipherService.decrypt(user.getFio()));
            user.setEmail(cipherService.decrypt(user.getEmail()));
            user.setPassword(cipherService.decrypt(user.getPassword()));
            if (user.getPassport() != null) {
                user.setPassport(cipherService.decrypt(user.getPassport()));
            }
        }

        return users.isEmpty() ? null : users.get(0);
    }

    public String UserCreate(UserOfPharmacy user) {

        String sql = "CALL user_pkg.set_of_user(?, ?, ?, ?, ?, ?, ?, ?)";

        UserOfPharmacy encodedUser = UserOfPharmacy.builder()
                .login(cipherService.encrypt(user.getLogin()))
                .dataOfBirt(user.getDataOfBirt())
                .phone(cipherService.encrypt(user.getPhone()))
                .passport(user.getPassport() != null ? cipherService.encrypt(user.getPassport()) : null)
                .fio(cipherService.encrypt(user.getFio()))
                .email(cipherService.encrypt(user.getEmail()))
                .password(cipherService.encrypt(user.getPassword()))
                .build();
        String errorMessage = jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setString(1, encodedUser.getLogin());
            cs.setDate(2, Date.valueOf(encodedUser.getDataOfBirt()));
            cs.setString(3, encodedUser.getPhone());
            cs.setString(4, encodedUser.getPassport());
            cs.setString(5, encodedUser.getFio());
            cs.setString(6, encodedUser.getEmail());
            cs.setString(7, encodedUser.getPassword());
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
        List<Workers> workers = jdbcTemplate.query(sql, new Object[]{fio}, new BeanPropertyRowMapper<>(Workers.class));

        for (Workers worker : workers) {
            worker.setUserFio(cipherService.decrypt(worker.getUserFio()));
        }
        return workers;
    }

    public List<Workers> getAdministrators() {
        String sql = "SELECT * FROM user_pkg.get_administrators() as (id numeric, user_fio varchar)";
        List<Workers> workers = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(Workers.class));
        for (Workers worker : workers) {
            worker.setUserFio(cipherService.decrypt(worker.getUserFio()));
        }
        return workers;
    }

    public List<UserOfPharmacy> searchUsers(@RequestParam String fio) {
        String sql = "SELECT * FROM user_pkg.get_users(?)";
        List<UserOfPharmacy> userOfPharmacies = jdbcTemplate.query(sql, new Object[]{fio}, new BeanPropertyRowMapper<>(UserOfPharmacy.class));

        for (UserOfPharmacy user : userOfPharmacies) {
            user.setLogin(cipherService.decrypt(user.getLogin()));
            user.setPhone(cipherService.decrypt(user.getPhone()));
            user.setFio(cipherService.decrypt(user.getFio()));
            user.setEmail(cipherService.decrypt(user.getEmail()));
            user.setPassword(cipherService.decrypt(user.getPassword()));
            if (user.getPassport() != null) {
                user.setPassport(cipherService.decrypt(user.getPassport()));
            }
        }
        return userOfPharmacies;
    }

    public List<Role> getRoles() {
        String sql = "SELECT * FROM user_pkg.get_roles()";
        return jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(Role.class));
    }

    public Workers workerCreate(Workers worker) {

        String sql = "CALL user_pkg.set_worker(?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setBigDecimal(1, worker.getId_of_user());
            cs.setBigDecimal(2, worker.getSalary());
            cs.setString(3, worker.getEducation());
            cs.setBigDecimal(4, worker.getId_of_role());
            cs.setString(5, worker.getInn());
            cs.setString(6, worker.getSnils());
            cs.registerOutParameter(7, Types.VARCHAR);
            cs.registerOutParameter(8, Types.NUMERIC);
            cs.execute();
            Workers workers = new Workers();
            workers.setWorkerResult(cs.getString(7), cs.getBigDecimal(8));
            return workers;
        });
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
        List<UserOfPharmacy> userOfPharmacies = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserOfPharmacy.class), idPharmacy);
        for (UserOfPharmacy worker : userOfPharmacies) {
            worker.setFio(cipherService.decrypt(worker.getFio()));
        }
        return userOfPharmacies;
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
        List<UserOfPharmacy> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserOfPharmacy.class), id);
        for (UserOfPharmacy user : users) {
            user.setLogin(cipherService.decrypt(user.getLogin()));
            user.setPhone(cipherService.decrypt(user.getPhone()));
            user.setFio(cipherService.decrypt(user.getFio()));
            user.setEmail(cipherService.decrypt(user.getEmail()));
            user.setPassword(cipherService.decrypt(user.getPassword()));
            if (user.getPassport() != null) {
                user.setPassport(cipherService.decrypt(user.getPassport()));
            }
        }

        return users.isEmpty() ? null : users.get(0);
    }

    public void updateUser(UserOfPharmacy user) {
        String sql = "call user_pkg.update_user(?, ?, ?, ?, ?, ?, ?, ?)";

        UserOfPharmacy encodedUser = UserOfPharmacy.builder()
                .login(cipherService.encrypt(user.getLogin()))
                .dataOfBirt(user.getDataOfBirt())
                .phone(cipherService.encrypt(user.getPhone()))
                .passport(user.getPassport() != null ? cipherService.encrypt(user.getPassport()) : null)
                .fio(cipherService.encrypt(user.getFio()))
                .email(cipherService.encrypt(user.getEmail()))
                .password(cipherService.encrypt(user.getPassword()))
                .build();
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;

            cs.setBigDecimal(index++, user.getId());           // 1 - p_id
            cs.setDate(index++, java.sql.Date.valueOf(encodedUser.getDataOfBirt()));    // 2 - p_name
            cs.setString(index++, encodedUser.getPhone());   // 3 - p_legal_name
            cs.setString(index++, encodedUser.getLogin());          // 4 - p_inn
            cs.setString(index++, encodedUser.getEmail());          // 5 - p_kpp
            cs.setString(index++, encodedUser.getFio());         // 6 - p_ogrn
            cs.setString(index++, encodedUser.getPassword());      // 7 - p_address
            cs.setString(index, encodedUser.getPassport());      // 8 - p_ID_CITY// 11 - id_of_worker (IN)


            cs.execute();
            return null;
        });

    }

    public Workers getWorkerId(BigDecimal userID){
        String sql = "SELECT * FROM user_pkg.get_worker_id(?)";
        List<Workers> workers = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Workers.class), userID);
        if (workers.isEmpty()) {
            return null;
        }
        return workers.get(0);
    }
}
