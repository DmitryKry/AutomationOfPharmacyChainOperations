package ru.IS_122.AutomationOfPharmacyChainOperations.service;
import ru.IS_122.AutomationOfPharmacyChainOperations.model.*;
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
public class OrderService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createOrder(BigDecimal userID) {
        String sql = "call order_pkg.create_order(?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index, userID);
            return null;
        });
    }

    public void createOrderMedicine(BigDecimal userID, BigDecimal medicineID) {
        List<Order> orders = getAllOrders().stream()
                .filter(order -> order.getIdOfUser().equals(userID)
                        && order.getIdOfPharmacy() == null)
                .toList();
        if (orders.isEmpty()){
            createOrder(userID);
        }
        String sql = "call order_pkg.create_order_medicine(?, ?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index, userID);
            cs.setBigDecimal(index, medicineID);
            return null;
        });
    }

    public void updateOrderMedicine(BigDecimal userID, BigDecimal medicineID, BigDecimal quantity) {
        String sql = "call order_pkg.update_order_medicine(?, ?, ?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index, userID);
            cs.setBigDecimal(index, medicineID);
            cs.setBigDecimal(index, quantity);
            return null;
        });
    }

    public void endOrder(BigDecimal userID, BigDecimal pharmacyID) {
        String sql = "call order_pkg.end_order(?, ?, ?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index, userID);
            cs.setBigDecimal(index, pharmacyID);
            return null;
        });
    }

    public void deleteMedicineOrder(BigDecimal orders_Medicine_Id) {
        String sql = "call order_pkg.delete_medicine_order(?, ?, ?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index, orders_Medicine_Id);
            return null;
        });
    }

    public void deletePharmacyOrder(BigDecimal ordersPharmacyID) {
        String sql = "call order_pkg.orders_pharmacy_Id(?, ?, ?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index, ordersPharmacyID);
            return null;
        });
    }

    public List<Order> getAllOrdersAdmin() {
        String sql = "call order_pkg.get_all_orders_admin()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    public List<Order> getAllOrders() {
        String sql = "call order_pkg.get_all_orders()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    public List<Order> getOrderPharmacy(BigDecimal pharmacyId) {
        String sql = "call order_pkg.get_order_pharmacy()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    public List<Order> getMedOrder(BigDecimal user_id) {
        String sql = "call order_pkg.get_med_order()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }
}
