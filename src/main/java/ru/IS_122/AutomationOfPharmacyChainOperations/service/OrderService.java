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
import java.util.Objects;

@Service
public class OrderService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createOrder(BigDecimal userID) {
        String sql = "call order_pkg.create_order(?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index, userID);
            cs.execute();
            return null;
        });
    }

    public void createOrderMedicine(BigDecimal userID, BigDecimal medicineID) {
        String sql = "call order_pkg.create_order_medicine(?, ?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index++, userID);
            cs.setBigDecimal(index, medicineID);
            cs.execute();
            return null;
        });
    }

    public void updateOrderMedicine(BigDecimal userID, BigDecimal medicineID, BigDecimal quantity) {
        String sql = "call order_pkg.update_order_medicine(?, ?, ?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index++, userID);
            cs.setBigDecimal(index++, medicineID);
            cs.setBigDecimal(index, quantity);
            cs.execute();
            return null;
        });
    }

    public BigDecimal endOrder(BigDecimal userID, BigDecimal pharmacyID) {
        String sql = "call order_pkg.end_order(?, ?, ?)";
        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index++, userID);
            cs.setBigDecimal(index, pharmacyID);
            cs.registerOutParameter(3, Types.NUMERIC);
            cs.execute();

            return cs.getBigDecimal(3);
        });
    }

    public void deleteMedicineOrder(BigDecimal orders_Medicine_Id) {
        String sql = "call order_pkg.delete_medicine_order(?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index++, orders_Medicine_Id);
            cs.execute();
            return null;
        });
    }

    public void deletePharmacyOrder(BigDecimal ordersPharmacyID) {
        String sql = "call order_pkg.orders_pharmacy_Id(?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index++, ordersPharmacyID);
            cs.execute();
            return null;
        });
    }

    public List<Order> getAllOrdersAdmin() {
        String sql = "select * from order_pkg.get_all_orders_admin()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    public List<Order> getAllOrders() {
        String sql = "select * from order_pkg.get_all_orders()";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    public List<Order> getOrderPharmacy(BigDecimal pharmacyId) {
        String sql = "select * from order_pkg.get_order_pharmacy(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class), pharmacyId);
    }

    public List<Medicine> getMedOrders(BigDecimal userId) {
        String sql = "select * from order_pkg.get_med_orders(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Medicine.class), userId);
    }

    public void update_count(BigDecimal userId, BigDecimal medicineId, BigDecimal count) {
        String sql = "call order_pkg.update_count(?, ?, ?)";
        jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            int index = 1;
            cs.setBigDecimal(index++, userId);
            cs.setBigDecimal(index++, medicineId);
            cs.setBigDecimal(index++, count);
            cs.execute();
            return null;
        });
    }

    public List<Medicine> getMedOrdersIdOrder(BigDecimal orderID) {
        String sql = "select * from order_pkg.get_med_orders_id_order(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Medicine.class), orderID);
    }

    public List<OrderMedicine> getMedOrder(BigDecimal userID) {
        String sql = "select * from order_pkg.get_med_order(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OrderMedicine.class), userID);
    }

    public List<OrderMedicine> getMedOrderPharmacy(BigDecimal orderID) {
        String sql = "select * from order_pkg.get_med_order_pharmacy(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OrderMedicine.class), orderID);
    }

    public List<Order> getAllOrdersUser(BigDecimal userID) {
        String sql = "select * from order_pkg.get_all_orders_user(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class), userID);
    }

    public Order getOrderId(BigDecimal orderID) {
        String sql = "select * from order_pkg.get_order_id(?)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class), orderID).get(0);
    }

 }
