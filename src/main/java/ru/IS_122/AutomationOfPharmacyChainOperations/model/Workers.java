package ru.IS_122.AutomationOfPharmacyChainOperations.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "workers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Workers {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_seq")
    @SequenceGenerator(name = "allobjects_seq", sequenceName = "allobjects", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private BigDecimal id;

    @Column(name = "id_of_user")
    private BigDecimal id_of_user;

    @Column(name = "salary", precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(name = "hire_date", nullable = false)
    @ColumnDefault("CURRENT_DATE")
    private LocalDate hire_date;

    @Column(name = "dismissal_date")
    private LocalDate dismissal_date;

    @Column(name = "inn", length = 12)
    private String inn;

    @Column(name = "snils", length = 11)
    private String snils;

    @Column(name = "payment_account", length = 20)
    private String payment_account;

    @Column(name = "education", columnDefinition = "TEXT")
    private String education;

    @Column(name = "certificate_number", length = 50)
    private String certificate_number;

    @Column(name = "certificate_expiry")
    private LocalDate certificate_expiry;

    @Column(name = "created_at")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime updated_at;

    @Column(name = "id_of_role")
    private BigDecimal id_of_role;

    @Transient
    private String userFio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_of_user", insertable = false, updatable = false)
    private UserOfPharmacy user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_of_role", insertable = false, updatable = false)
    private Role role;
}