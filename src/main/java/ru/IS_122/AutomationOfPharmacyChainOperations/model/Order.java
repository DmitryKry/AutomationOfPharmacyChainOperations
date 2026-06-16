package ru.IS_122.AutomationOfPharmacyChainOperations.model;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_seq")
    @SequenceGenerator(name = "allobjects_seq", sequenceName = "allobjects", allocationSize = 1)
    private Long id;

    @Column(name = "id_of_pharmacy")
    private Long idOfPharmacy;

    @Column(name = "id_of_user")
    private Long idOfUser;

    @Column(name = "id_user_role")
    private Long idUserRole;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "status_pay")
    private Boolean statusPay;

    @Transient
    private String pharmacyName;

    @Transient
    private String pharmacyAddress;

    @Transient
    private String pharmacyCity;

    @Transient
    private String userFio;

    public boolean isStatusPay() {
        return statusPay != null && statusPay;
    }
}